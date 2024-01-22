package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

data class SqlAccountRepositoryConfigurationError internal constructor(
    private val details: String
) : IntoThrowable {

    override fun intoThrowable(): Throwable = IllegalArgumentException(details)
}
