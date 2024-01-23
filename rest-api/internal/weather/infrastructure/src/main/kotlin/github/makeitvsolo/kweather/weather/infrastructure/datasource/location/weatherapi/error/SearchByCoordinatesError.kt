package github.makeitvsolo.kweather.weather.infrastructure.datasource.location.weatherapi.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.weather.api.datasource.location.exception.LocationRepositoryException

sealed interface SearchByCoordinatesError : IntoThrowable {

    data class NotFoundError internal constructor(private val details: String) : SearchByCoordinatesError {

        override fun intoThrowable(): Throwable = LocationRepositoryException(details)
    }

    data class InternalError internal constructor(private val throwable: Throwable) : SearchByCoordinatesError {

        override fun intoThrowable(): Throwable = throwable
    }
}
