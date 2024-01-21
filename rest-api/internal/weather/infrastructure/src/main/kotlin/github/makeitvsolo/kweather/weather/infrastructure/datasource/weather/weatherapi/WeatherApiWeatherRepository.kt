package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.weather.WeatherRepository
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.FindForecastError
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.FindWeatherError
import github.makeitvsolo.kweather.weather.domain.weather.current.Weather
import github.makeitvsolo.kweather.weather.domain.weather.forecast.DailyWeather
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.data.WeatherApiForecast
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.data.WeatherApiWeather

import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result.Failure
import com.github.kittinunf.result.Result.Success

import java.math.BigDecimal

class WeatherApiWeatherRepository internal constructor(
    private val apiKey: String,
    private val forecastDays: Int
) : WeatherRepository {

    override fun findByCoordinates(latitude: BigDecimal, longitude: BigDecimal): Result<Weather, FindWeatherError> {
        val (_, _, result) = WEATHER_API_URL.httpGet(
            listOf(
                KEY_PARAMETER to apiKey,
                QUERY_PARAMETER to "$latitude,$longitude"
            )
        )
            .responseObject<WeatherApiWeather>()

        return when (result) {
            is Success -> {
                val response = result.get()

                Result.ok(
                    response.intoWeather()
                )
            }

            is Failure -> Result.error(FindWeatherError.InternalError(result.getException()))
        }
    }

    override fun findForecastByCoordinates(
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<List<DailyWeather>, FindForecastError> {
        val (_, _, result) = FORECAST_API_URL.httpGet(
            listOf(
                KEY_PARAMETER to apiKey,
                QUERY_PARAMETER to "$latitude,$longitude",
                FORECAST_DAYS_PARAMETER to forecastDays
            )
        )
            .responseObject<WeatherApiForecast>()

        return when (result) {
            is Success -> {
                val response = result.get()

                Result.ok(
                    response.intoForecast()
                )
            }

            is Failure -> Result.error(FindForecastError.InternalError(result.getException()))
        }
    }

    companion object {

        private const val FORECAST_API_URL = "http://api.weatherapi.com/v1/forecast.json"
        private const val FORECAST_DAYS_PARAMETER = "days"
        private const val KEY_PARAMETER = "key"
        private const val QUERY_PARAMETER = "q"
        private const val WEATHER_API_URL = "http://api.weatherapi.com/v1/current.json"
    }
}
