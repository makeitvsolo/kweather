package github.makeitvsolo.kweather.user.access.api.service.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.api.security.session.MapDecodeTokenErrorInto
import github.makeitvsolo.kweather.user.access.api.service.dto.AccessTokenDto

typealias RefreshAccessTokenPayload = AccessTokenDto
typealias RefreshAccessTokenResponse = AccessTokenDto

interface MapRefreshAccessTokenErrorInto<out R> : Into<R> {

    fun fromInvalidTokenError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface RefreshAccessTokenError {

    object FromDecodeTokenError : MapDecodeTokenErrorInto<RefreshAccessTokenError> {

        override fun fromInvalidTokenError(details: String): RefreshAccessTokenError =
            InvalidTokenError(details)

        override fun fromInternalError(throwable: Throwable): RefreshAccessTokenError =
            InternalError(throwable)
    }

    fun <R, M : MapRefreshAccessTokenErrorInto<R>> into(map: M): R

    data class InvalidTokenError(private val details: String) : RefreshAccessTokenError {

        override fun <R, M : MapRefreshAccessTokenErrorInto<R>> into(map: M): R =
            map.fromInvalidTokenError(details)
    }

    data class InternalError(private val throwable: Throwable) : RefreshAccessTokenError {

        override fun <R, M : MapRefreshAccessTokenErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)
    }
}

interface RefreshAccessToken {

    fun refresh(payload: RefreshAccessTokenPayload): Result<RefreshAccessTokenResponse, RefreshAccessTokenError>
}
