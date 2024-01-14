package github.makeitvsolo.kweather.weather.api.datasource.location.operation

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.domain.location.Location

interface MapSearchLocationErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface SearchLocationError {

    fun <R, M : MapSearchLocationErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : SearchLocationError {

        override fun <R, M : MapSearchLocationErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)
    }

    data class InternalError(private val throwable: Throwable) : SearchLocationError {

        override fun <R, M : MapSearchLocationErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)
    }
}

interface SearchLocation {

    fun searchByName(accountId: String, name: String): Result<List<Location>, SearchLocationError>
}
