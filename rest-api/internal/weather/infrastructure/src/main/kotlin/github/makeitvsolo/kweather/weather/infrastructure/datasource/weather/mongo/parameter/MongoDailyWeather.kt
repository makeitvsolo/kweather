package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.parameter

import github.makeitvsolo.kweather.weather.domain.weather.forecast.MapDailyWeatherInto
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyHumidity
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyPrecipitation
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyTemperature
import github.makeitvsolo.kweather.weather.domain.weather.forecast.value.DailyWind
import java.time.LocalDate

internal data class MongoDailyWeather(
    val code: Int,
    val summary: String,
    val date: LocalDate,
    val averageTemperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val wind: Double,
    val humidity: Int,
    val precipitation: Double,
    val rainChance: Int,
    val snowChance: Int
) {

    object FromDailyWeather : MapDailyWeatherInto<MongoDailyWeather> {

        override fun from(
            code: Int,
            summary: String,
            date: LocalDate,
            temperature: DailyTemperature,
            wind: DailyWind,
            humidity: DailyHumidity,
            precipitation: DailyPrecipitation
        ): MongoDailyWeather =
            MongoDailyWeather(
                code,
                summary,
                date,
                temperature.average,
                temperature.max,
                temperature.min,
                wind.speed,
                humidity.average,
                precipitation.amount,
                precipitation.rainChance,
                precipitation.snowChance
            )
    }
}
