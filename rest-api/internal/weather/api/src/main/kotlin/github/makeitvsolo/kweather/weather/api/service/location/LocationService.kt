package github.makeitvsolo.kweather.weather.api.service.location

import github.makeitvsolo.kweather.weather.api.service.location.usecase.AddFavourite
import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavourite
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFavourite
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchLocation

interface LocationService :
    AddFavourite,
    RemoveFavourite,
    FetchFavourite,
    SearchLocation
