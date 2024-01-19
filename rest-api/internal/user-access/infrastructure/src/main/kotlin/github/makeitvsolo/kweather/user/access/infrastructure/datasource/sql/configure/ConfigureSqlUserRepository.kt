package github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.SqlUserRepository
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.error.SqlUserRepositoryConfigurationError

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class ConfigureSqlUserRepository internal constructor(
    private var url: String? = null,
    private var username: String? = null,
    private var password: String? = null
) {

    fun datasourceUrl(url: String) = apply { this.url = url }
    fun username(username: String) = apply { this.username = username }
    fun password(password: String) = apply { this.password = password }

    fun configured(): Result<SqlUserRepository, SqlUserRepositoryConfigurationError> {
        val appliedUrl = url
        val appliedUsername = username
        val appliedPassword = password

        appliedUrl ?: return Result.error(
            SqlUserRepositoryConfigurationError.DataSourceUrlError("missing datasource url")
        )

        appliedUsername ?: return Result.error(
            SqlUserRepositoryConfigurationError.InvalidCredentialsError("missing username")
        )

        appliedPassword ?: return Result.error(
            SqlUserRepositoryConfigurationError.InvalidCredentialsError("missing password")
        )

        val config = HikariConfig().apply {
            jdbcUrl = appliedUrl
            username = appliedUsername
            password = appliedPassword
        }

        val datasource = HikariDataSource(config)
        return Result.ok(
            SqlUserRepository(datasource)
        )
    }

    companion object {

        fun with(): ConfigureSqlUserRepository = ConfigureSqlUserRepository()
    }
}
