package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.WeatherApiWeatherRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.error.WeatherApiWeatherRepositoryConfigurationError

class ConfigureWeatherApiWeatherRepository internal constructor(
    private var apiKey: String? = null,
    private var forecastDays: Int? = null
) {

    fun apiKey(key: String) = apply { this.apiKey = key }
    fun forecastDays(days: Int) = apply { this.forecastDays = days }

    fun configured(): Result<WeatherApiWeatherRepository, WeatherApiWeatherRepositoryConfigurationError> {
        val appliedApiKey = apiKey
        val appliedForecastDays = forecastDays

        appliedApiKey ?: return Result.error(
            WeatherApiWeatherRepositoryConfigurationError.InvalidApiKeyError("missing api key")
        )

        appliedForecastDays ?: return Result.error(
            WeatherApiWeatherRepositoryConfigurationError.InvalidForecastDaysError("missing forecast days")
        )

        if (appliedForecastDays <= 0) {
            return Result.error(
                WeatherApiWeatherRepositoryConfigurationError.InvalidForecastDaysError("days must be `>0`")
            )
        }

        return Result.ok(
            WeatherApiWeatherRepository(appliedApiKey, appliedForecastDays)
        )
    }

    companion object {

        fun with(): ConfigureWeatherApiWeatherRepository = ConfigureWeatherApiWeatherRepository()
    }
}
