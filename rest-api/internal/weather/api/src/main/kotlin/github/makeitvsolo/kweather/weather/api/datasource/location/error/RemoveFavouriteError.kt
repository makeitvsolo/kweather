package github.makeitvsolo.kweather.weather.api.datasource.location.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.location.exception.LocationNotFoundInFavouritesException

interface MapRemoveFavouriteErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface RemoveFavouriteError : IntoThrowable {

    fun <R, M : MapRemoveFavouriteErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : RemoveFavouriteError {

        override fun <R, M : MapRemoveFavouriteErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = LocationNotFoundInFavouritesException(details)
    }

    data class InternalError(private val throwable: Throwable) : RemoveFavouriteError {

        override fun <R, M : MapRemoveFavouriteErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
