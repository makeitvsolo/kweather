package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.data

import com.google.gson.annotations.SerializedName

internal data class WeatherApiCurrent(
    @SerializedName("condition") val condition: WeatherApiCurrentCondition,
    @SerializedName("temp_c") val temperature: Double,
    @SerializedName("feelslike_c") val feelsLike: Double,
    @SerializedName("wind_kph") val windSpeed: Double,
    @SerializedName("wind_degree") val windDegree: Int,
    @SerializedName("wind_dir") val windDirection: String,
    @SerializedName("gust_kph") val windGust: Double,
    @SerializedName("pressure_mb") val pressure: Double,
    @SerializedName("precip_mm") val precipitation: Double,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("cloud") val cloud: Int,
    @SerializedName("vis_km") val visibility: Double
)
