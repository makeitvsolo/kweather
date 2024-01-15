package github.makeitvsolo.kweather.user.access.application.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.security.session.EncodeToken
import github.makeitvsolo.kweather.user.access.api.security.session.Token
import github.makeitvsolo.kweather.user.access.api.service.dto.AccessTokenDto
import github.makeitvsolo.kweather.user.access.api.service.usecase.RefreshAccessToken
import github.makeitvsolo.kweather.user.access.api.service.usecase.RefreshAccessTokenError
import github.makeitvsolo.kweather.user.access.api.service.usecase.RefreshAccessTokenPayload
import github.makeitvsolo.kweather.user.access.api.service.usecase.RefreshAccessTokenResponse

class ApplicationRefreshAccessToken(
    private val session: EncodeToken
) : RefreshAccessToken {

    override fun refresh(payload: RefreshAccessTokenPayload):
    Result<RefreshAccessTokenResponse, RefreshAccessTokenError> {
        val token = Token(payload.access, payload.refresh)

        return session.refresh(token).map { refreshed ->
            AccessTokenDto(refreshed.access, refreshed.refresh)
        }.mapError { error ->
            error.into(RefreshAccessTokenError.FromDecodeTokenError)
        }
    }
}
