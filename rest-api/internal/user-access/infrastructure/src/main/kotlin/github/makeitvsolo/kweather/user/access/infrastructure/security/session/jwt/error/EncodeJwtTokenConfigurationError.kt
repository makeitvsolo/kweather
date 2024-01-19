package github.makeitvsolo.kweather.user.access.infrastructure.security.session.jwt.error

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable

sealed interface EncodeJwtTokenConfigurationError : IntoThrowable {

    data class InvalidAlgorithmError internal constructor(
        private val details: String
    ) : EncodeJwtTokenConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class InvalidSecretKeyError internal constructor(
        private val details: String
    ) : EncodeJwtTokenConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }

    data class InvalidTimeToLiveError internal constructor(
        private val details: String
    ) : EncodeJwtTokenConfigurationError {

        override fun intoThrowable(): Throwable = IllegalArgumentException(details)
    }
}
