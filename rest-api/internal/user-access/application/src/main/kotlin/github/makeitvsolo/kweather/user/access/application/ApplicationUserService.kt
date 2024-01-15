package github.makeitvsolo.kweather.user.access.application

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.service.UserService
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthenticateUser
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthenticateUserError
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthenticateUserPayload
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthenticateUserResponse
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthorizeUser
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthorizeUserError
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthorizeUserPayload
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthorizeUserResponse
import github.makeitvsolo.kweather.user.access.api.service.usecase.RefreshAccessToken
import github.makeitvsolo.kweather.user.access.api.service.usecase.RefreshAccessTokenError
import github.makeitvsolo.kweather.user.access.api.service.usecase.RefreshAccessTokenPayload
import github.makeitvsolo.kweather.user.access.api.service.usecase.RefreshAccessTokenResponse
import github.makeitvsolo.kweather.user.access.api.service.usecase.RegisterUser
import github.makeitvsolo.kweather.user.access.api.service.usecase.RegisterUserError
import github.makeitvsolo.kweather.user.access.api.service.usecase.RegisterUserPayload

class ApplicationUserService(
    private val registerUsecase: RegisterUser,
    private val authorizeUsecase: AuthorizeUser,
    private val authenticateUsecase: AuthenticateUser,
    private val refreshTokenUsecase: RefreshAccessToken
) : UserService {

    override fun register(payload: RegisterUserPayload): Result<Unit, RegisterUserError> =
        registerUsecase.register(payload)

    override fun authorize(payload: AuthorizeUserPayload): Result<AuthorizeUserResponse, AuthorizeUserError> =
        authorizeUsecase.authorize(payload)

    override fun authenticate(payload: AuthenticateUserPayload):
    Result<AuthenticateUserResponse, AuthenticateUserError> =
        authenticateUsecase.authenticate(payload)

    override fun refresh(payload: RefreshAccessTokenPayload):
    Result<RefreshAccessTokenResponse, RefreshAccessTokenError> =
        refreshTokenUsecase.refresh(payload)
}
