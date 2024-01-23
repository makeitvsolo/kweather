package github.makeitvsolo.kweather.boot.api.controller.weather.response

import github.makeitvsolo.kweather.boot.api.message.ErrorMessage
import github.makeitvsolo.kweather.boot.api.message.OkMessage
import github.makeitvsolo.kweather.weather.api.service.location.error.FetchFavouriteError
import github.makeitvsolo.kweather.weather.api.service.location.error.MapFetchFavouriteErrorInto
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchAllFavouriteResponse

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object FetchLocationsResponse : MapFetchFavouriteErrorInto<ResponseEntity<*>> {

    fun fromOk(response: FetchAllFavouriteResponse): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.OK)
            .body(OkMessage.from(HttpStatus.OK, response))

    fun fromError(error: FetchFavouriteError): ResponseEntity<*> =
        error.into(this)

    override fun fromNotFoundError(details: String): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage.from(HttpStatus.NOT_FOUND, details))

    override fun fromInternalError(throwable: Throwable): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build<Any>()
}
