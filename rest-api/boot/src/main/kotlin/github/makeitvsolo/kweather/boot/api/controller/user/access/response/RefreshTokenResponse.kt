package github.makeitvsolo.kweather.boot.api.controller.user.access.response

import github.makeitvsolo.kweather.boot.api.message.ErrorMessage
import github.makeitvsolo.kweather.boot.api.message.OkMessage
import github.makeitvsolo.kweather.user.access.api.service.user.error.MapRefreshAccessTokenErrorInto
import github.makeitvsolo.kweather.user.access.api.service.user.error.RefreshAccessTokenError
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RefreshAccessTokenResponse

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object RefreshTokenResponse : MapRefreshAccessTokenErrorInto<ResponseEntity<*>> {

    fun fromOk(response: RefreshAccessTokenResponse): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.OK)
            .body(OkMessage.from(HttpStatus.OK, response))

    fun fromError(error: RefreshAccessTokenError): ResponseEntity<*> =
        error.into(this)

    override fun fromInvalidTokenError(details: String): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage.from(HttpStatus.UNAUTHORIZED, details))

    override fun fromInternalError(throwable: Throwable): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build<Any>()
}
