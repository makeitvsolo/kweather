package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.exception

import github.makeitvsolo.kweather.core.error.handling.KweatherException

class AccountAlreadyExistsException internal constructor(message: String) : KweatherException(message)
