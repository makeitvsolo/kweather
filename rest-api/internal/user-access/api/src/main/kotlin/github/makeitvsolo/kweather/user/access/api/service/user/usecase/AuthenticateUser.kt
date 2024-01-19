package github.makeitvsolo.kweather.user.access.api.service.user.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.service.user.dto.AccessTokenDto
import github.makeitvsolo.kweather.user.access.api.service.user.dto.ActiveUserDto
import github.makeitvsolo.kweather.user.access.api.service.user.error.AuthenticateUserError

typealias AuthenticateUserPayload = AccessTokenDto
typealias AuthenticateUserResponse = ActiveUserDto

interface AuthenticateUser {

    fun authenticate(payload: AuthenticateUserPayload): Result<AuthenticateUserResponse, AuthenticateUserError>
}
