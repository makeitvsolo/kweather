package github.makeitvsolo.kweather.user.access.api.service.user.exception

import github.makeitvsolo.kweather.core.error.handling.KweatherException

class UserAlreadyExistsException(message: String) : KweatherException(message)
