package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.data

import com.google.gson.annotations.SerializedName

internal data class WeatherApiWeather(
    @SerializedName("location") val location: WeatherApiLocation,
    @SerializedName("current") val current: WeatherApiCurrent
)