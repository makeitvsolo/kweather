package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.infrastructure.configure.mongo.MongoDatasource
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.MongoForecastRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.error.MongoForecastRepositoryConfigurationError

class ConfigureMongoForecastRepository internal constructor(
    private var datasource: MongoDatasource? = null
) {

    fun datasource(datasource: MongoDatasource) = apply { this.datasource = datasource }

    fun configured(): Result<MongoForecastRepository, MongoForecastRepositoryConfigurationError> {
        val appliedDatasource = datasource

        appliedDatasource ?: return Result.error(
            MongoForecastRepositoryConfigurationError("missing mongo datasource")
        )

        return Result.ok(
            MongoForecastRepository(appliedDatasource.intoDatabase())
        )
    }

    companion object {

        fun with(): ConfigureMongoForecastRepository = ConfigureMongoForecastRepository()
    }
}
