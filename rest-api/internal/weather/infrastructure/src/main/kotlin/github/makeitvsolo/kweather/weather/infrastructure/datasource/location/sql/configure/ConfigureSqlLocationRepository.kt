package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.SqlLocationRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.error.SqlLocationRepositoryConfigurationError

import javax.sql.DataSource

class ConfigureSqlLocationRepository internal constructor(
    private var dataSource: DataSource? = null
) {

    fun datasource(dataSource: DataSource) = apply { this.dataSource = dataSource }

    fun configured(): Result<SqlLocationRepository, SqlLocationRepositoryConfigurationError> {
        val appliedDatasource = dataSource

        appliedDatasource ?: return Result.error(
            SqlLocationRepositoryConfigurationError("missing datasource")
        )

        return Result.ok(
            SqlLocationRepository(appliedDatasource)
        )
    }

    companion object {

        fun with(): ConfigureSqlLocationRepository = ConfigureSqlLocationRepository()
    }
}
