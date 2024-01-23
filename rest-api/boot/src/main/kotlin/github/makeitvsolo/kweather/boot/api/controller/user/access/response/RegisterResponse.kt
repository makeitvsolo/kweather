package github.makeitvsolo.kweather.boot.api.controller.user.access.response

import github.makeitvsolo.kweather.boot.api.message.ErrorMessage
import github.makeitvsolo.kweather.boot.api.message.OkMessage
import github.makeitvsolo.kweather.user.access.api.service.user.error.MapRegisterUserErrorInto
import github.makeitvsolo.kweather.user.access.api.service.user.error.RegisterUserError

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object RegisterResponse : MapRegisterUserErrorInto<ResponseEntity<*>> {

    fun fromOk(): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.OK)
            .body(OkMessage.from(HttpStatus.CREATED))

    fun fromError(error: RegisterUserError): ResponseEntity<*> =
        error.into(this)

    override fun fromConflictError(details: String): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage.from(HttpStatus.CONFLICT, details))

    override fun fromInternalError(throwable: Throwable): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build<Any>()
}
