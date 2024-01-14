package github.makeitvsolo.kweather.weather.api.service.location

import github.makeitvsolo.kweather.weather.api.service.location.usecase.AddFavourite
import github.makeitvsolo.kweather.weather.api.service.location.usecase.RemoveFavourite

interface LocationService : AddFavourite, RemoveFavourite
