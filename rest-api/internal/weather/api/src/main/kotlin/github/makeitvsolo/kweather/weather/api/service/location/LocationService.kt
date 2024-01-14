package github.makeitvsolo.kweather.weather.api.service.location

import github.makeitvsolo.kweather.weather.api.service.location.usecase.FetchFavourite
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFromFavourites
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SaveToFavourites
import github.makeitvsolo.kweather.weather.api.service.location.usecase.SearchLocation

interface LocationService :
    SaveToFavourites,
    RemoveFromFavourites,
    FetchFavourite,
    SearchLocation
