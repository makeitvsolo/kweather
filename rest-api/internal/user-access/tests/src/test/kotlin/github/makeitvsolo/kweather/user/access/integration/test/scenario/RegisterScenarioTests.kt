package github.makeitvsolo.kweather.user.access.integration.test.scenario

import github.makeitvsolo.kweather.user.access.api.datasource.operation.FindUserError
import github.makeitvsolo.kweather.user.access.api.service.usecase.RegisterUserError
import github.makeitvsolo.kweather.user.access.api.service.usecase.RegisterUserPayload
import github.makeitvsolo.kweather.user.access.application.usecase.ApplicationRegisterUser
import github.makeitvsolo.kweather.user.access.integration.test.UserAccessIntegrationTest
import org.junit.jupiter.api.Nested

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RegisterScenarioTests : UserAccessIntegrationTest() {

    private val registerUsecase = ApplicationRegisterUser(
        repository, bcrypt, unique
    )

    private val user = RegisterUserPayload("name", "passwd")
    private val userWithSameName = RegisterUserPayload("name", "otherpasswd")

    @Test
    fun `at start user does not exists`() {
        repository.createTable().unwrap()

        val result = repository.findByName(user.name)
        assertTrue(result.isError)
        assertTrue(result.unwrapError() is FindUserError.NotFoundError)
    }

    @Nested
    inner class WhenUserDoesNotExists {

        @Test
        fun `user can be registered`() {
            val expected = Unit

            val result = registerUsecase.register(user)

            assertTrue(result.isOk)
            assertEquals(expected, result.unwrap())
        }

        @Nested
        inner class WhenUserRegistered {

            @Test
            fun `user with same name cannot be registered again`() {
                val result = registerUsecase.register(userWithSameName)

                assertTrue(result.isError)
                assertTrue(result.unwrapError() is RegisterUserError.ConflictError)
            }
        }
    }
}
