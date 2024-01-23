package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.base.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.base.BaseLocationRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.base.error.BaseLocationRepositoryConfigurationError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.SqlLocationRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.weatherapi.WeatherApiLocationRepository

class ConfigureBaseLocationRepository internal constructor(
    private var weatherApiRepository: WeatherApiLocationRepository? = null,
    private var sqlRepository: SqlLocationRepository? = null
) {

    fun weatherApi(weatherApiRepository: WeatherApiLocationRepository) =
        apply { this.weatherApiRepository = weatherApiRepository }

    fun sql(sqlRepository: SqlLocationRepository) =
        apply { this.sqlRepository = sqlRepository }

    fun configured(): Result<BaseLocationRepository, BaseLocationRepositoryConfigurationError> {
        val appliedWeatherApiRepository = weatherApiRepository
        val appliedSqlRepository = sqlRepository

        appliedWeatherApiRepository ?: return Result.error(
            BaseLocationRepositoryConfigurationError.WeatherApiRepositoryError("missing weather api repository")
        )

        appliedSqlRepository ?: return Result.error(
            BaseLocationRepositoryConfigurationError.SqlRepositoryError("missing sql repository")
        )

        return Result.ok(
            BaseLocationRepository(appliedSqlRepository, appliedWeatherApiRepository)
        )
    }

    companion object {

        fun with(): ConfigureBaseLocationRepository = ConfigureBaseLocationRepository()
    }
}
