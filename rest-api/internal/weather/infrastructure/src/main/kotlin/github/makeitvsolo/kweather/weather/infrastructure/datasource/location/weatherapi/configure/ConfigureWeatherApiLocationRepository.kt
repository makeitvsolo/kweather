package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.weatherapi.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.weatherapi.WeatherApiLocationRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.weatherapi.error.WeatherApiLocationRepositoryConfigurationError

class ConfigureWeatherApiLocationRepository internal constructor(
    private var apiKey: String? = null
) {

    fun apiKey(key: String) = apply { this.apiKey = key }

    fun configured(): Result<WeatherApiLocationRepository, WeatherApiLocationRepositoryConfigurationError> {
        val appliedApiKey = apiKey

        appliedApiKey ?: return Result.error(
            WeatherApiLocationRepositoryConfigurationError("missing api key")
        )

        return Result.ok(
            WeatherApiLocationRepository(appliedApiKey)
        )
    }

    companion object {

        fun with(): ConfigureWeatherApiLocationRepository = ConfigureWeatherApiLocationRepository()
    }
}
