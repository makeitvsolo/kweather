package github.makeitvsolo.kweather.weather.api.service.weather.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.MapFindFavouriteErrorInto
import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.MapFindWeatherErrorInto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.CurrentWeatherDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.FavouriteLocationDto

typealias FetchWeatherPayload = FavouriteLocationDto
typealias FetchWeatherResponse = CurrentWeatherDto

interface MapFetchWeatherErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FetchWeatherError {

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
    }

    data class InternalError(private val throwable: Throwable) : FetchWeatherError {

        override fun <R, M : MapFetchWeatherErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)
    }
}

interface FetchWeather {

    fun fetchWeather(payload: FetchWeatherPayload): Result<FetchWeatherResponse, FetchWeatherError>
}
