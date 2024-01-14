package github.makeitvsolo.kweather.weather.api.datasource.location.operation

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into

import java.math.BigDecimal

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

    fun removeFavourite(
        accountId: String,
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<Unit, RemoveFavouriteError>
}
