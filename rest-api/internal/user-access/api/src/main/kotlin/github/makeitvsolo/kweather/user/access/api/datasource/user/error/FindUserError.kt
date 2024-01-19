package github.makeitvsolo.kweather.user.access.api.datasource.user.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.api.datasource.user.exception.UserDoesNotExistsException

interface MapFindUserErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FindUserError : IntoThrowable {

    fun <R, M : MapFindUserErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : FindUserError {

        override fun <R, M : MapFindUserErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = UserDoesNotExistsException(details)
    }

    data class InternalError(private val throwable: Throwable) : FindUserError {

        override fun <R, M : MapFindUserErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
