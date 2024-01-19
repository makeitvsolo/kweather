package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.parameter

import com.google.gson.annotations.SerializedName

internal data class WeatherApiForecastDay(
    @SerializedName("date") val date: String,
    @SerializedName("day") val weather: WeatherApiDailyWeather
)
