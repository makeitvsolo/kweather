package github.makeitvsolo.kweather.weather.application

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.api.service.location.LocationService
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchAllFavouritePayload
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchAllFavouriteResponse
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavourite
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavouriteError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavouritePayload
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavouriteResponse
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFromFavourites
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFromFavouritesError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFromFavouritesPayload
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SaveToFavourites
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SaveToFavouritesError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SaveToFavouritesPayload
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchForLocation
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchForLocationError
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchForLocationPayload
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchForLocationResponse

class ApplicationLocationService(
    private val saveUsecase: SaveToFavourites,
    private val removeUsecase: RemoveFromFavourites,
    private val fetchUsecase: FetchFavourite,
    private val searchUsecase: SearchForLocation
) : LocationService {

    override fun saveToFavourites(payload: SaveToFavouritesPayload): Result<Unit, SaveToFavouritesError> =
        saveUsecase.saveToFavourites(payload)

    override fun removeFromFavourites(payload: RemoveFromFavouritesPayload): Result<Unit, RemoveFromFavouritesError> =
        removeUsecase.removeFromFavourites(payload)

    override fun fetchAllFavourite(payload: FetchAllFavouritePayload):
    Result<FetchAllFavouriteResponse, FetchFavouriteError> =
        fetchUsecase.fetchAllFavourite(payload)

    override fun fetchFavourite(payload: FetchFavouritePayload): Result<FetchFavouriteResponse, FetchFavouriteError> =
        fetchUsecase.fetchFavourite(payload)

    override fun search(payload: SearchForLocationPayload): Result<SearchForLocationResponse, SearchForLocationError> =
        searchUsecase.search(payload)
}
