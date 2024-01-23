package github.makeitvsolo.kweather.weather.api.service.weather.dto

import github.makeitvsolo.kweather.weather.domain.weather.current.MapWeatherInto
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Cloudiness
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Humidity
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Precipitation
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Pressure
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Temperature
import github.makeitvsolo.kweather.weather.domain.weather.current.value.Wind

import java.time.LocalDateTime

data class WeatherDto(
    val code: Int,
    val summary: String,
    val localTime: LocalDateTime,
    val temperature: TemperatureDto,
    val wind: WindDto,
    val pressure: Double,
    val humidity: Int,
    val cloudiness: Int,
    val precipitation: Double
) {

    object FromWeather : MapWeatherInto<WeatherDto> {

        override fun from(
            code: Int,
            summary: String,
            localTime: LocalDateTime,
            temperature: Temperature,
            wind: Wind,
            pressure: Pressure,
            humidity: Humidity,
            cloudiness: Cloudiness,
            precipitation: Precipitation
        ): WeatherDto =
            WeatherDto(
                code,
                summary,
                localTime,
                TemperatureDto(temperature.current, temperature.feelsLike),
                WindDto(wind.speed, wind.gust, wind.degree, wind.direction),
                pressure.value,
                humidity.percent,
                cloudiness.percent,
                precipitation.amount
            )
    }
}
