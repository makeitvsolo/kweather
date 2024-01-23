package github.makeitvsolo.kweather.weather.domain.location

import github.makeitvsolo.kweather.core.mapping.Into

import java.math.BigDecimal
import java.util.Objects

interface MapLocationInto<out R> : Into<R> {

    fun from(
        name: String,
        region: String,
        country: String,
        latitude: BigDecimal,
        longitude: BigDecimal,
        isFavourite: Boolean
    ): R
}

class Location internal constructor(
    private val name: String,
    private val region: String,
    private val country: String,
    private val latitude: BigDecimal,
    private val longitude: BigDecimal,
    private val isFavourite: Boolean
) {

    fun isFavourite(): Boolean = isFavourite

    fun <R, M : MapLocationInto<R>> into(map: M): R =
        map.from(name, region, country, latitude, longitude, isFavourite)

    override fun equals(other: Any?): Boolean {
        if (other !is Location) {
            return false
        }

        return latitude == other.latitude && longitude == other.longitude
    }

    override fun hashCode(): Int = Objects.hash(latitude, longitude)

    companion object {

        fun from(
            name: String,
            region: String,
            country: String,
            latitude: BigDecimal,
            longitude: BigDecimal,
            isFavourite: Boolean
        ): Location =
            Location(name, region, country, latitude, longitude, isFavourite)

        fun asFavourite(
            name: String,
            region: String,
            country: String,
            latitude: BigDecimal,
            longitude: BigDecimal
        ): Location =
            Location(name, region, country, latitude, longitude, isFavourite = true)
    }
}
