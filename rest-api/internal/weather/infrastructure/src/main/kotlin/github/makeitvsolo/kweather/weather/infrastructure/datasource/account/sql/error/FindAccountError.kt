package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.exception.AccountDoesNotExistsException

sealed interface FindAccountError : IntoThrowable {

    data class NotFoundError internal constructor(private val details: String) : FindAccountError {

        override fun intoThrowable(): Throwable = AccountDoesNotExistsException(details)
    }

    data class InternalError internal constructor(private val throwable: Throwable) : FindAccountError {

        override fun intoThrowable(): Throwable = throwable
    }
}
