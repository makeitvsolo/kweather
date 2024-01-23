package github.makeitvsolo.kweather.user.access.application.user.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.security.session.EncodeToken
import github.makeitvsolo.kweather.user.access.api.security.session.Token
import github.makeitvsolo.kweather.user.access.api.security.session.error.DecodeTokenError
import github.makeitvsolo.kweather.user.access.api.service.user.error.RefreshAccessTokenError
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RefreshAccessTokenPayload
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.RefreshAccessTokenResponse
import github.makeitvsolo.kweather.user.access.application.ApplicationUnitTest

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.whenever

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationRefreshAccessTokenTests : ApplicationUnitTest() {

    private val refreshedToken: Token = Token("refreshed access token", "refreshed refresh token")

    @Mock
    private lateinit var session: EncodeToken

    @InjectMocks
    private lateinit var usecase: ApplicationRefreshAccessToken

    @Test
    fun `refresh returns updated access token`() {
        val payload = RefreshAccessTokenPayload("refresh token")
        val expected = RefreshAccessTokenResponse("refreshed access token", "refreshed refresh token")

        whenever(session.refresh(payload.token))
            .thenReturn(Result.ok(refreshedToken))

        val result = usecase.refresh(payload)

        assertTrue(result.isOk)
        assertEquals(expected, result.unwrap())
    }

    @Test
    fun `refresh returns invalid token error when refresh token invalid`() {
        val payload = RefreshAccessTokenPayload("invalid refresh token")
        val errorMessage = "invalid token"
        val expected = RefreshAccessTokenError.InvalidTokenError(errorMessage)

        whenever(session.refresh(payload.token))
            .thenReturn(Result.error(DecodeTokenError.InvalidTokenError(errorMessage)))

        val result = usecase.refresh(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `refresh returns internal error`() {
        val payload = RefreshAccessTokenPayload("refresh token")
        val exception = Throwable("internal error")
        val expected = RefreshAccessTokenError.InternalError(exception)

        whenever(session.refresh(payload.token))
            .thenReturn(Result.error(DecodeTokenError.InternalError(exception)))

        val result = usecase.refresh(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }
}
