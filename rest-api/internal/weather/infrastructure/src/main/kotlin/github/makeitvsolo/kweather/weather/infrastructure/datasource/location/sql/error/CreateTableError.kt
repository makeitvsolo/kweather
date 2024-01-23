package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

data class CreateTableError internal constructor(private val throwable: Throwable) : IntoThrowable {

    override fun intoThrowable(): Throwable = throwable
}
