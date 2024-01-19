package github.makeitvsolo.kweather.weather.api.service.weather.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.location.error.MapFindFavouriteErrorInto
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.MapFindForecastErrorInto
import github.makeitvsolo.kweather.weather.api.service.weather.exception.WeatherServiceException

interface MapFetchForecastErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FetchForecastError : IntoThrowable {

    object FromFindFavouriteError : MapFindFavouriteErrorInto<FetchForecastError> {

        override fun fromNotFoundError(details: String): FetchForecastError =
            NotFoundError(details)

        override fun fromInternalError(throwable: Throwable): FetchForecastError =
            InternalError(throwable)
    }

    object FromFindForecastError : MapFindForecastErrorInto<FetchForecastError> {

        override fun fromNotFoundError(details: String): FetchForecastError =
            NotFoundError(details)

        override fun fromInternalError(throwable: Throwable): FetchForecastError =
            InternalError(throwable)
    }

    fun <R, M : MapFetchForecastErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : FetchForecastError {

        override fun <R, M : MapFetchForecastErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = WeatherServiceException(details)
    }

    data class InternalError(private val throwable: Throwable) : FetchForecastError {

        override fun <R, M : MapFetchForecastErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
