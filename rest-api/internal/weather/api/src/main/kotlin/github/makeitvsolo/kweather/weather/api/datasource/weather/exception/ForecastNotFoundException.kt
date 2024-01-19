package github.makeitvsolo.kweather.weather.api.datasource.weather.exception

import github.makeitvsolo.kweather.core.error.handling.KweatherException

class ForecastNotFoundException(message: String) : KweatherException(message)
