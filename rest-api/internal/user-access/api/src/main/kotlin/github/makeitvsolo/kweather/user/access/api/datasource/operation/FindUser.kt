package github.makeitvsolo.kweather.user.access.api.datasource.operation

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.domain.User

interface MapFindUserErrorInto<out R> : Into<R> {

    fun fromNotFoundError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface FindUserError : IntoThrowable {

    fun <R, M : MapFindUserErrorInto<R>> into(map: M): R

    data class NotFoundError(private val details: String) : FindUserError {

        override fun <R, M : MapFindUserErrorInto<R>> into(map: M): R =
            map.fromNotFoundError(details)

        override fun intoThrowable(): Throwable = Throwable(details)
    }

    data class InternalError(private val throwable: Throwable) : FindUserError {

        override fun <R, M : MapFindUserErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)

        override fun intoThrowable(): Throwable = throwable
    }
}

interface FindUser {

    fun findByName(name: String): Result<User, FindUserError>
}
