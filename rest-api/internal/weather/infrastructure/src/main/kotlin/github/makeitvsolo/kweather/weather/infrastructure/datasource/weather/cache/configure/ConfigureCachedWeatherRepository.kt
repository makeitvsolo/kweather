package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.cache.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.cache.CachedWeatherRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.cache.error.CachedWeatherRepositoryConfigurationError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.MongoForecastRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.WeatherApiWeatherRepository

class ConfigureCachedWeatherRepository internal constructor(
    private var weatherApiRepository: WeatherApiWeatherRepository? = null,
    private var mongoRepository: MongoForecastRepository? = null
) {

    fun base(weatherApiRepository: WeatherApiWeatherRepository) =
        apply { this.weatherApiRepository = weatherApiRepository }

    fun cache(mongoRepository: MongoForecastRepository) =
        apply { this.mongoRepository = mongoRepository }

    fun configured(): Result<CachedWeatherRepository, CachedWeatherRepositoryConfigurationError> {
        val appliedWeatherApiRepository = weatherApiRepository
        val appliedMongoRepository = mongoRepository

        appliedWeatherApiRepository ?: return Result.error(
            CachedWeatherRepositoryConfigurationError.WeatherApiRepositoryError("missing base repository")
        )

        appliedMongoRepository ?: return Result.error(
            CachedWeatherRepositoryConfigurationError.MongoRepositoryError("missing mongo repository")
        )

        return Result.ok(
            CachedWeatherRepository(appliedWeatherApiRepository, appliedMongoRepository)
        )
    }

    companion object {

        fun with(): ConfigureCachedWeatherRepository = ConfigureCachedWeatherRepository()
    }
}
