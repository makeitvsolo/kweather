package github.makeitvsolo.kweather.boot.api.controller.weather.response

import github.makeitvsolo.kweather.boot.api.message.ErrorMessage
import github.makeitvsolo.kweather.boot.api.message.OkMessage
import github.makeitvsolo.kweather.weather.api.service.location.error.MapSaveToFavouritesErrorInto
import github.makeitvsolo.kweather.weather.api.service.location.error.SaveToFavouritesError

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object AddToFavouritesResponse : MapSaveToFavouritesErrorInto<ResponseEntity<*>> {

    fun fromOk(): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.OK)
            .body(OkMessage.from(HttpStatus.CREATED))

    fun fromError(error: SaveToFavouritesError): ResponseEntity<*> =
        error.into(this)

    override fun fromNotFoundError(details: String): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage.from(HttpStatus.NOT_FOUND, details))

    override fun fromConflictError(details: String): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage.from(HttpStatus.CONFLICT, details))

    override fun fromInternalError(throwable: Throwable): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build<Any>()
}
