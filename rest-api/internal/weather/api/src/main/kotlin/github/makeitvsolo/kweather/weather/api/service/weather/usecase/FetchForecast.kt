package github.makeitvsolo.kweather.weather.api.service.weather.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.service.weather.dto.FavouriteLocationDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.ForecastDto
import github.makeitvsolo.kweather.weather.api.service.weather.error.FetchForecastError

typealias FetchForecastPayload = FavouriteLocationDto
typealias FetchForecastResponse = ForecastDto

interface FetchForecast {

    fun fetchForecast(payload: FetchForecastPayload): Result<FetchForecastResponse, FetchForecastError>
}
