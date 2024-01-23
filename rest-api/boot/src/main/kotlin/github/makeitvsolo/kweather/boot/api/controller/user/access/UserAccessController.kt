package github.makeitvsolo.kweather.boot.api.controller.user.access

import github.makeitvsolo.kweather.boot.api.controller.user.access.request.RefreshTokenRequest
import github.makeitvsolo.kweather.boot.api.controller.user.access.request.UserCredentialsRequest
import github.makeitvsolo.kweather.boot.api.controller.user.access.response.AuthorizeResponse
import github.makeitvsolo.kweather.boot.api.controller.user.access.response.RefreshTokenResponse
import github.makeitvsolo.kweather.boot.api.controller.user.access.response.RegisterResponse
import github.makeitvsolo.kweather.user.access.api.service.user.UserService
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthorizeUserPayload
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RefreshAccessTokenPayload
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RegisterUserPayload

import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/user-access")
open class UserAccessController(
    private val service: UserService
) {

    @PostMapping(value = ["/sign-up"], consumes = ["application/json"])
    open fun signUp(@Valid @RequestBody credentials: UserCredentialsRequest): ResponseEntity<*> {
        log.info("trying to register ${credentials.name}...")
        val payload = RegisterUserPayload(credentials.name, credentials.password)

        return service.register(payload).map {
            log.info("${credentials.name} registered successfully")
            RegisterResponse.fromOk()
        }.unwrapOrElse { error ->
            log.info("${credentials.name} registration error: $error")
            RegisterResponse.fromError(error)
        }
    }

    @PostMapping(value = ["/sign-in"], consumes = ["application/json"])
    open fun signIn(@Valid @RequestBody credentials: UserCredentialsRequest): ResponseEntity<*> {
        log.info("trying to authorize ${credentials.name}...")
        val payload = AuthorizeUserPayload(credentials.name, credentials.password)

        return service.authorize(payload).map { response ->
            log.info("${credentials.name} authorized successfully")
            AuthorizeResponse.fromOk(response)
        }.unwrapOrElse { error ->
            log.info("${credentials.name} authorize error: $error")
            AuthorizeResponse.fromError(error)
        }
    }

    @PostMapping(value = ["/refresh-token"], consumes = ["application/json"])
    open fun refreshToken(@Valid @RequestBody refresh: RefreshTokenRequest): ResponseEntity<*> {
        log.info("trying to refresh token...")
        val payload = RefreshAccessTokenPayload(refresh.token)

        return service.refresh(payload).map { response ->
            log.info("token refreshed successfully")
            RefreshTokenResponse.fromOk(response)
        }.unwrapOrElse { error ->
            log.info("token refresh error: $error")
            RefreshTokenResponse.fromError(error)
        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(UserAccessController::class.java)
    }
}
