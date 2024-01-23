package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.data

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

internal data class WeatherApiLocation(
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String,
    @SerializedName("country") val country: String,
    @SerializedName("lat") val latitude: BigDecimal,
    @SerializedName("lon") val longitude: BigDecimal,
    @SerializedName("tz_id") val timezone: String,
    @SerializedName("localtime") val localtime: String
)
