package github.makeitvsolo.kweather.weather.api.service.location.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.location.error.MapAddFavouriteErrorInto
import github.makeitvsolo.kweather.weather.api.service.location.exception.LocationServiceException

interface MapSaveToFavouritesErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromConflictError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface SaveToFavouritesError : IntoThrowable {

    object FromAddFavouriteError : MapAddFavouriteErrorInto<SaveToFavouritesError> {

        override fun fromNotFoundError(details: String): SaveToFavouritesError =
            NotFoundError(details)

        override fun fromConflictError(details: String): SaveToFavouritesError =
            ConflictError(details)

        override fun fromInternalError(throwable: Throwable): SaveToFavouritesError =
            InternalError(throwable)
    }

    fun <R, M : MapSaveToFavouritesErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : SaveToFavouritesError {

        override fun <R, M : MapSaveToFavouritesErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = LocationServiceException(details)
    }

    data class ConflictError(private val details: String) : SaveToFavouritesError {

        override fun <R, M : MapSaveToFavouritesErrorInto<R>> into(map: M): R =
            map.fromConflictError(details)

        override fun intoThrowable(): Throwable = LocationServiceException(details)
    }

    data class InternalError(private val throwable: Throwable) : SaveToFavouritesError {

        override fun <R, M : MapSaveToFavouritesErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
