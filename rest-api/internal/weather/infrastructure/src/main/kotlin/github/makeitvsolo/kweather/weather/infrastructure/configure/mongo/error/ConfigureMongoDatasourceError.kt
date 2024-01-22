package github.makeitvsolo.kweather.weather.infrastructure.configure.mongo.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

sealed interface ConfigureMongoDatasourceError : IntoThrowable {

    data class MongoUrlError internal constructor(
        private val details: String
    ) : ConfigureMongoDatasourceError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class MongoDatabaseError internal constructor(
        private val details: String
    ) : ConfigureMongoDatasourceError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class InvalidCredentialsError internal constructor(
        private val details: String
    ) : ConfigureMongoDatasourceError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }
}
