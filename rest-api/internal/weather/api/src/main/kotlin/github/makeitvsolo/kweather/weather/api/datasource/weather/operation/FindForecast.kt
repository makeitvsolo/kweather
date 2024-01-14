package github.makeitvsolo.kweather.weather.api.datasource.weather.operation

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.domain.weather.forecast.DailyWeather

import java.math.BigDecimal

interface MapFindForecastErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FindForecastError {

    fun <R, M : MapFindForecastErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : FindForecastError {

        override fun <R, M : MapFindForecastErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)
    }

    data class InternalError(private val throwable: Throwable) : FindForecastError {

        override fun <R, M : MapFindForecastErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)
    }
}

interface FindForecast {

    fun findForecastByCoordinates(
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<List<DailyWeather>, FindForecastError>
}
