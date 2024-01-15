package github.makeitvsolo.kweather.user.access.application.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.security.session.DecodeTokenError
import github.makeitvsolo.kweather.user.access.api.security.session.EncodeToken
import github.makeitvsolo.kweather.user.access.api.security.session.Token
import github.makeitvsolo.kweather.user.access.api.service.usecase.RefreshAccessTokenError
import github.makeitvsolo.kweather.user.access.api.service.usecase.RefreshAccessTokenPayload
import github.makeitvsolo.kweather.user.access.api.service.usecase.RefreshAccessTokenResponse
import github.makeitvsolo.kweather.user.access.application.ApplicationUnitTest
import org.mockito.InjectMocks

import org.mockito.Mock
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationRefreshAccessTokenTests : ApplicationUnitTest() {

    private val token: Token = Token("access token", "refresh token")
    private val invalidToken: Token = Token("invalid access token", "invalid refresh token")
    private val refreshedToken: Token = Token("refreshed access token", "refreshed refresh token")

    @Mock
    private lateinit var session: EncodeToken

    @InjectMocks
    private lateinit var usecase: ApplicationRefreshAccessToken

    @Test
    fun `refresh returns updated access token`() {
        val payload = RefreshAccessTokenPayload("access token", "refresh token")
        val expected = RefreshAccessTokenResponse("refreshed access token", "refreshed refresh token")

        whenever(session.refresh(token))
            .thenReturn(Result.ok(refreshedToken))

        val result = usecase.refresh(payload)

        assertTrue(result.isOk)
        assertEquals(expected, result.unwrap())
    }

    @Test
    fun `refresh returns invalid token error when refresh token invalid`() {
        val payload = RefreshAccessTokenPayload("invalid access token", "invalid refresh token")
        val errorMessage = "invalid token"
        val expected = RefreshAccessTokenError.InvalidTokenError(errorMessage)

        whenever(session.refresh(invalidToken))
            .thenReturn(Result.error(DecodeTokenError.InvalidTokenError(errorMessage)))

        val result = usecase.refresh(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `refresh returns internal error`() {
        val payload = RefreshAccessTokenPayload("access token", "refresh token")
        val exception = Throwable("internal error")
        val expected = RefreshAccessTokenError.InternalError(exception)

        whenever(session.refresh(token))
            .thenReturn(Result.error(DecodeTokenError.InternalError(exception)))

        val result = usecase.refresh(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }
}