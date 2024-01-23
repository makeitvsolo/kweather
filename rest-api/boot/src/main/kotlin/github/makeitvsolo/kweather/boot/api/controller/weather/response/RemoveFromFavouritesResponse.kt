package github.makeitvsolo.kweather.boot.api.controller.weather.response

import github.makeitvsolo.kweather.boot.api.message.ErrorMessage
import github.makeitvsolo.kweather.boot.api.message.OkMessage
import github.makeitvsolo.kweather.weather.api.service.location.error.MapRemoveFromFavouritesErrorInto
import github.makeitvsolo.kweather.weather.api.service.location.error.RemoveFromFavouritesError

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object RemoveFromFavouritesResponse : MapRemoveFromFavouritesErrorInto<ResponseEntity<*>> {

    fun fromOk(): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.OK)
            .body(OkMessage.from(HttpStatus.NO_CONTENT))

    fun fromError(error: RemoveFromFavouritesError): ResponseEntity<*> =
        error.into(this)

    override fun fromNotFoundError(details: String): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage.from(HttpStatus.NOT_FOUND, details))

    override fun fromInternalError(throwable: Throwable): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build<Any>()
}
