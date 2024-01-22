package github.makeitvsolo.kweather.user.access.application.user.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.security.session.EncodeToken
import github.makeitvsolo.kweather.user.access.api.service.user.dto.ActiveUserDto
import github.makeitvsolo.kweather.user.access.api.service.user.error.AuthenticateUserError
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthenticateUser
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthenticateUserPayload
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthenticateUserResponse

class ApplicationAuthenticateUser(
    private val session: EncodeToken
) : AuthenticateUser {

    override fun authenticate(payload: AuthenticateUserPayload):
    Result<AuthenticateUserResponse, AuthenticateUserError> =
        session.decode(payload.token).map { tokenPayload ->
            ActiveUserDto(tokenPayload.userId, tokenPayload.userName)
        }.mapError { error ->
            error.into(AuthenticateUserError.FromDecodeTokenError)
        }
}
