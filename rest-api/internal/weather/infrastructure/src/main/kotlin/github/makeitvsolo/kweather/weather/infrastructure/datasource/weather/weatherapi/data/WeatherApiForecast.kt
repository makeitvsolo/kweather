package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.data

import com.google.gson.annotations.SerializedName

internal data class WeatherApiForecast(
    @SerializedName("location") val location: WeatherApiLocation,
    @SerializedName("forecast") val forecast: WeatherApiDailyForecast,
)
