package github.makeitvsolo.kweather.weather.api.service.location.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.service.location.dto.AccountDto
import github.makeitvsolo.kweather.weather.api.service.location.dto.FavouriteLocationDto
import github.makeitvsolo.kweather.weather.api.service.location.dto.LocationDto

typealias FetchFavouritePayload = FavouriteLocationDto
typealias FetchAllFavouritePayload = AccountDto
typealias FetchFavouriteResponse = LocationDto
typealias FetchAllFavouriteResponse = List<LocationDto>

interface MapFetchFavouriteErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FetchFavouriteError {

    fun <R, M : MapFetchFavouriteErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : FetchFavouriteError {

        override fun <R, M : MapFetchFavouriteErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)
    }

    data class InternalError(private val throwable: Throwable) : FetchFavouriteError {

        override fun <R, M : MapFetchFavouriteErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)
    }
}

interface FetchFavourite {

    fun fetchAllFavourite(payload: FetchAllFavouritePayload): Result<FetchAllFavouriteResponse, FetchFavouriteError>
    fun fetchFavourite(payload: FetchFavouritePayload): Result<FetchFavouriteResponse, FetchFavouriteError>
}
