package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.base.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

sealed interface BaseLocationRepositoryConfigurationError : IntoThrowable {

    data class WeatherApiRepositoryError internal constructor(
        private val details: String
    ) : BaseLocationRepositoryConfigurationError {
        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class SqlRepositoryError internal constructor(
        private val details: String
    ) : BaseLocationRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }
}
