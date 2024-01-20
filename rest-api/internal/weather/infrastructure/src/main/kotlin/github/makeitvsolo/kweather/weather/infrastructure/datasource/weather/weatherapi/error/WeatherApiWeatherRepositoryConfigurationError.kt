package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

sealed interface WeatherApiWeatherRepositoryConfigurationError : IntoThrowable {

    data class InvalidApiKeyError internal constructor(
        private val details: String
    ) : WeatherApiWeatherRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class InvalidForecastDaysError internal constructor(
        private val details: String
    ) : WeatherApiWeatherRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }
}
