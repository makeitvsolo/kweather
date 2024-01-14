package github.makeitvsolo.kweather.user.access.api.service.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.api.datasource.operation.MapSaveUserErrorInto
import github.makeitvsolo.kweather.user.access.api.service.dto.UserDto

typealias RegisterUserPayload = UserDto

interface MapRegisterUserErrorInto<out R> : Into<R> {

    fun fromConflictError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface RegisterUserError {

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
    }

    data class InternalError(private val throwable: Throwable) : RegisterUserError {

        override fun <R, M : MapRegisterUserErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)
    }
}

interface RegisterUser {

    fun register(payload: RegisterUserPayload): Result<Unit, RegisterUserError>
}
