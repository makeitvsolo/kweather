package github.makeitvsolo.kweather.weather.api.service.location.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.service.location.dto.FavouriteLocationDto
import github.makeitvsolo.kweather.weather.api.service.location.error.SaveToFavouritesError

typealias SaveToFavouritesPayload = FavouriteLocationDto

interface SaveToFavourites {

    fun saveToFavourites(payload: SaveToFavouritesPayload): Result<Unit, SaveToFavouritesError>
}
