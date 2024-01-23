package github.makeitvsolo.kweather.user.access.api.service.user.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.api.datasource.user.error.MapSaveUserErrorInto
import github.makeitvsolo.kweather.user.access.api.service.user.exception.UserServiceException

interface MapRegisterUserErrorInto<out R> : Into<R> {

    fun fromConflictError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface RegisterUserError : IntoThrowable {

    object FromSaveUserError : MapSaveUserErrorInto<RegisterUserError> {

        override fun fromConflictError(details: String): RegisterUserError =
            ConflictError(details)

        override fun fromInternalError(throwable: Throwable): RegisterUserError =
            InternalError(throwable)
    }

    fun <R, M : MapRegisterUserErrorInto<R>> into(map: M): R

    data class ConflictError(private val details: String) : RegisterUserError {

        override fun <R, M : MapRegisterUserErrorInto<R>> into(map: M): R =
            map.fromConflictError(details)

        override fun intoThrowable(): Throwable = UserServiceException(details)
    }

    data class InternalError(private val throwable: Throwable) : RegisterUserError {

        override fun <R, M : MapRegisterUserErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
