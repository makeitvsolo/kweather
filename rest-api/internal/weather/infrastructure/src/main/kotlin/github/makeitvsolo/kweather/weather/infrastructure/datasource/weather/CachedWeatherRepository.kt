package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.weather.WeatherRepository
import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.FindForecastError
import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.FindWeatherError
import github.makeitvsolo.kweather.weather.domain.weather.current.Weather
import github.makeitvsolo.kweather.weather.domain.weather.forecast.DailyWeather

import java.math.BigDecimal

data class CleanCacheError internal constructor(private val throwable: Throwable) : IntoThrowable {

    override fun intoThrowable(): Throwable = throwable
}

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
        mongo.dropCollection().mapError { error ->
            CleanCacheError(error.intoThrowable())
        }
}
