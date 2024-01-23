package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.data

import github.makeitvsolo.kweather.weather.domain.weather.current.Weather
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Cloudiness
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Humidity
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Precipitation
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Pressure
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Temperature
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Wind

import com.google.gson.annotations.SerializedName

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal data class WeatherApiWeather(
    @SerializedName("location") val location: WeatherApiLocation,
    @SerializedName("current") val current: WeatherApiCurrent
) {

    fun intoWeather(): Weather =
        Weather.from(
            current.condition.code,
            current.condition.summary,
            LocalDateTime.parse(location.localtime, DateTimeFormatter.ofPattern(DATETIME_FORMAT)),
            Temperature(current.temperature, current.feelsLike),
            Wind(
                current.windSpeed,
                current.windGust,
                current.windDegree,
                current.windDirection
            ),
            Pressure(current.pressure),
            Humidity(current.humidity),
            Cloudiness(current.cloud),
            Precipitation(current.precipitation)
        )

    companion object {

        private const val DATETIME_FORMAT = "yyyy-MM-dd H[H]:mm"
    }
}
