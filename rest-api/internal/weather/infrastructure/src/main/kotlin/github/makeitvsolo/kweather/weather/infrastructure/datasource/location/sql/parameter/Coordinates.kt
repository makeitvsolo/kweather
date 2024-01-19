package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.sql.parameter

import java.math.BigDecimal

data class Coordinates internal constructor(val latitude: BigDecimal, val longitude: BigDecimal)
