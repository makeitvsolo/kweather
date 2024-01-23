package github.makeitvsolo.kweather.user.access.api.service.user.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.api.security.session.error.MapDecodeTokenErrorInto
import github.makeitvsolo.kweather.user.access.api.service.user.exception.UserServiceException

interface MapAuthenticateUserErrorInto<out R> : Into<R> {

    fun fromInvalidTokenError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface AuthenticateUserError : IntoThrowable {

    object FromDecodeTokenError : MapDecodeTokenErrorInto<AuthenticateUserError> {

        override fun fromInvalidTokenError(details: String): AuthenticateUserError =
            InvalidTokenError(details)

        override fun fromInternalError(throwable: Throwable): AuthenticateUserError =
            InternalError(throwable)
    }

    fun <R, M : MapAuthenticateUserErrorInto<R>> into(map: M): R

    data class InvalidTokenError(private val details: String) : AuthenticateUserError {

        override fun <R, M : MapAuthenticateUserErrorInto<R>> into(map: M): R =
            map.fromInvalidTokenError(details)

        override fun intoThrowable(): Throwable = UserServiceException(details)
    }

    data class InternalError(private val throwable: Throwable) : AuthenticateUserError {

        override fun <R, M : MapAuthenticateUserErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
