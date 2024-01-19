package github.makeitvsolo.kweather.weather.api.datasource.weather.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.weather.exception.WeatherNotFoundException

interface MapFindWeatherErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FindWeatherError : IntoThrowable {

    fun <R, M : MapFindWeatherErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : FindWeatherError {

        override fun <R, M : MapFindWeatherErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = WeatherNotFoundException(details)
    }

    data class InternalError(private val throwable: Throwable) : FindWeatherError {

        override fun <R, M : MapFindWeatherErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
