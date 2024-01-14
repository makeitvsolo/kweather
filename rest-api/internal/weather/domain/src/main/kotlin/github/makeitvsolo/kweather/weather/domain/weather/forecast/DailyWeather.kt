package github.makeitvsolo.kweather.weather.domain.weather.forecast

import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyHumidity
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyPrecipitation
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyTemperature
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyWind

import java.time.LocalDate

interface MapDailyWeatherInto<out R> : Into<R> {

    fun from(
        code: Int,
        summary: String,
        date: LocalDate,
        temperature: DailyTemperature,
        wind: DailyWind,
        humidity: DailyHumidity,
        precipitation: DailyPrecipitation
    ): R
}

class DailyWeather internal constructor(
    private val code: Int,
    private val summary: String,
    private val date: LocalDate,
    private val temperature: DailyTemperature,
    private val wind: DailyWind,
    private val humidity: DailyHumidity,
    private val precipitation: DailyPrecipitation
) {

    fun <R, M : MapDailyWeatherInto<R>> mapBy(map: M): R =
        map.from(code, summary, date, temperature, wind, humidity, precipitation)

    companion object {

        fun from(
            code: Int,
            summary: String,
            date: LocalDate,
            temperature: DailyTemperature,
            wind: DailyWind,
            humidity: DailyHumidity,
            precipitation: DailyPrecipitation
        ): DailyWeather =
            DailyWeather(code, summary, date, temperature, wind, humidity, precipitation)
    }
}
