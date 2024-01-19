package github.makeitvsolo.kweather.weather.api.service.location.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.service.location.dto.LocationDto
import github.makeitvsolo.kweather.weather.api.service.location.dto.SearchLocationDto
import github.makeitvsolo.kweather.weather.api.service.location.error.SearchForLocationError

typealias SearchForLocationPayload = SearchLocationDto
typealias SearchForLocationResponse = List<LocationDto>

interface SearchForLocation {

    fun search(payload: SearchForLocationPayload): Result<SearchForLocationResponse, SearchForLocationError>
}
