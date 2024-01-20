package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

sealed interface MongoForecastRepositoryConfigurationError : IntoThrowable {

    data class MongoUrlError internal constructor(
        private val details: String
    ) : MongoForecastRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class MongoDatabaseError internal constructor(
        private val details: String
    ) : MongoForecastRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class InvalidCredentialsError internal constructor(
        private val details: String
    ) : MongoForecastRepositoryConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }
}
