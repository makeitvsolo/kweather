package github.makeitvsolo.kweather.user.access.application.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.type.Unique
import github.makeitvsolo.kweather.user.access.api.datasource.UserRepository
import github.makeitvsolo.kweather.user.access.api.security.hash.Hash
import github.makeitvsolo.kweather.user.access.api.service.usecase.RegisterUser
import github.makeitvsolo.kweather.user.access.api.service.usecase.RegisterUserError
import github.makeitvsolo.kweather.user.access.api.service.usecase.RegisterUserPayload
import github.makeitvsolo.kweather.user.access.domain.User

class ApplicationRegisterUser(
    private val repository: UserRepository,
    private val encode: Hash,
    private val userId: Unique<String>
) : RegisterUser {

    override fun register(payload: RegisterUserPayload): Result<Unit, RegisterUserError> {
        val user = User.create(userId, payload.name, encode.hash(payload.password))

        return repository.save(user).mapError { error ->
            error.into(RegisterUserError.FromSaveUserError)
        }
    }
}
