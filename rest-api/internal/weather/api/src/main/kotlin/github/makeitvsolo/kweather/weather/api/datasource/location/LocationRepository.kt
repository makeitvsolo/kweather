package github.makeitvsolo.kweather.weather.api.datasource.location

import github.makeitvsolo.kweather.weather.api.datasource.location.operation.AddFavourite
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.FindFavourite
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.RemoveFavourite
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.SearchLocation

interface LocationRepository :
    AddFavourite,
    RemoveFavourite,
    FindFavourite,
    SearchLocation
