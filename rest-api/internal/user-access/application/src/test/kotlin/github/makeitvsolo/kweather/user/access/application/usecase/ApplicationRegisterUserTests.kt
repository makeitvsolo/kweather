package github.makeitvsolo.kweather.user.access.application.usecase

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.type.Unique
import github.makeitvsolo.kweather.user.access.api.datasource.UserRepository
import github.makeitvsolo.kweather.user.access.api.datasource.operation.SaveUserError
import github.makeitvsolo.kweather.user.access.api.security.hash.Hash
import github.makeitvsolo.kweather.user.access.api.service.usecase.RegisterUserError
import github.makeitvsolo.kweather.user.access.api.service.usecase.RegisterUserPayload
import github.makeitvsolo.kweather.user.access.application.ApplicationUnitTest

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationRegisterUserTests : ApplicationUnitTest() {

    @Mock
    private lateinit var repository: UserRepository

    @Mock
    private lateinit var encode: Hash

    @Mock
    private lateinit var userId: Unique<String>

    @InjectMocks
    private lateinit var usecase: ApplicationRegisterUser

    @Test
    fun `register saves new user`() {
        val payload = RegisterUserPayload("name", "passwd")
        val expected = Unit

        whenever(userId.unique())
            .thenReturn("unique id")
        whenever(encode.hash(payload.password))
            .thenReturn("hashed")
        whenever(repository.save(any()))
            .thenReturn(Result.ok(expected))

        val result = usecase.register(payload)

        verify(encode).hash(payload.password)

        assertTrue(result.isOk)
        assertEquals(expected, result.unwrap())
    }

    @Test
    fun `register returns conflict error when user already exists`() {
        val payload = RegisterUserPayload("name", "passwd")
        val errorMessage = "user already exists"
        val expected = RegisterUserError.ConflictError(errorMessage)

        whenever(userId.unique())
            .thenReturn("unique id")
        whenever(encode.hash(payload.password))
            .thenReturn("hashed")
        whenever(repository.save(any()))
            .thenReturn(Result.error(SaveUserError.ConflictError(errorMessage)))

        val result = usecase.register(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }

    @Test
    fun `register returns internal error`() {
        val payload = RegisterUserPayload("name", "passwd")
        val exception = Throwable("internal error")
        val expected = RegisterUserError.InternalError(exception)

        whenever(userId.unique())
            .thenReturn("unique id")
        whenever(encode.hash(payload.password))
            .thenReturn("hashed")
        whenever(repository.save(any()))
            .thenReturn(Result.error(SaveUserError.InternalError(exception)))

        val result = usecase.register(payload)

        assertTrue(result.isError)
        assertEquals(expected, result.unwrapError())
    }
}
