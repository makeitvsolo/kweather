package github.makeitvsolo.kweather.user.access.api.service.user.exception

import github.makeitvsolo.kweather.core.error.handling.KWeatherException

class UserAlreadyExistsException(message: String) : KWeatherException(message)
