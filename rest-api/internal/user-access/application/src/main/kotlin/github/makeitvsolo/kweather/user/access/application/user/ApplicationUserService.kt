package github.makeitvsolo.kweather.user.access.application.user

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.service.user.UserService
import github.makeitvsolo.kweather.user.access.api.service.user.error.AuthenticateUserError
import github.makeitvsolo.kweather.user.access.api.service.user.error.AuthorizeUserError
import github.makeitvsolo.kweather.user.access.api.service.user.error.RefreshAccessTokenError
import github.makeitvsolo.kweather.user.access.api.service.user.error.RegisterUserError
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthenticateUser
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthenticateUserPayload
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthenticateUserResponse
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthorizeUser
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthorizeUserPayload
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthorizeUserResponse
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RefreshAccessToken
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RefreshAccessTokenPayload
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RefreshAccessTokenResponse
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RegisterUser
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RegisterUserPayload

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
