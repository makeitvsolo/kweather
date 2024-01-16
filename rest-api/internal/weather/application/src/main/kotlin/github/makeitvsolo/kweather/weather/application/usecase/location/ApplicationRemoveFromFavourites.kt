package github.makeitvsolo.kweather.weather.application.usecase.location

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFromFavourites
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFromFavouritesError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFromFavouritesPayload

class ApplicationRemoveFromFavourites(
    private val repository: LocationRepository
) : RemoveFromFavourites {

    override fun removeFromFavourites(payload: RemoveFromFavouritesPayload): Result<Unit, RemoveFromFavouritesError> =
        repository.removeFavourite(payload.accountId, payload.latitude, payload.longitude).mapError { error ->
            error.into(RemoveFromFavouritesError.FromRemoveFavouriteError)
        }
}
