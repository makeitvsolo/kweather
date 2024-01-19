package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.parameter

import com.google.gson.annotations.SerializedName

internal data class WeatherApiDailyWeather(
    @SerializedName("condition") val condition: WeatherApiCurrentCondition,
    @SerializedName("maxtemp_c") val maxTemperature: Double,
    @SerializedName("mintemp_c") val minTemperature: Double,
    @SerializedName("avgtemp_c") val averageTemperature: Double,
    @SerializedName("maxwind_kph") val wind: Double,
    @SerializedName("totalprecip_mm") val precipitation: Double,
    @SerializedName("avgvis_km") val visibility: Double,
    @SerializedName("avghumidity") val humidity: Int,
    @SerializedName("daily_will_it_rain") val rainChance: Int,
    @SerializedName("daily_will_it_snow") val snowChance: Int
)
