package github.makeitvsolo.kweather.weather.domain.weather.current

import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Cloudiness
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Humidity
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Precipitation
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Pressure
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Temperature
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Wind

import java.time.LocalDateTime

interface MapWeatherInto<out R> : Into<R> {

    fun from(
        code: Int,
        summary: String,
        localTime: LocalDateTime,
        temperature: Temperature,
        wind: Wind,
        pressure: Pressure,
        humidity: Humidity,
        cloudiness: Cloudiness,
        precipitation: Precipitation
    ): R
}

class Weather internal constructor(
    private val code: Int,
    private val summary: String,
    private val localTime: LocalDateTime,
    private val temperature: Temperature,
    private val wind: Wind,
    private val pressure: Pressure,
    private val humidity: Humidity,
    private val cloudiness: Cloudiness,
    private val precipitation: Precipitation
) {

    fun <R, M : MapWeatherInto<R>> mapBy(map: M): R =
        map.from(code, summary, localTime, temperature, wind, pressure, humidity, cloudiness, precipitation)

    companion object {

        fun from(
            code: Int,
            summary: String,
            localTime: LocalDateTime,
            temperature: Temperature,
            wind: Wind,
            pressure: Pressure,
            humidity: Humidity,
            cloudiness: Cloudiness,
            precipitation: Precipitation
        ): Weather =
            Weather(code, summary, localTime, temperature, wind, pressure, humidity, cloudiness, precipitation)
    }
}
