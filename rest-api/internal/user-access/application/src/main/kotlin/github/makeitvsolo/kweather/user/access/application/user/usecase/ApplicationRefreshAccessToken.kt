package github.makeitvsolo.kweather.user.access.application.user.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.security.session.EncodeToken
import github.makeitvsolo.kweather.user.access.api.service.user.dto.AccessTokenDto
import github.makeitvsolo.kweather.user.access.api.service.user.error.RefreshAccessTokenError
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RefreshAccessToken
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RefreshAccessTokenPayload
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RefreshAccessTokenResponse

class ApplicationRefreshAccessToken(
    private val session: EncodeToken
) : RefreshAccessToken {

    override fun refresh(payload: RefreshAccessTokenPayload):
    Result<RefreshAccessTokenResponse, RefreshAccessTokenError> =
        session.refresh(payload.token).map { refreshed ->
            AccessTokenDto(refreshed.access, refreshed.refresh)
        }.mapError { error ->
            error.into(RefreshAccessTokenError.FromDecodeTokenError)
        }
}
