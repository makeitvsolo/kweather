package github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.SqlUserRepository
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.error.SqlUserRepositoryConfigurationError

import javax.sql.DataSource

class ConfigureSqlUserRepository internal constructor(
    private var dataSource: DataSource? = null
) {

    fun datasource(dataSource: DataSource) = apply { this.dataSource = dataSource }

    fun configured(): Result<SqlUserRepository, SqlUserRepositoryConfigurationError> {
        val appliedDatasource = dataSource

        appliedDatasource ?: return Result.error(
            SqlUserRepositoryConfigurationError("missing datasource")
        )

        return Result.ok(
            SqlUserRepository(appliedDatasource)
        )
    }

    companion object {

        fun with(): ConfigureSqlUserRepository = ConfigureSqlUserRepository()
    }
}
