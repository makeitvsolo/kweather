package github.makeitvsolo.kweather.weather.api.datasource.location.operation

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.error.FindFavouriteError
import github.makeitvsolo.kweather.weather.domain.location.Location

import java.math.BigDecimal

interface FindFavourite {

    fun findAllFavourite(accountId: String): Result<List<Location>, FindFavouriteError>

    fun findFavourite(
        accountId: String,
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<Location, FindFavouriteError>
}
