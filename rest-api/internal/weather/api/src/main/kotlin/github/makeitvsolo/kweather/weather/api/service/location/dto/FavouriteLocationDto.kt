package github.makeitvsolo.kweather.weather.api.service.location.dto

import java.math.BigDecimal

data class FavouriteLocationDto(
    val accountId: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal
)
