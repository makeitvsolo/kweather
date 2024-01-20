package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.base.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

sealed interface BaseLocationRepositoryConfigurationError : IntoThrowable {

    data class DataSourceUrlError internal constructor(
        private val details: String
    ) : BaseLocationRepositoryConfigurationError {
        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class InvalidCredentialsError internal constructor(
        private val details: String
    ) : BaseLocationRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class InvalidApiKeyError internal constructor(
        private val details: String
    ) : BaseLocationRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }
}
