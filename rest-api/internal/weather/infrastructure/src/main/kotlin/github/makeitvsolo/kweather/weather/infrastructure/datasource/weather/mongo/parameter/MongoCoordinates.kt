package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.parameter

import java.math.BigDecimal

internal data class MongoCoordinates(
    val latitude: BigDecimal,
    val longitude: BigDecimal
)
