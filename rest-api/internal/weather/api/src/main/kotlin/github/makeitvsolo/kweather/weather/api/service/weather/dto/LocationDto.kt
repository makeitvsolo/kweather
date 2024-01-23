package github.makeitvsolo.kweather.weather.api.service.weather.dto

import github.makeitvsolo.kweather.weather.domain.location.MapLocationInto

import java.math.BigDecimal

data class LocationDto(
    val name: String,
    val region: String,
    val country: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal
) {

    object FromLocation : MapLocationInto<LocationDto> {

        override fun from(
            name: String,
            region: String,
            country: String,
            latitude: BigDecimal,
            longitude: BigDecimal,
            isFavourite: Boolean
        ): LocationDto =
            LocationDto(name, region, country, latitude, longitude)
    }
}
