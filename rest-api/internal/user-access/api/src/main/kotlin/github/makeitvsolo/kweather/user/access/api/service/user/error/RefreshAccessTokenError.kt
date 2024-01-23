package github.makeitvsolo.kweather.user.access.api.service.user.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.api.security.session.error.MapDecodeTokenErrorInto
import github.makeitvsolo.kweather.user.access.api.service.user.exception.UserServiceException

interface MapRefreshAccessTokenErrorInto<out R> : Into<R> {

    fun fromInvalidTokenError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface RefreshAccessTokenError : IntoThrowable {

    object FromDecodeTokenError : MapDecodeTokenErrorInto<RefreshAccessTokenError> {

        override fun fromInvalidTokenError(details: String): RefreshAccessTokenError =
            InvalidTokenError(details)

        override fun fromInternalError(throwable: Throwable): RefreshAccessTokenError =
            InternalError(throwable)
    }

    fun <R, M : MapRefreshAccessTokenErrorInto<R>> into(map: M): R

    data class InvalidTokenError(private val details: String) : RefreshAccessTokenError {

        override fun <R, M : MapRefreshAccessTokenErrorInto<R>> into(map: M): R =
            map.fromInvalidTokenError(details)

        override fun intoThrowable(): Throwable = UserServiceException(details)
    }

    data class InternalError(private val throwable: Throwable) : RefreshAccessTokenError {

        override fun <R, M : MapRefreshAccessTokenErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
