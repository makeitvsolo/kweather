package github.makeitvsolo.kweather.user.access.api.service.user.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.api.datasource.user.error.MapFindUserErrorInto
import github.makeitvsolo.kweather.user.access.api.service.user.exception.UserServiceException

interface MapAuthorizeUserErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInvalidCredentialsError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface AuthorizeUserError : IntoThrowable {

    object FromFindUserError : MapFindUserErrorInto<AuthorizeUserError> {

        override fun fromNotFoundError(details: String): AuthorizeUserError =
            NotFoundError(details)

        override fun fromInternalError(throwable: Throwable): AuthorizeUserError =
            InternalError(throwable)
    }

    fun <R, M : MapAuthorizeUserErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : AuthorizeUserError {

        override fun <R, M : MapAuthorizeUserErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = UserServiceException(details)
    }

    data class InternalError(private val throwable: Throwable) : AuthorizeUserError {

        override fun <R, M : MapAuthorizeUserErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }

    data class InvalidCredentialsError(private val details: String = "invalid credentials") : AuthorizeUserError {

        override fun <R, M : MapAuthorizeUserErrorInto<R>> into(map: M): R =
            map.fromInvalidCredentialsError(details)

        override fun intoThrowable(): Throwable = UserServiceException(details)
    }
}
