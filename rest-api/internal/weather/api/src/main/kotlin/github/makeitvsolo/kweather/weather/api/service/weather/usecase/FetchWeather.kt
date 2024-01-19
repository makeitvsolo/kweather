package github.makeitvsolo.kweather.weather.api.service.weather.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.service.weather.dto.CurrentWeatherDto
import github.makeitvsolo.kweather.weather.api.service.weather.dto.FavouriteLocationDto
import github.makeitvsolo.kweather.weather.api.service.weather.error.FetchWeatherError

typealias FetchWeatherPayload = FavouriteLocationDto
typealias FetchWeatherResponse = CurrentWeatherDto

interface FetchWeather {

    fun fetchWeather(payload: FetchWeatherPayload): Result<FetchWeatherResponse, FetchWeatherError>
}
