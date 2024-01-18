package github.makeitvsolo.kweather.weather.api.datasource.location.operation

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.error.handling.Result

import java.math.BigDecimal

interface MapAddFavouriteErrorInto<out R> {

    fun fromNotFoundError(details: String): R
    fun fromConflictError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface AddFavouriteError : IntoThrowable {

    fun <R, M : MapAddFavouriteErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : AddFavouriteError {

        override fun <R, M : MapAddFavouriteErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = Throwable(details)
    }

    data class ConflictError(private val details: String) : AddFavouriteError {

        override fun <R, M : MapAddFavouriteErrorInto<R>> into(map: M): R =
            map.fromConflictError(details)

        override fun intoThrowable(): Throwable = Throwable(details)
    }

    data class InternalError(private val throwable: Throwable) : AddFavouriteError {

        override fun <R, M : MapAddFavouriteErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}

interface AddFavourite {

    fun addFavourite(
        accountId: String,
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Result<Unit, AddFavouriteError>
}
