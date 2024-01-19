package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

data class TruncateTableError internal constructor(private val throwable: Throwable) : IntoThrowable {

    override fun intoThrowable(): Throwable = throwable
}
