package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.weatherapi.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

data class WeatherApiLocationRepositoryConfigurationError internal constructor(
    private val details: String
) : IntoThrowable {

    override fun intoThrowable(): Throwable = IllegalArgumentException(details)
}
