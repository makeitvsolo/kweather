package github.makeitvsolo.kweather.weather.api.service.location.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.weather.api.datasource.location.operation.MapSearchLocationErrorInto
import github.makeitvsolo.kweather.weather.api.service.location.dto.LocationDto
import github.makeitvsolo.kweather.weather.api.service.location.dto.SearchLocationDto

typealias SearchForLocationPayload = SearchLocationDto
typealias SearchForLocationResponse = List<LocationDto>

interface MapSearchForLocationErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface SearchForLocationError {

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
    }

    data class InternalError(private val throwable: Throwable) : SearchForLocationError {

        override fun <R, M : MapSearchForLocationErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)
    }
}

interface SearchForLocation {

    fun search(payload: SearchForLocationPayload): Result<SearchForLocationResponse, SearchForLocationError>
}
