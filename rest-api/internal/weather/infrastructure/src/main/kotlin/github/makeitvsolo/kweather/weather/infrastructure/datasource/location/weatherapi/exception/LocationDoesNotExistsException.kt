package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.weatherapi.exception

import github.makeitvsolo.kweather.core.error.handling.KweatherException

class LocationDoesNotExistsException internal constructor(message: String) : KweatherException(message)
