package github.makeitvsolo.kweather.weather.application.usecase.location

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SaveToFavourites
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SaveToFavouritesError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SaveToFavouritesPayload

class ApplicationSaveToFavourites(
    private val repository: LocationRepository
) : SaveToFavourites {

    override fun saveToFavourites(payload: SaveToFavouritesPayload): Result<Unit, SaveToFavouritesError> =
        repository.addFavourite(payload.accountId, payload.latitude, payload.longitude).mapError { error ->
            error.into(SaveToFavouritesError.FromAddFavouriteError)
        }
}
