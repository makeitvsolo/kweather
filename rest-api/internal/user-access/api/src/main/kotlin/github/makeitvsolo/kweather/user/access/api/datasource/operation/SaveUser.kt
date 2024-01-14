package github.makeitvsolo.kweather.user.access.api.datasource.operation

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.domain.User

interface MapSaveUserErrorInto<out R> : Into<R> {

    fun fromConflictError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface SaveUserError {

    fun <R, M : MapSaveUserErrorInto<R>> into(map: M): R

    data class ConflictError(private val details: String) : SaveUserError {

        override fun <R, M : MapSaveUserErrorInto<R>> into(map: M): R =
            map.fromConflictError(details)
    }

    data class InternalError(private val throwable: Throwable) : SaveUserError {

        override fun <R, M : MapSaveUserErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)
    }
}

interface SaveUser {

    fun save(user: User): Result<Unit, SaveUserError>
}
