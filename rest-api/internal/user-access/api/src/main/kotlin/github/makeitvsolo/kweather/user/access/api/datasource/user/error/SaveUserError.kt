package github.makeitvsolo.kweather.user.access.api.datasource.user.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.api.datasource.user.exception.UserRepositoryException

interface MapSaveUserErrorInto<out R> : Into<R> {

    fun fromConflictError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface SaveUserError : IntoThrowable {

    fun <R, M : MapSaveUserErrorInto<R>> into(map: M): R

    data class ConflictError(private val details: String) : SaveUserError {

        override fun <R, M : MapSaveUserErrorInto<R>> into(map: M): R =
            map.fromConflictError(details)

        override fun intoThrowable(): Throwable = UserRepositoryException(details)
    }

    data class InternalError(private val throwable: Throwable) : SaveUserError {

        override fun <R, M : MapSaveUserErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
