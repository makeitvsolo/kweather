package github.makeitvsolo.kweather.weather.api.service.location.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.location.error.MapSearchLocationErrorInto
import github.makeitvsolo.kweather.weather.api.service.location.exception.LocationServiceException

interface MapSearchForLocationErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface SearchForLocationError : IntoThrowable {

    object FromSearchLocation : MapSearchLocationErrorInto<SearchForLocationError> {

        override fun fromNotFoundError(details: String): SearchForLocationError =
            NotFoundError(details)

        override fun fromInternalError(throwable: Throwable): SearchForLocationError =
            InternalError(throwable)
    }

    fun <R, M : MapSearchForLocationErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : SearchForLocationError {

        override fun <R, M : MapSearchForLocationErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = LocationServiceException(details)
    }

    data class InternalError(private val throwable: Throwable) : SearchForLocationError {

        override fun <R, M : MapSearchForLocationErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
