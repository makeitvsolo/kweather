package github.makeitvsolo.kweather.weather.api.datasource.location.exception

import github.makeitvsolo.kweather.core.error.handling.KweatherException

class LocationDoesNotExistsException(message: String) : KweatherException(message)
