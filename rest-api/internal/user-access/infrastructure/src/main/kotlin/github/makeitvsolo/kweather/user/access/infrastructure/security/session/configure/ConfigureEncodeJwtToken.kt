package github.makeitvsolo.kweather.user.access.infrastructure.security.session.configure

import github.makeitvsolo.kweather.core.error.handling.IntoThrowable
import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.infrastructure.security.session.EncodeJwtToken
import github.makeitvsolo.kweather.user.access.infrastructure.security.session.internal.BaseEncodeJwtToken

import com.auth0.jwt.algorithms.Algorithm

sealed interface EncodeJwtTokenConfigurationError : IntoThrowable {

    data class InvalidAlgorithmError(private val details: String) : EncodeJwtTokenConfigurationError {

        override fun intoThrowable(): Throwable = Throwable(details)
    }

    data class InvalidSecretKeyError(private val details: String) : EncodeJwtTokenConfigurationError {

        override fun intoThrowable(): Throwable = Throwable(details)
    }

    data class InvalidTimeToLiveError(private val details: String) : EncodeJwtTokenConfigurationError {

        override fun intoThrowable(): Throwable = Throwable(details)
    }
}

class ConfigureEncodeJwtToken internal constructor(
    private var accessConfiguration: TokenConfiguration = TokenConfiguration(),
    private var refreshConfiguration: TokenConfiguration = TokenConfiguration()
) {

    object EncodeAlgorithm {

        const val HMACH256 = "hmac256"
        const val HMACK384 = "hmack384"
        const val HMACK512 = "hmack512"
    }

    fun accessAlgorithm(name: String) =
        apply { this.accessConfiguration = accessConfiguration.copy(algorithm = name) }

    fun refreshAlgorithm(name: String) =
        apply { this.refreshConfiguration = refreshConfiguration.copy(algorithm = name) }

    fun accessSecretKey(key: String) =
        apply { this.accessConfiguration = accessConfiguration.copy(secretKey = key) }

    fun refreshSecretKey(key: String) =
        apply { this.refreshConfiguration = refreshConfiguration.copy(secretKey = key) }

    fun accessTimeToLive(seconds: Long) =
        apply { this.accessConfiguration = accessConfiguration.copy(timeToLive = seconds) }

    fun refreshTimeToLive(seconds: Long) =
        apply { this.refreshConfiguration = refreshConfiguration.copy(timeToLive = seconds) }

    fun configured(): Result<EncodeJwtToken, EncodeJwtTokenConfigurationError> {
        val appliedAccessConfiguration = accessConfiguration.copy()
        val appliedRefreshConfiguration = refreshConfiguration.copy()

        appliedAccessConfiguration.algorithm ?: return Result.error(
            EncodeJwtTokenConfigurationError.InvalidAlgorithmError("missing access algorithm")
        )

        appliedRefreshConfiguration.algorithm ?: return Result.error(
            EncodeJwtTokenConfigurationError.InvalidAlgorithmError("missing refresh algorithm")
        )

        appliedAccessConfiguration.secretKey ?: return Result.error(
            EncodeJwtTokenConfigurationError.InvalidSecretKeyError("missing access secret key")
        )

        appliedRefreshConfiguration.secretKey ?: return Result.error(
            EncodeJwtTokenConfigurationError.InvalidSecretKeyError("missing refresh secret key")
        )

        appliedAccessConfiguration.timeToLive ?: return Result.error(
            EncodeJwtTokenConfigurationError.InvalidTimeToLiveError("missing access time to live")
        )

        appliedRefreshConfiguration.timeToLive ?: return Result.error(
            EncodeJwtTokenConfigurationError.InvalidTimeToLiveError("missing refresh time to live")
        )

        val accessAlgorithm = when (appliedAccessConfiguration.algorithm) {
            EncodeAlgorithm.HMACH256 -> Algorithm.HMAC256(appliedAccessConfiguration.secretKey)
            EncodeAlgorithm.HMACK384 -> Algorithm.HMAC384(appliedAccessConfiguration.secretKey)
            EncodeAlgorithm.HMACK512 -> Algorithm.HMAC512(appliedAccessConfiguration.secretKey)
            else -> return Result.error(
                EncodeJwtTokenConfigurationError.InvalidAlgorithmError("unsupported access algorithm")
            )
        }

        val refreshAlgorithm = when (appliedRefreshConfiguration.algorithm) {
            EncodeAlgorithm.HMACH256 -> Algorithm.HMAC256(appliedRefreshConfiguration.secretKey)
            EncodeAlgorithm.HMACK384 -> Algorithm.HMAC384(appliedRefreshConfiguration.secretKey)
            EncodeAlgorithm.HMACK512 -> Algorithm.HMAC512(appliedRefreshConfiguration.secretKey)
            else -> return Result.error(
                EncodeJwtTokenConfigurationError.InvalidAlgorithmError("unsupported refresh algorithm")
            )
        }

        if (appliedAccessConfiguration.timeToLive <= ZERO_TIME_TO_LIVE) {
            return Result.error(
                EncodeJwtTokenConfigurationError.InvalidTimeToLiveError("access time to live must be `> 0`")
            )
        }

        if (appliedRefreshConfiguration.timeToLive <= ZERO_TIME_TO_LIVE) {
            return Result.error(
                EncodeJwtTokenConfigurationError.InvalidTimeToLiveError("refresh time to live must be `> 0`")
            )
        }

        return Result.ok(
            EncodeJwtToken(
                access = BaseEncodeJwtToken(accessAlgorithm, appliedAccessConfiguration.timeToLive),
                refresh = BaseEncodeJwtToken(refreshAlgorithm, appliedRefreshConfiguration.timeToLive)
            )
        )
    }

    companion object {

        private const val ZERO_TIME_TO_LIVE = 0

        fun with(): ConfigureEncodeJwtToken = ConfigureEncodeJwtToken()
    }
}

internal data class TokenConfiguration(
    val algorithm: String? = null,
    val secretKey: String? = null,
    val timeToLive: Long? = null,
)
