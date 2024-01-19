package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.data

import com.google.gson.annotations.SerializedName

internal data class WeatherApiDailyForecast(
    @SerializedName("forecastday") val days: List<WeatherApiForecastDay>
)
