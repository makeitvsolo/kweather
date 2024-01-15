package github.makeitvsolo.kweather.user.access.application.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.security.session.DecodeTokenError
import github.makeitvsolo.kweather.user.access.api.security.session.EncodeToken
import github.makeitvsolo.kweather.user.access.api.security.session.Token
import github.makeitvsolo.kweather.user.access.api.security.session.TokenPayload
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthenticateUserError
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthenticateUserPayload
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthenticateUserResponse
import github.makeitvsolo.kweather.user.access.application.ApplicationUnitTest

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationAuthenticateUserTests : ApplicationUnitTest() {

    private val token: Token = Token("access token", "refresh token")
    private val invalidToken: Token = Token("invalid access token", "invalid refresh token")
    private val tokenPayload: TokenPayload = TokenPayload("id", "name")

    @Mock
    private lateinit var session: EncodeToken

    @InjectMocks
    private lateinit var usecase: ApplicationAuthenticateUser

    @Test
    fun `authenticate returns active user`() {
        val payload = AuthenticateUserPayload("access token", "refresh token")
        val expected = AuthenticateUserResponse("id", "name")

        whenever(session.decode(token))
            .thenReturn(Result.ok(tokenPayload))

        val result = usecase.authenticate(payload)

        assertTrue(result.isOk)
        assertEquals(expected, result.unwrap())
    }

    @Test
    fun `authenticate returns invalid token error when token is invalid`() {
        val payload = AuthenticateUserPayload("invalid access token", "invalid refresh token")
        val errorMessage = "invalid token"
        val expected = AuthenticateUserError.InvalidTokenError(errorMessage)

        whenever(session.decode(invalidToken))
            .thenReturn(Result.error(DecodeTokenError.InvalidTokenError(errorMessage)))

        val result = usecase.authenticate(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `authenticate returns internal error`() {
        val payload = AuthenticateUserPayload("access token", "refresh token")
        val exception = Throwable("internal error")
        val expected = AuthenticateUserError.InternalError(exception)

        whenever(session.decode(token))
            .thenReturn(Result.error(DecodeTokenError.InternalError(exception)))

        val result = usecase.authenticate(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }
}
