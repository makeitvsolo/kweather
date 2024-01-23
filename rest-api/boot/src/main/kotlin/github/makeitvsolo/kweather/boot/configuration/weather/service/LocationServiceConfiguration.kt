package github.makeitvsolo.kweather.boot.configuration.weather.service

import github.makeitvsolo.kweather.weather.api.datasource.location.LocationRepository
import github.makeitvsolo.kweather.weather.api.service.location.LocationService
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavourite
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFromFavourites
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SaveToFavourites
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchForLocation
import github.makeitvsolo.kweather.weather.application.location.ApplicationLocationService
import github.makeitvsolo.kweather.weather.application.location.usecase.ApplicationFetchFavourite
import github.makeitvsolo.kweather.weather.application.location.usecase.ApplicationRemoveFromFavourites
import github.makeitvsolo.kweather.weather.application.location.usecase.ApplicationSaveToFavourites
import github.makeitvsolo.kweather.weather.application.location.usecase.ApplicationSearchForLocation

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class LocationServiceConfiguration {

    @Bean
    open fun fetchFavouriteUsecase(
        repository: LocationRepository
    ): FetchFavourite =
        ApplicationFetchFavourite(repository)

    @Bean
    open fun removeFromFavouritesUsecase(
        repository: LocationRepository
    ): RemoveFromFavourites =
        ApplicationRemoveFromFavourites(repository)

    @Bean
    open fun saveToFavouritesUsecase(
        repository: LocationRepository
    ): SaveToFavourites =
        ApplicationSaveToFavourites(repository)

    @Bean
    open fun searchUsecase(
        repository: LocationRepository
    ): SearchForLocation =
        ApplicationSearchForLocation(repository)

    @Bean
    open fun locationService(
        fetchFavouriteUsecase: FetchFavourite,
        removeFromFavouritesUsecase: RemoveFromFavourites,
        saveToFavouritesUsecase: SaveToFavourites,
        searchUsecase: SearchForLocation
    ): LocationService =
        ApplicationLocationService(
            saveToFavouritesUsecase,
            removeFromFavouritesUsecase,
            fetchFavouriteUsecase,
            searchUsecase
        )
}
