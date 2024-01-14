package github.makeitvsolo.kweather.weather.api.service.weather.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.MapFindFavouriteErrorInto
import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.MapFindForecastErrorInto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.FavouriteLocationDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.ForecastDto

typealias FetchForecastPayload = FavouriteLocationDto
typealias FetchForecastResponse = ForecastDto

interface MapFetchForecastErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FetchForecastError {

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
    }

    data class InternalError(private val throwable: Throwable) : FetchForecastError {

        override fun <R, M : MapFetchForecastErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)
    }
}

interface FetchForecast {

    fun fetchForecast(payload: FetchForecastPayload): Result<FetchForecastResponse, FetchForecastError>
}