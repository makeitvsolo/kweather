package github.makeitvsolo.kweather.weather.api.service.location.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.service.location.dto.FavouriteLocationDto
import github.makeitvsolo.kweather.weather.api.service.location.error.RemoveFromFavouritesError

typealias RemoveFromFavouritesPayload = FavouriteLocationDto

interface RemoveFromFavourites {

    fun removeFromFavourites(payload: RemoveFromFavouritesPayload): Result<Unit, RemoveFromFavouritesError>
}
