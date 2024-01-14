package github.makeitvsolo.kweather.user.access.api.service.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.api.security.session.MapDecodeTokenErrorInto
import github.makeitvsolo.kweather.user.access.api.service.dto.AccessTokenDto
import github.makeitvsolo.kweather.user.access.api.service.dto.ActiveUserDto

typealias AuthenticateUserPayload = AccessTokenDto
typealias AuthenticateUserResponse = ActiveUserDto

interface MapAuthenticateUserErrorInto<out R> : Into<R> {

    fun fromInvalidTokenError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface AuthenticateUserError {

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
    }

    data class InternalError(private val throwable: Throwable) : AuthenticateUserError {

        override fun <R, M : MapAuthenticateUserErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)
    }
}

interface AuthenticateUser {

    fun authenticate(payload: AuthenticateUserPayload): Result<AuthenticateUserResponse, AuthenticateUserError>
}
