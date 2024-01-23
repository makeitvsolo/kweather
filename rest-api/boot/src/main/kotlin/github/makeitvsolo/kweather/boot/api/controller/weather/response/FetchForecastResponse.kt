package github.makeitvsolo.kweather.boot.api.controller.weather.response

import github.makeitvsolo.kweather.boot.api.message.ErrorMessage
import github.makeitvsolo.kweather.boot.api.message.OkMessage
import github.makeitvsolo.kweather.weather.api.service.weather.error.FetchForecastError
import github.makeitvsolo.kweather.weather.api.service.weather.error.MapFetchForecastErrorInto
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecastResponse

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object FetchForecastResponse : MapFetchForecastErrorInto<ResponseEntity<*>> {

    fun fromOk(response: FetchForecastResponse): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.OK)
            .body(OkMessage.from(HttpStatus.OK, response))

    fun fromError(error: FetchForecastError): ResponseEntity<*> =
        error.into(this)

    override fun fromNotFoundError(details: String): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage.from(HttpStatus.NOT_FOUND, details))

    override fun fromInternalError(throwable: Throwable): ResponseEntity<*> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build<Any>()
}
