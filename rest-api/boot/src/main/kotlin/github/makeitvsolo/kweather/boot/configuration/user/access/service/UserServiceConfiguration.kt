package github.makeitvsolo.kweather.boot.configuration.user.access.service

import github.makeitvsolo.kweather.core.type.Unique
import github.makeitvsolo.kweather.user.access.api.datasource.user.UserRepository
import github.makeitvsolo.kweather.user.access.api.security.hash.Hash
import github.makeitvsolo.kweather.user.access.api.security.session.EncodeToken
import github.makeitvsolo.kweather.user.access.api.service.user.UserService
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthenticateUser
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthorizeUser
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RefreshAccessToken
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RegisterUser
import github.makeitvsolo.kweather.user.access.application.user.ApplicationUserService
import github.makeitvsolo.kweather.user.access.application.user.usecase.ApplicationAuthenticateUser
import github.makeitvsolo.kweather.user.access.application.user.usecase.ApplicationAuthorizeUser
import github.makeitvsolo.kweather.user.access.application.user.usecase.ApplicationRefreshAccessToken
import github.makeitvsolo.kweather.user.access.application.user.usecase.ApplicationRegisterUser

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class UserServiceConfiguration {

    @Bean
    fun authenticateUsecase(
        session: EncodeToken
    ): AuthenticateUser =
        ApplicationAuthenticateUser(session)

    @Bean
    fun authorizeUsecase(
        repository: UserRepository,
        encode: Hash,
        session: EncodeToken
    ): AuthorizeUser =
        ApplicationAuthorizeUser(repository, encode, session)

    @Bean
    fun refreshTokenUsecase(
        session: EncodeToken
    ): RefreshAccessToken =
        ApplicationRefreshAccessToken(session)

    @Bean
    fun registerUsecase(
        repository: UserRepository,
        encode: Hash,
        userId: Unique<String>
    ): RegisterUser =
        ApplicationRegisterUser(repository, encode, userId)

    @Bean
    fun userService(
        authenticateUsecase: AuthenticateUser,
        authorizeUsecase: AuthorizeUser,
        refreshTokenUsecase: RefreshAccessToken,
        registerUsecase: RegisterUser
    ): UserService =
        ApplicationUserService(
            registerUsecase,
            authorizeUsecase,
            authenticateUsecase,
            refreshTokenUsecase
        )
}
