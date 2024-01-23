package github.makeitvsolo.kweather.user.access.infrastructure.configure.sql

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.infrastructure.configure.sql.error.ConfigureJdbcDatasourceError

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

import javax.sql.DataSource

class ConfigureJdbcDatasource internal constructor(
    private var url: String? = null,
    private var username: String? = null,
    private var password: String? = null
) {

    fun url(url: String) = apply { this.url = url }
    fun username(username: String) = apply { this.username = username }
    fun password(password: String) = apply { this.password = password }

    fun configured(): Result<DataSource, ConfigureJdbcDatasourceError> {
        val appliedUrl = url
        val appliedUsername = username
        val appliedPassword = password

        appliedUrl ?: return Result.error(
            ConfigureJdbcDatasourceError.DataSourceUrlError("missing datasource url")
        )

        appliedUsername ?: return Result.error(
            ConfigureJdbcDatasourceError.InvalidCredentialsError("missing username")
        )

        appliedPassword ?: return Result.error(
            ConfigureJdbcDatasourceError.InvalidCredentialsError("missing password")
        )

        val config = HikariConfig().apply {
            jdbcUrl = appliedUrl
            username = appliedUsername
            password = appliedPassword
        }

        return Result.ok(HikariDataSource(config))
    }

    companion object {

        fun with(): ConfigureJdbcDatasource = ConfigureJdbcDatasource()
    }
}
