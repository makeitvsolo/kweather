package github.makeitvsolo.kweather.user.access.application.user.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.datasource.user.UserRepository
import github.makeitvsolo.kweather.user.access.api.datasource.user.error.FindUserError
import github.makeitvsolo.kweather.user.access.api.security.hash.Hash
import github.makeitvsolo.kweather.user.access.api.security.session.EncodeToken
import github.makeitvsolo.kweather.user.access.api.security.session.Token
import github.makeitvsolo.kweather.user.access.api.service.user.dto.AccessTokenDto
import github.makeitvsolo.kweather.user.access.api.service.user.dto.ActiveUserDto
import github.makeitvsolo.kweather.user.access.api.service.user.error.AuthorizeUserError
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthorizeUserPayload
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthorizeUserResponse
import github.makeitvsolo.kweather.user.access.application.ApplicationUnitTest
import github.makeitvsolo.kweather.user.access.domain.User

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.whenever

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationAuthorizeUserTests : ApplicationUnitTest() {

    private val existingUser: User = User.from("id", "name", "passwd")
    private val token: Token = Token("access token", "refresh token")

    @Mock
    private lateinit var repository: UserRepository

    @Mock
    private lateinit var encode: Hash

    @Mock
    private lateinit var session: EncodeToken

    @InjectMocks
    private lateinit var usecase: ApplicationAuthorizeUser

    @Test
    fun `authorize returns authorized user`() {
        val payload = AuthorizeUserPayload("name", "passwd")
        val expected = AuthorizeUserResponse(
            AccessTokenDto("access token", "refresh token"),
            ActiveUserDto("id", "name")
        )

        whenever(repository.findByName(payload.name))
            .thenReturn(Result.ok(existingUser))
        whenever(encode.hash(payload.password))
            .thenReturn(payload.password)
        whenever(session.encode(existingUser))
            .thenReturn(token)

        val result = usecase.authorize(payload)

        assertTrue(result.isOk)
        assertEquals(expected, result.unwrap())
    }

    @Test
    fun `authorize returns not found error when user does not exists`() {
        val payload = AuthorizeUserPayload("name", "passwd")
        val errorMessage = "user does not exists"
        val expected = AuthorizeUserError.NotFoundError(errorMessage)

        whenever(repository.findByName(payload.name))
            .thenReturn(Result.error(FindUserError.NotFoundError(errorMessage)))

        val result = usecase.authorize(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `authorize returns invalid credentials error when credentials invalid`() {
        val payload = AuthorizeUserPayload("name", "invalid passwd")
        val expected = AuthorizeUserError.InvalidCredentialsError()

        whenever(repository.findByName(payload.name))
            .thenReturn(Result.ok(existingUser))
        whenever(encode.hash(payload.password))
            .thenReturn(payload.password)

        val result = usecase.authorize(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `authorize returns internal error`() {
        val payload = AuthorizeUserPayload("name", "passwd")
        val exception = Throwable("internal error")
        val expected = AuthorizeUserError.InternalError(exception)

        whenever(repository.findByName(payload.name))
            .thenReturn(Result.error(FindUserError.InternalError(exception)))

        val result = usecase.authorize(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }
}
