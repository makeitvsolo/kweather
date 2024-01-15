package github.makeitvsolo.kweather.user.access.infrastructure.security.session.internal

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.security.session.DecodeTokenError
import github.makeitvsolo.kweather.user.access.api.security.session.TokenPayload

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException

import java.time.Instant

internal class BaseEncodeJwtToken internal constructor(
    private val algorithm: Algorithm,
    private val timeToLive: Long
) {

    fun encode(payload: TokenPayload, now: Instant): String {
        val token = JWT.create()
            .withSubject(payload.userId)
            .withClaim(USER_NAME_CLAIM, payload.userName)
            .withIssuedAt(now)
            .withExpiresAt(now.plusSeconds(timeToLive))
            .sign(algorithm)

        return token
    }

    fun decode(token: String): Result<TokenPayload, DecodeTokenError> {
        try {
            val decoded = JWT.require(algorithm)
                .withClaimPresence(USER_NAME_CLAIM)
                .build()
                .verify(token)

            return Result.ok(
                TokenPayload(
                    decoded.subject,
                    decoded.getClaim(USER_NAME_CLAIM).asString()
                )
            )
        } catch (ex: JWTVerificationException) {
            return Result.error(
                DecodeTokenError.InvalidTokenError(ex.message ?: DEFAULT_DECODE_ERROR)
            )
        } catch (throwable: Throwable) {
            return Result.error(DecodeTokenError.InternalError(throwable))
        }
    }

    companion object {

        private const val DEFAULT_DECODE_ERROR = "invalid token"
        private const val USER_NAME_CLAIM = "name"
    }
}
