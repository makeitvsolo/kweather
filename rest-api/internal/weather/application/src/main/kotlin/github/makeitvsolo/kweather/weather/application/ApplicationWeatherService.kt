package github.makeitvsolo.kweather.weather.application

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.service.weather.WeatherService
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecast
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecastError
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecastPayload
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchForecastResponse
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeather
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeatherError
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeatherPayload
import github.makeitvsolo.kweather.weather.api.service.weather.usecase.FetchWeatherResponse

class ApplicationWeatherService(
    private val fetchWeatherUsecase: FetchWeather,
    private val fetchForecastUsecase: FetchForecast
) : WeatherService {

    override fun fetchWeather(payload: FetchWeatherPayload): Result<FetchWeatherResponse, FetchWeatherError> =
        fetchWeatherUsecase.fetchWeather(payload)

    override fun fetchForecast(payload: FetchForecastPayload): Result<FetchForecastResponse, FetchForecastError> =
        fetchForecastUsecase.fetchForecast(payload)
}
