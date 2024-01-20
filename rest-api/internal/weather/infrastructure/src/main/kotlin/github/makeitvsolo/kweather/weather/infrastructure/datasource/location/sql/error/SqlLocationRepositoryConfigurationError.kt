package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

sealed interface SqlLocationRepositoryConfigurationError : IntoThrowable {

    data class DataSourceUrlError internal constructor(
        private val details: String
    ) : SqlLocationRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class InvalidCredentialsError internal constructor(
        private val details: String
    ) : SqlLocationRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }
}
