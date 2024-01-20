package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.base.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.base.BaseLocationRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.base.error.BaseLocationRepositoryConfigurationError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.SqlLocationRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.weatherapi.WeatherApiLocationRepository

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class ConfigureBaseLocationRepository internal constructor(
    private var url: String? = null,
    private var username: String? = null,
    private var password: String? = null,
    private var apiKey: String? = null
) {

    fun datasourceUrl(url: String) = apply { this.url = url }
    fun username(username: String) = apply { this.username = username }
    fun password(password: String) = apply { this.password = password }
    fun apiKey(key: String) = apply { this.apiKey = key }

    fun configured(): Result<BaseLocationRepository, BaseLocationRepositoryConfigurationError> {
        val appliedUrl = url
        val appliedUsername = username
        val appliedPassword = password
        val appliedApiKey = apiKey

        appliedUrl ?: return Result.error(
            BaseLocationRepositoryConfigurationError.DataSourceUrlError("missing datasource url")
        )

        appliedUsername ?: return Result.error(
            BaseLocationRepositoryConfigurationError.InvalidCredentialsError("missing username")
        )

        appliedPassword ?: return Result.error(
            BaseLocationRepositoryConfigurationError.InvalidCredentialsError("missing password")
        )

        appliedApiKey ?: return Result.error(
            BaseLocationRepositoryConfigurationError.InvalidApiKeyError("missing api key")
        )

        val config = HikariConfig().apply {
            jdbcUrl = appliedUrl
            username = appliedUsername
            password = appliedPassword
        }

        val datasource = HikariDataSource(config)

        val sqlRepository = SqlLocationRepository(datasource)
        val weatherApiRepository = WeatherApiLocationRepository(appliedApiKey)

        return Result.ok(
            BaseLocationRepository(sqlRepository, weatherApiRepository)
        )
    }

    companion object {

        fun with(): ConfigureBaseLocationRepository = ConfigureBaseLocationRepository()
    }
}
