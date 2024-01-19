package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

data class SaveForecastError internal constructor(private val throwable: Throwable) : IntoThrowable {

    override fun intoThrowable(): Throwable = throwable
}
