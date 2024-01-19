package github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.exception.AccountAlreadyExistsException

sealed interface SaveAccountError : IntoThrowable {

    data class ConflictError internal constructor(private val details: String) : SaveAccountError {

        override fun intoThrowable(): Throwable = AccountAlreadyExistsException(details)
    }

    data class InternalError internal constructor(private val throwable: Throwable) : SaveAccountError {

        override fun intoThrowable(): Throwable = throwable
    }
}
