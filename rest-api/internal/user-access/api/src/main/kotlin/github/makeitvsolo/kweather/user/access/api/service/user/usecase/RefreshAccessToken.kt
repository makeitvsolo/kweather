package github.makeitvsolo.kweather.user.access.api.service.user.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.service.user.dto.AccessTokenDto
import github.makeitvsolo.kweather.user.access.api.service.user.error.RefreshAccessTokenError

typealias RefreshAccessTokenPayload = AccessTokenDto
typealias RefreshAccessTokenResponse = AccessTokenDto

interface RefreshAccessToken {

    fun refresh(payload: RefreshAccessTokenPayload): Result<RefreshAccessTokenResponse, RefreshAccessTokenError>
}
