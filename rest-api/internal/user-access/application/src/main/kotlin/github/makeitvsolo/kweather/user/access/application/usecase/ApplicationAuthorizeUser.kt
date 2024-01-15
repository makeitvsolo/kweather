package github.makeitvsolo.kweather.user.access.application.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.datasource.UserRepository
import github.makeitvsolo.kweather.user.access.api.security.hash.Hash
import github.makeitvsolo.kweather.user.access.api.security.session.EncodeToken
import github.makeitvsolo.kweather.user.access.api.service.dto.AccessTokenDto
import github.makeitvsolo.kweather.user.access.api.service.dto.ActiveUserDto
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthorizeUser
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthorizeUserError
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthorizeUserPayload
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthorizeUserResponse

class ApplicationAuthorizeUser(
    private val repository: UserRepository,
    private val encode: Hash,
    private val session: EncodeToken
) : AuthorizeUser {

    override fun authorize(payload: AuthorizeUserPayload): Result<AuthorizeUserResponse, AuthorizeUserError> =
        repository.findByName(payload.name).mapError { error ->
            error.into(AuthorizeUserError.FromFindUserError)
        }.andThen { user ->
            if (!user.isValidCredentials(payload.name, encode.hash(payload.password))) {
                return@andThen Result.error(AuthorizeUserError.InvalidCredentialsError())
            }

            val token = session.encode(user)

            return@andThen Result.ok(
                AuthorizeUserResponse(
                    AccessTokenDto(token.access, token.refresh),
                    user.into(ActiveUserDto.FromUser)
                )
            )
        }
}
