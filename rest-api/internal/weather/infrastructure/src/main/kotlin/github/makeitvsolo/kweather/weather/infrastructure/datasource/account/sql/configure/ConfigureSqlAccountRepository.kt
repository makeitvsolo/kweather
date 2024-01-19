package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.SqlAccountRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.error.SqlAccountRepositoryConfigurationError

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class ConfigureSqlAccountRepository internal constructor(
    private var url: String? = null,
    private var username: String? = null,
    private var password: String? = null
) {

    fun datasourceUrl(url: String) = apply { this.url = url }
    fun username(username: String) = apply { this.username = username }
    fun password(password: String) = apply { this.password = password }

    fun configured(): Result<SqlAccountRepository, SqlAccountRepositoryConfigurationError> {
        val appliedUrl = url
        val appliedUsername = username
        val appliedPassword = password

        appliedUrl ?: return Result.error(
            SqlAccountRepositoryConfigurationError.DataSourceUrlError("missing datasource url")
        )

        appliedUsername ?: return Result.error(
            SqlAccountRepositoryConfigurationError.InvalidCredentialsError("missing username")
        )

        appliedPassword ?: return Result.error(
            SqlAccountRepositoryConfigurationError.InvalidCredentialsError("missing password")
        )

        val config = HikariConfig().apply {
            jdbcUrl = appliedUrl
            username = appliedUsername
            password = appliedPassword
        }

        val datasource = HikariDataSource(config)
        return Result.ok(
            SqlAccountRepository(datasource)
        )
    }

    companion object {

        fun with(): ConfigureSqlAccountRepository = ConfigureSqlAccountRepository()
    }
}