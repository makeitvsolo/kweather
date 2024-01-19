package github.makeitvsolo.kweather.weather.api.datasource.location.operation

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.error.SearchLocationError
import github.makeitvsolo.kweather.weather.domain.location.Location

interface SearchLocation {

    fun searchByName(accountId: String, name: String): Result<List<Location>, SearchLocationError>
}
