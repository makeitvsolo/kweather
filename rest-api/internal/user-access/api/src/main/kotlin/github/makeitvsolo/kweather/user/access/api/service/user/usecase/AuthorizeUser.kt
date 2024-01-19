package github.makeitvsolo.kweather.user.access.api.service.user.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.service.user.dto.AuthorizedUserDto
import github.makeitvsolo.kweather.user.access.api.service.user.dto.UserDto
import github.makeitvsolo.kweather.user.access.api.service.user.error.AuthorizeUserError

typealias AuthorizeUserPayload = UserDto
typealias AuthorizeUserResponse = AuthorizedUserDto

interface AuthorizeUser {

    fun authorize(payload: AuthorizeUserPayload): Result<AuthorizeUserResponse, AuthorizeUserError>
}
