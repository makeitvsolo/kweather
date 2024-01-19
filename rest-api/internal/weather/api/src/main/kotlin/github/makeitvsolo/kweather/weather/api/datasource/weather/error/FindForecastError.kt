package github.makeitvsolo.kweather.weather.api.datasource.weather.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.weather.exception.ForecastNotFoundException

interface MapFindForecastErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FindForecastError : IntoThrowable {

    fun <R, M : MapFindForecastErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : FindForecastError {

        override fun <R, M : MapFindForecastErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = ForecastNotFoundException(details)
    }

    data class InternalError(private val throwable: Throwable) : FindForecastError {

        override fun <R, M : MapFindForecastErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
