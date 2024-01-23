package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.cache.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

sealed interface CachedWeatherRepositoryConfigurationError : IntoThrowable {

    data class WeatherApiRepositoryError internal constructor(
        private val details: String
    ) : CachedWeatherRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class MongoRepositoryError internal constructor(
        private val details: String
    ) : CachedWeatherRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }
}
