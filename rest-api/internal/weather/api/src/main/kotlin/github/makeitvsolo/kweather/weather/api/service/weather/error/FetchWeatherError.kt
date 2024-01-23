package github.makeitvsolo.kweather.weather.api.service.weather.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.location.error.MapFindFavouriteErrorInto
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.MapFindWeatherErrorInto
import github.makeitvsolo.kweather.weather.api.service.weather.exception.WeatherServiceException

interface MapFetchWeatherErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FetchWeatherError : IntoThrowable {

    object FromFindFavouriteError : MapFindFavouriteErrorInto<FetchWeatherError> {

        override fun fromNotFoundError(details: String): FetchWeatherError =
            NotFoundError(details)

        override fun fromInternalError(throwable: Throwable): FetchWeatherError =
            InternalError(throwable)
    }

    object FromFindWeatherError : MapFindWeatherErrorInto<FetchWeatherError> {

        override fun fromNotFoundError(details: String): FetchWeatherError =
            NotFoundError(details)

        override fun fromInternalError(throwable: Throwable): FetchWeatherError =
            InternalError(throwable)
    }

    fun <R, M : MapFetchWeatherErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : FetchWeatherError {

        override fun <R, M : MapFetchWeatherErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = WeatherServiceException(details)
    }

    data class InternalError(private val throwable: Throwable) : FetchWeatherError {

        override fun <R, M : MapFetchWeatherErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
