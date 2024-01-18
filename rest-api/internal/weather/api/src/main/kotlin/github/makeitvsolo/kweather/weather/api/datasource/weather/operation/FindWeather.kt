package github.makeitvsolo.kweather.weather.api.datasource.weather.operation

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.domain.weather.current.Weather

import java.math.BigDecimal

interface MapFindWeatherErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FindWeatherError : IntoThrowable {

    fun <R, M : MapFindWeatherErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : FindWeatherError {

        override fun <R, M : MapFindWeatherErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = Throwable(details)
    }

    data class InternalError(private val throwable: Throwable) : FindWeatherError {

        override fun <R, M : MapFindWeatherErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}

interface FindWeather {

    fun findByCoordinates(
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<Weather, FindWeatherError>
}
