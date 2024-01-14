package github.makeitvsolo.kweather.weather.api.datasource.location

import github.makeitvsolo.kweather.weather.api.datasource.location.operation.AddFavourite
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.RemoveFavourite

interface LocationRepository : AddFavourite, RemoveFavourite
