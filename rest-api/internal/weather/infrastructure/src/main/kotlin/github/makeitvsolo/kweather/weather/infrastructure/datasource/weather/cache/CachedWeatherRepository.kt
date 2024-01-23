package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.cache

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.weather.WeatherRepository
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.FindForecastError
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.FindWeatherError
import github.makeitvsolo.kweather.weather.domain.weather.current.Weather
import github.makeitvsolo.kweather.weather.domain.weather.forecast.DailyWeather
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.cache.error.CleanCacheError
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.MongoForecastRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.WeatherApiWeatherRepository

import java.math.BigDecimal

class CachedWeatherRepository internal constructor(
    private val weatherApi: WeatherApiWeatherRepository,
    private val mongo: MongoForecastRepository
) : WeatherRepository {

    override fun findByCoordinates(latitude: BigDecimal, longitude: BigDecimal): Result<Weather, FindWeatherError> =
        weatherApi.findByCoordinates(latitude, longitude)

    override fun findForecastByCoordinates(
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<List<DailyWeather>, FindForecastError> =
        mongo.findForecastByCoordinates(latitude, longitude).or {
            weatherApi.findForecastByCoordinates(latitude, longitude).ifOk { forecast ->
                mongo.saveForecast(latitude, longitude, forecast)
            }
        }

    fun cleanCache(): Result<Unit, CleanCacheError> =
        mongo.truncateCollection().mapError { error ->
            CleanCacheError(error.intoThrowable())
        }
}
