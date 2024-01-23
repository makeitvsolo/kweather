package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.data

import github.makeitvsolo.kweather.weather.domain.weather.forecast.DailyWeather
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyHumidity
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyPrecipitation
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyTemperature
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyWind

import com.google.gson.annotations.SerializedName

import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal data class WeatherApiForecast(
    @SerializedName("location") val location: WeatherApiLocation,
    @SerializedName("forecast") val forecast: WeatherApiDailyForecast,
) {

    fun intoForecast(): List<DailyWeather> =
        forecast.days.map { day ->
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

    companion object {

        private const val DATE_FORMAT = "yyyy-MM-dd"
    }
}
