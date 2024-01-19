package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.weatherapi.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

data class SearchByNameError internal constructor(private val throwable: Throwable) : IntoThrowable {

    override fun intoThrowable(): Throwable = throwable
}
