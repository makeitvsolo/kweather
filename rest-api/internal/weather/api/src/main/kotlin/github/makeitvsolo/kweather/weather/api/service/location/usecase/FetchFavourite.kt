package github.makeitvsolo.kweather.weather.api.service.location.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.service.location.dto.AccountDto
import github.makeitvsolo.kweather.weather.api.service.location.dto.FavouriteLocationDto
import github.makeitvsolo.kweather.weather.api.service.location.dto.LocationDto
import github.makeitvsolo.kweather.weather.api.service.location.error.FetchFavouriteError

typealias FetchFavouritePayload = FavouriteLocationDto
typealias FetchAllFavouritePayload = AccountDto
typealias FetchFavouriteResponse = LocationDto
typealias FetchAllFavouriteResponse = List<LocationDto>

interface FetchFavourite {

    fun fetchAllFavourite(payload: FetchAllFavouritePayload): Result<FetchAllFavouriteResponse, FetchFavouriteError>
    fun fetchFavourite(payload: FetchFavouritePayload): Result<FetchFavouriteResponse, FetchFavouriteError>
}
