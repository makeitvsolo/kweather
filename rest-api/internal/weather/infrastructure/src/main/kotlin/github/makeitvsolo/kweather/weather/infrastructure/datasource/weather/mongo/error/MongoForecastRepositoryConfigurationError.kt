package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

data class MongoForecastRepositoryConfigurationError internal constructor(
    private val details: String
) : IntoThrowable {

    override fun intoThrowable(): Throwable = IllegalArgumentException(details)
}
