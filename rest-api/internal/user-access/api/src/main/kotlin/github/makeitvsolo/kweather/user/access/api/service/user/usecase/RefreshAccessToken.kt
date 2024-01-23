package github.makeitvsolo.kweather.user.access.api.service.user.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.service.user.dto.AccessTokenDto
import github.makeitvsolo.kweather.user.access.api.service.user.dto.TokenDto
import github.makeitvsolo.kweather.user.access.api.service.user.error.RefreshAccessTokenError

typealias RefreshAccessTokenPayload = TokenDto
typealias RefreshAccessTokenResponse = AccessTokenDto

interface RefreshAccessToken {

    fun refresh(payload: RefreshAccessTokenPayload): Result<RefreshAccessTokenResponse, RefreshAccessTokenError>
}
