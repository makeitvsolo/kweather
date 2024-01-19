package github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

sealed interface SqlUserRepositoryConfigurationError : IntoThrowable {

    data class DataSourceUrlError internal constructor(
        private val details: String
    ) : SqlUserRepositoryConfigurationError {
        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class InvalidCredentialsError internal constructor(
        private val details: String
    ) : SqlUserRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }
}
