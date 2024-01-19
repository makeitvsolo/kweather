package github.makeitvsolo.kweather.user.access.infrastructure.security.hash.bcrypt.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

sealed interface BcryptHashConfigurationError : IntoThrowable {

    data class InvalidCostError internal constructor(
        private val details: String
    ) : BcryptHashConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class InvalidSaltError internal constructor(
        private val details: String
    ) : BcryptHashConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }
}
