package github.makeitvsolo.kweather.weather.api.service.weather.exception

import github.makeitvsolo.kweather.core.error.handling.KweatherException

class ForecastNotFoundException(message: String) : KweatherException(message)
