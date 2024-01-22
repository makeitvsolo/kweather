package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.SqlAccountRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.error.SqlAccountRepositoryConfigurationError

import javax.sql.DataSource

class ConfigureSqlAccountRepository internal constructor(
    private var dataSource: DataSource? = null
) {

    fun datasource(dataSource: DataSource) = apply { this.dataSource = dataSource }

    fun configured(): Result<SqlAccountRepository, SqlAccountRepositoryConfigurationError> {
        val appliedDatasource = dataSource

        appliedDatasource ?: return Result.error(
            SqlAccountRepositoryConfigurationError("missing datasource")
        )

        return Result.ok(
            SqlAccountRepository(appliedDatasource)
        )
    }

    companion object {

        fun with(): ConfigureSqlAccountRepository = ConfigureSqlAccountRepository()
    }
}
