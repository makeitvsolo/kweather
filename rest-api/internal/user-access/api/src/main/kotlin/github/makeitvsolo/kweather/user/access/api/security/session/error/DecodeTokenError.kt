package github.makeitvsolo.kweather.user.access.api.security.session.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.api.security.session.exception.DecodeTokenException

interface MapDecodeTokenErrorInto<out R> : Into<R> {

    fun fromInvalidTokenError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface DecodeTokenError : IntoThrowable {

    fun <R, M : MapDecodeTokenErrorInto<R>> into(map: M): R

    data class InvalidTokenError(private val details: String) : DecodeTokenError {

        override fun <R, M : MapDecodeTokenErrorInto<R>> into(map: M): R =
            map.fromInvalidTokenError(details)

        override fun intoThrowable(): Throwable = DecodeTokenException(details)
    }

    data class InternalError(private val throwable: Throwable) : DecodeTokenError {

        override fun <R, M : MapDecodeTokenErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}
