package github.makeitvsolo.kweather.weather.api.datasource.location.operation

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.error.RemoveFavouriteError

import java.math.BigDecimal

interface RemoveFavourite {

    fun removeFavourite(
        accountId: String,
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<Unit, RemoveFavouriteError>
}
