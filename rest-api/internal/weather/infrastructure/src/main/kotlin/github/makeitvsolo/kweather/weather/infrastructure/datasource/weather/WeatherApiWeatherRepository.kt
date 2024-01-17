package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.weather.WeatherRepository
import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.FindForecastError
import github.makeitvsolo.kweather.weather.api.datasource.weather.operation.FindWeatherError
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

import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result.Failure
import com.github.kittinunf.result.Result.Success
import com.google.gson.annotations.SerializedName

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class WeatherApiLocation(
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String,
    @SerializedName("country") val country: String,
    @SerializedName("lat") val latitude: BigDecimal,
    @SerializedName("lon") val longitude: BigDecimal,
    @SerializedName("tz_id") val timezone: String,
    @SerializedName("localtime") val localtime: String
)

data class WeatherApiCurrentCondition(
    @SerializedName("text") val summary: String,
    @SerializedName("code") val code: Int
)

data class WeatherApiCurrent(
    @SerializedName("condition") val condition: WeatherApiCurrentCondition,
    @SerializedName("temp_c") val temperature: Double,
    @SerializedName("feelslike_c") val feelsLike: Double,
    @SerializedName("wind_kph") val windSpeed: Double,
    @SerializedName("wind_degree") val windDegree: Int,
    @SerializedName("wind_dir") val windDirection: String,
    @SerializedName("gust_kph") val windGust: Double,
    @SerializedName("pressure_mb") val pressure: Double,
    @SerializedName("precip_mm") val precipitation: Double,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("cloud") val cloud: Int,
    @SerializedName("vis_km") val visibility: Double
)

data class WeatherApiWeather(
    @SerializedName("location") val location: WeatherApiLocation,
    @SerializedName("current") val current: WeatherApiCurrent
)

data class WeatherApiDailyWeather(
    @SerializedName("condition") val condition: WeatherApiCurrentCondition,
    @SerializedName("maxtemp_c") val maxTemperature: Double,
    @SerializedName("mintemp_c") val minTemperature: Double,
    @SerializedName("avgtemp_c") val averageTemperature: Double,
    @SerializedName("maxwind_kph") val wind: Double,
    @SerializedName("totalprecip_mm") val precipitation: Double,
    @SerializedName("avgvis_km") val visibility: Double,
    @SerializedName("avghumidity") val humidity: Int,
    @SerializedName("daily_will_it_rain") val rainChance: Int,
    @SerializedName("daily_will_it_snow") val snowChance: Int
)

data class WeatherApiForecastDay(
    @SerializedName("date") val date: String,
    @SerializedName("day") val weather: WeatherApiDailyWeather
)

data class WeatherApiDailyForecast(
    @SerializedName("forecastday") val days: List<WeatherApiForecastDay>
)

data class WeatherApiForecast(
    @SerializedName("location") val location: WeatherApiLocation,
    @SerializedName("forecast") val forecast: WeatherApiDailyForecast,
)

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
