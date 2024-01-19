package github.makeitvsolo.kweather.weather.api.service.location.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.location.error.MapFindFavouriteErrorInto
import github.makeitvsolo.kweather.weather.api.service.location.exception.LocationServiceException

interface MapFetchFavouriteErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FetchFavouriteError : IntoThrowable {

    object FromFindFavouriteError : MapFindFavouriteErrorInto<FetchFavouriteError> {

        override fun fromNotFoundError(details: String): FetchFavouriteError =
            NotFoundError(details)

        override fun fromInternalError(throwable: Throwable): FetchFavouriteError =
            InternalError(throwable)
    }

    fun <R, M : MapFetchFavouriteErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : FetchFavouriteError {

        override fun <R, M : MapFetchFavouriteErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = LocationServiceException(details)
    }

    data class InternalError(private val throwable: Throwable) : FetchFavouriteError {

        override fun <R, M : MapFetchFavouriteErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
