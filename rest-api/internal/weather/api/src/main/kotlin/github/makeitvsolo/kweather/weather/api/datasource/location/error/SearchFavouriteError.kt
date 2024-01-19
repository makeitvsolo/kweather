package github.makeitvsolo.kweather.weather.api.datasource.location.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.location.exception.LocationRepositoryException

interface MapSearchLocationErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface SearchLocationError : IntoThrowable {

    fun <R, M : MapSearchLocationErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : SearchLocationError {

        override fun <R, M : MapSearchLocationErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = LocationRepositoryException(details)
    }

    data class InternalError(private val throwable: Throwable) : SearchLocationError {

        override fun <R, M : MapSearchLocationErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
