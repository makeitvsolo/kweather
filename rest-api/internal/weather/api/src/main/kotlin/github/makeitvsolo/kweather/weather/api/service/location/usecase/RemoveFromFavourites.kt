package github.makeitvsolo.kweather.weather.api.service.location.usecase

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.MapRemoveFavouriteErrorInto
import github.makeitvsolo.kweather.weather.api.service.location.dto.FavouriteLocationDto

typealias RemoveFromFavouritesPayload = FavouriteLocationDto

interface MapRemoveFromFavouritesErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface RemoveFromFavouritesError : IntoThrowable {

    object FromRemoveFavouriteError : MapRemoveFavouriteErrorInto<RemoveFromFavouritesError> {

        override fun fromNotFoundError(details: String): RemoveFromFavouritesError =
            NotFoundError(details)

        override fun fromInternalError(throwable: Throwable): RemoveFromFavouritesError =
            InternalError(throwable)
    }

    fun <R, M : MapRemoveFromFavouritesErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : RemoveFromFavouritesError {

        override fun <R, M : MapRemoveFromFavouritesErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = Throwable(details)
    }

    data class InternalError(private val throwable: Throwable) : RemoveFromFavouritesError {

        override fun <R, M : MapRemoveFromFavouritesErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}

interface RemoveFromFavourites {

    fun removeFromFavourites(payload: RemoveFromFavouritesPayload): Result<Unit, RemoveFromFavouritesError>
}
