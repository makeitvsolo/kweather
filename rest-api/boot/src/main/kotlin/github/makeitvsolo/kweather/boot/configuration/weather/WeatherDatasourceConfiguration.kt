package github.makeitvsolo.kweather.boot.configuration.weather

import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.datasource.weather.WeatherRepository
import github.makeitvsolo.kweather.weather.infrastructure.configure.mongo.MongoDatasource
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.base.configure.ConfigureBaseLocationRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.configure.ConfigureSqlLocationRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.weatherapi.configure.ConfigureWeatherApiLocationRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.cache.configure.ConfigureCachedWeatherRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.configure.ConfigureMongoForecastRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.configure.ConfigureWeatherApiWeatherRepository

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

import javax.sql.DataSource

@Configuration
open class WeatherDatasourceConfiguration(env: Environment) {

    private val apiKey: String = env.getProperty("") ?: ""
    private val forecastDays: Int = env.getProperty("", Int::class.java) ?: 0

    @Bean
    fun locationRepository(datasource: DataSource): LocationRepository {
        val sql = ConfigureSqlLocationRepository.with()
            .datasource(datasource)
            .configured()
            .unwrapOrElseThrow { it.intoThrowable() }

        val weatherApi = ConfigureWeatherApiLocationRepository.with()
            .apiKey(apiKey)
            .configured()
            .unwrapOrElseThrow { it.intoThrowable() }

        return ConfigureBaseLocationRepository.with()
            .sql(sql)
            .weatherApi(weatherApi)
            .configured()
            .unwrapOrElseThrow { it.intoThrowable() }
    }

    @Bean
    fun weatherRepository(datasource: MongoDatasource): WeatherRepository {
        val mongo = ConfigureMongoForecastRepository.with()
            .datasource(datasource)
            .configured()
            .unwrapOrElseThrow { it.intoThrowable() }

        val weatherApi = ConfigureWeatherApiWeatherRepository.with()
            .apiKey(apiKey)
            .forecastDays(forecastDays)
            .configured()
            .unwrapOrElseThrow { it.intoThrowable() }

        return ConfigureCachedWeatherRepository.with()
            .base(weatherApi)
            .cache(mongo)
            .configured()
            .unwrapOrElseThrow { it.intoThrowable() }
    }
}
