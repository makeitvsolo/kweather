package github.makeitvsolo.kweather.weather.usecase.location

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.service.location.dto.LocationDto
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchForLocation
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchForLocationError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchForLocationPayload
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchForLocationResponse

class ApplicationSearchForLocation(
    private val repository: LocationRepository
) : SearchForLocation {

    override fun search(payload: SearchForLocationPayload): Result<SearchForLocationResponse, SearchForLocationError> =
        repository.searchByName(payload.accountId, payload.locationName).map { locations ->
            locations.map { it.into(LocationDto.FromLocation) }
        }.mapError { error ->
            error.into(SearchForLocationError.FromSearchLocation)
        }
}
