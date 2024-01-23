package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.weatherapi.data

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class WeatherApiLocation internal constructor(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String,
    @SerializedName("country") val country: String,
    @SerializedName("lat") val latitude: BigDecimal,
    @SerializedName("lon") val longitude: BigDecimal,
    @SerializedName("url") val url: String
)
