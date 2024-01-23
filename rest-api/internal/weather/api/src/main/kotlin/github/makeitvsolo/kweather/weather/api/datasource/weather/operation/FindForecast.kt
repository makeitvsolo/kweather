package github.makeitvsolo.kweather.weather.api.datasource.weather.operation

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.weather.error.FindForecastError
import github.makeitvsolo.kweather.weather.domain.weather.forecast.DailyWeather

import java.math.BigDecimal

interface FindForecast {

    fun findForecastByCoordinates(
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<List<DailyWeather>, FindForecastError>
}
