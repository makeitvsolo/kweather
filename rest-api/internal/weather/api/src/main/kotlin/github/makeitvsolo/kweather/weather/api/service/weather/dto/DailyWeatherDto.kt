package github.makeitvsolo.kweather.weather.api.service.weather.dto

import github.makeitvsolo.kweather.weather.domain.weather.forecast.MapDailyWeatherInto
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyHumidity
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyPrecipitation
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyTemperature
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyWind

import java.time.LocalDate

data class DailyWeatherDto(
    val code: Int,
    val summary: String,
    val date: LocalDate,
    val temperature: DailyTemperatureDto,
    val wind: Double,
    val humidity: Int,
    val precipitation: Double,
    val rainChance: Int,
    val snowChance: Int
) {

    object FromDailyWeather : MapDailyWeatherInto<DailyWeatherDto> {

        override fun from(
            code: Int,
            summary: String,
            date: LocalDate,
            temperature: DailyTemperature,
            wind: DailyWind,
            humidity: DailyHumidity,
            precipitation: DailyPrecipitation
        ): DailyWeatherDto =
            DailyWeatherDto(
                code,
                summary,
                date,
                DailyTemperatureDto(temperature.average, temperature.max, temperature.min),
                wind.speed,
                humidity.average,
                precipitation.amount,
                precipitation.rainChance,
                precipitation.snowChance
            )
    }
}
