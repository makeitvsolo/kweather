package github.makeitvsolo.kweather.weather.api.datasource.weather.operation

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.FindWeatherError
import github.makeitvsolo.kweather.weather.domain.weather.current.Weather

import java.math.BigDecimal

interface FindWeather {

    fun findByCoordinates(
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<Weather, FindWeatherError>
}
