package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

data class SqlLocationRepositoryConfigurationError internal constructor(
    private val details: String
) : IntoThrowable {

    override fun intoThrowable(): Throwable = IllegalArgumentException(details)
}
