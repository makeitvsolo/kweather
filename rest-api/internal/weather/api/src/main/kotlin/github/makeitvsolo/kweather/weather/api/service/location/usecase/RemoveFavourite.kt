package github.makeitvsolo.kweather.weather.api.service.location.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.service.location.dto.FavouriteLocationDto

typealias RemoveFavouritePayload = FavouriteLocationDto

interface MapRemoveFavouriteErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface RemoveFavouriteError {

    fun <R, M : MapRemoveFavouriteErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : RemoveFavouriteError {

        override fun <R, M : MapRemoveFavouriteErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)
    }

    data class InternalError(private val throwable: Throwable) : RemoveFavouriteError {

        override fun <R, M : MapRemoveFavouriteErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)
    }
}

interface RemoveFavourite {

    fun removeFavourite(payload: RemoveFavouritePayload): Result<Unit, RemoveFavouriteError>
}
