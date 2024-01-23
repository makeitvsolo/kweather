package github.makeitvsolo.kweather.boot.api.controller.user.access.response

import github.makeitvsolo.kweather.boot.api.message.ErrorMessage
import github.makeitvsolo.kweather.boot.api.message.OkMessage
import github.makeitvsolo.kweather.user.access.api.service.user.error.AuthorizeUserError
import github.makeitvsolo.kweather.user.access.api.service.user.error.MapAuthorizeUserErrorInto
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthorizeUserResponse

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object AuthorizeResponse : MapAuthorizeUserErrorInto<ResponseEntity<*>> {

    fun fromOk(response: AuthorizeUserResponse): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.OK)
            .body(OkMessage.from(HttpStatus.OK, response))

    fun fromError(error: AuthorizeUserError): ResponseEntity<*> =
        error.into(this)

    override fun fromNotFoundError(details: String): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage.from(HttpStatus.NOT_FOUND, details))

    override fun fromInvalidCredentialsError(details: String): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage.from(HttpStatus.UNAUTHORIZED, details))

    override fun fromInternalError(throwable: Throwable): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build<Any>()
}
