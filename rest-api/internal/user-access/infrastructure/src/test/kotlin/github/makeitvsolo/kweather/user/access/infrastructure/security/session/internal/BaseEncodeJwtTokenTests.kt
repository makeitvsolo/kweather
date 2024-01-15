package github.makeitvsolo.kweather.user.access.infrastructure.security.session.internal

import github.makeitvsolo.kweather.user.access.api.security.session.TokenPayload
import github.makeitvsolo.kweather.user.access.infrastructure.InfrastructureUnitTest

import com.auth0.jwt.algorithms.Algorithm
import github.makeitvsolo.kweather.user.access.api.security.session.DecodeTokenError

import kotlin.test.Test
import kotlin.test.assertEquals

import java.time.Instant
import kotlin.test.assertTrue

class BaseEncodeJwtTokenTests : InfrastructureUnitTest() {

    private val jwt: BaseEncodeJwtToken = BaseEncodeJwtToken(
        Algorithm.HMAC256(SECRET),
        TIME_TO_LIVE
    )

    @Test
    fun `raw and decoded payloads are equals`() {
        val raw = TokenPayload("id", "name")
        val now = Instant.now()
        val encoded = jwt.encode(raw, now)

        val result = jwt.decode(encoded)

        assertTrue(result.isOk)
        assertEquals(raw, result.unwrap())
    }

    @Test
    fun `decode returns error when token is expired`() {
        val raw = TokenPayload("id", "name")
        val now = Instant.now().minusSeconds(TIME_TO_LIVE)
        val encoded = jwt.encode(raw, now)

        val result = jwt.decode(encoded)

        assertTrue(result.isError)
        assertTrue(result.unwrapError() is DecodeTokenError.InvalidTokenError)
    }

    @Test
    fun `decode returns error when token is invalid`() {
        val invalidToken = "invalid token"

        val result = jwt.decode(invalidToken)

        assertTrue(result.isError)
        assertTrue(result.unwrapError() is DecodeTokenError.InvalidTokenError)
    }

    companion object {

        const val SECRET = "supersecret"
        const val TIME_TO_LIVE = 300L
    }
}
