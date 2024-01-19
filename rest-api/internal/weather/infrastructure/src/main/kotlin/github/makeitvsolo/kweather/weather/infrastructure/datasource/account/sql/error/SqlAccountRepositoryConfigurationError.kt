package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

sealed interface SqlAccountRepositoryConfigurationError : IntoThrowable {

    data class DataSourceUrlError internal constructor(
        private val details: String
    ) : SqlAccountRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class InvalidCredentialsError internal constructor(
        private val details: String
    ) : SqlAccountRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }
}
