package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.parameter

import com.google.gson.annotations.SerializedName

internal data class WeatherApiCurrentCondition(
    @SerializedName("text") val summary: String,
    @SerializedName("code") val code: Int
)
