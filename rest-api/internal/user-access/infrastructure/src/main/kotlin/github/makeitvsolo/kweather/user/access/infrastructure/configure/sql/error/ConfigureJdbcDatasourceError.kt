package github.makeitvsolo.kweather.user.access.infrastructure.configure.sql.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

sealed interface ConfigureJdbcDatasourceError : IntoThrowable {

    data class DataSourceUrlError internal constructor(
        private val details: String
    ) : ConfigureJdbcDatasourceError {
        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class InvalidCredentialsError internal constructor(
        private val details: String
    ) : ConfigureJdbcDatasourceError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }
}
