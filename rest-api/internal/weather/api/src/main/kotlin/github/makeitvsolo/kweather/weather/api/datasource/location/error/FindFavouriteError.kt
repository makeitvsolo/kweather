package github.makeitvsolo.kweather.weather.api.datasource.location.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.location.exception.LocationNotFoundInFavouritesException

interface MapFindFavouriteErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FindFavouriteError : IntoThrowable {

    fun <R, M : MapFindFavouriteErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : FindFavouriteError {

        override fun <R, M : MapFindFavouriteErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = LocationNotFoundInFavouritesException(details)
    }

    data class InternalError(private val throwable: Throwable) : FindFavouriteError {

        override fun <R, M : MapFindFavouriteErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
