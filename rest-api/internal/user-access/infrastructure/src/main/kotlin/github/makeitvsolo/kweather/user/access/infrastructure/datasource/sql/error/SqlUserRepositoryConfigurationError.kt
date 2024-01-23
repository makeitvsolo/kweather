package github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

data class SqlUserRepositoryConfigurationError internal constructor(
    private val details: String
) : IntoThrowable {

    override fun intoThrowable(): Throwable = IllegalArgumentException(details)
}
