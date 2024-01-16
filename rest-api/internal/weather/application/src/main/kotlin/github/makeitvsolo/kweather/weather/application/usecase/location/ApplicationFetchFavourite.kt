package github.makeitvsolo.kweather.weather.application.usecase.location

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.service.location.dto.LocationDto
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchAllFavouritePayload
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchAllFavouriteResponse
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavourite
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavouriteError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavouritePayload
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavouriteResponse

class ApplicationFetchFavourite(
    private val repository: LocationRepository
) : FetchFavourite {

    override fun fetchAllFavourite(payload: FetchAllFavouritePayload):
    Result<FetchAllFavouriteResponse, FetchFavouriteError> =
        repository.findAllFavourite(payload.accountId).map { locations ->
            locations.map { it.into(LocationDto.FromLocation) }
        }.mapError { error ->
            error.into(FetchFavouriteError.FromFindFavouriteError)
        }

    override fun fetchFavourite(payload: FetchFavouritePayload): Result<FetchFavouriteResponse, FetchFavouriteError> =
        repository.findFavourite(payload.accountId, payload.latitude, payload.longitude).map { location ->
            location.into(LocationDto.FromLocation)
        }.mapError { error ->
            error.into(FetchFavouriteError.FromFindFavouriteError)
        }
}
