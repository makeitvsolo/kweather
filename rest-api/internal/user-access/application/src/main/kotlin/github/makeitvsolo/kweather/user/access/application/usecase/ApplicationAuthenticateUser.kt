package github.makeitvsolo.kweather.user.access.application.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.security.session.EncodeToken
import github.makeitvsolo.kweather.user.access.api.security.session.Token
import github.makeitvsolo.kweather.user.access.api.service.dto.ActiveUserDto
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthenticateUser
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthenticateUserError
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthenticateUserPayload
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthenticateUserResponse

class ApplicationAuthenticateUser(
    private val session: EncodeToken
) : AuthenticateUser {

    override fun authenticate(payload: AuthenticateUserPayload):
    Result<AuthenticateUserResponse, AuthenticateUserError> {
        val token = Token(payload.access, payload.refresh)

        return session.decode(token).map { tokenPayload ->
            ActiveUserDto(tokenPayload.userId, tokenPayload.userName)
        }.mapError { error ->
            error.into(AuthenticateUserError.FromDecodeTokenError)
        }
    }
}
