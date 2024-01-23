package github.makeitvsolo.kweather.boot.api.controller.weather.request

import jakarta.validation.constraints.NotNull

import java.math.BigDecimal

data class CoordinatesRequest(

    @NotNull
    val latitude: BigDecimal,

    @NotNull
    val longitude: BigDecimal
)
