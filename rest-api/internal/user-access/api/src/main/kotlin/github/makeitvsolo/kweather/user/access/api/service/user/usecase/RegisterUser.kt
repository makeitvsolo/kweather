package github.makeitvsolo.kweather.user.access.api.service.user.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.service.user.dto.UserDto
import github.makeitvsolo.kweather.user.access.api.service.user.error.RegisterUserError

typealias RegisterUserPayload = UserDto

interface RegisterUser {

    fun register(payload: RegisterUserPayload): Result<Unit, RegisterUserError>
}
