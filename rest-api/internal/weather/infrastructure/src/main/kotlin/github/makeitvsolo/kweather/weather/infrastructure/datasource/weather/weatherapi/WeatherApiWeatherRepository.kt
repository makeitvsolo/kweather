package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.weather.WeatherRepository
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.FindForecastError
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.FindWeatherError
import github.makeitvsolo.kweather.weather.domain.weather.current.Weather
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Cloudiness
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Humidity
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Precipitation
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Pressure
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Temperature
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Wind
import github.makeitvsolo.kweather.weather.domain.weather.forecast.DailyWeather
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyHumidity
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyPrecipitation
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyTemperature
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyWind
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.data.WeatherApiForecast
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.data.WeatherApiWeather

import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result.Failure
import com.github.kittinunf.result.Result.Success

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
                    Weather.from(
                        response.current.condition.code,
                        response.current.condition.summary,
                        LocalDateTime.parse(response.location.localtime, DateTimeFormatter.ofPattern(DATETIME_FORMAT)),
                        Temperature(response.current.temperature, response.current.feelsLike),
                        Wind(
                            response.current.windSpeed,
                            response.current.windGust,
                            response.current.windDegree,
                            response.current.windDirection
                        ),
                        Pressure(response.current.pressure),
                        Humidity(response.current.humidity),
                        Cloudiness(response.current.cloud),
                        Precipitation(response.current.precipitation)
                    )
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
                    response.forecast.days.map { day ->
                        DailyWeather.from(
                            day.weather.condition.code,
                            day.weather.condition.summary,
                            LocalDate.parse(day.date, DateTimeFormatter.ofPattern(DATE_FORMAT)),
                            DailyTemperature(
                                day.weather.averageTemperature,
                                day.weather.maxTemperature,
                                day.weather.minTemperature
                            ),
                            DailyWind(day.weather.wind),
                            DailyHumidity(day.weather.humidity),
                            DailyPrecipitation(
                                day.weather.precipitation,
                                day.weather.rainChance,
                                day.weather.snowChance
                            )
                        )
                    }
                )
            }

            is Failure -> Result.error(FindForecastError.InternalError(result.getException()))
        }
    }

    companion object {

        private const val DATETIME_FORMAT = "yyyy-MM-dd HH:mm"
        private const val DATE_FORMAT = "yyyy-MM-dd"

        private const val FORECAST_API_URL = "http://api.weatherapi.com/v1/forecast.json"
        private const val FORECAST_DAYS_PARAMETER = "days"
        private const val KEY_PARAMETER = "key"
        private const val QUERY_PARAMETER = "q"
        private const val WEATHER_API_URL = "http://api.weatherapi.com/v1/current.json"
    }
}
