package github.makeitvsolo.kweather.user.access.integration.test.scenario

import github.makeitvsolo.kweather.user.access.api.service.dto.AccessTokenDto
import github.makeitvsolo.kweather.user.access.api.service.dto.UserDto
import github.makeitvsolo.kweather.user.access.api.service.usecase.AuthorizeUserError
import github.makeitvsolo.kweather.user.access.application.usecase.ApplicationAuthenticateUser
import github.makeitvsolo.kweather.user.access.application.usecase.ApplicationAuthorizeUser
import github.makeitvsolo.kweather.user.access.application.usecase.ApplicationRefreshAccessToken
import github.makeitvsolo.kweather.user.access.application.usecase.ApplicationRegisterUser
import github.makeitvsolo.kweather.user.access.integration.test.UserAccessIntegrationTest
import org.junit.jupiter.api.MethodOrderer

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthorizeAuthenticateUserScenarioTests : UserAccessIntegrationTest() {

    private val authorizeUsecase = ApplicationAuthorizeUser(
        repository, bcrypt, jwt
    )

    private val authenticateUsecase = ApplicationAuthenticateUser(
        jwt
    )

    private val refreshSessionUsecase = ApplicationRefreshAccessToken(
        jwt
    )

    private val registerUsecase = ApplicationRegisterUser(
        repository, bcrypt, unique
    )

    private val user = UserDto("name", "passwd")
    private val userWithInvalidPassword = UserDto("name", "invalidpasswd")

    @Test
    fun `at start user exists`() {
        repository.createTable().unwrap()
        registerUsecase.register(user)

        assertTrue(repository.findByName(user.name).isOk)
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
    inner class WhenUserExists {

        @Test
        @Order(1)
        fun `session for user can be created`() {
            val result = authorizeUsecase.authorize(user)

            val authorized = result.unwrap()

            assertTrue(result.isOk)
            assertEquals(user.name, authorized.user.name)
        }

        @Test
        @Order(2)
        fun `session for user with invalid password cannot be created`() {
            val result = authorizeUsecase.authorize(userWithInvalidPassword)

            assertTrue(result.isError)
            assertTrue(result.unwrapError() is AuthorizeUserError.InvalidCredentialsError)
        }

        @Nested
        @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
        inner class WhenSessionCreated {

            private lateinit var token: AccessTokenDto

            @BeforeTest
            fun beforeEach() {
                val authorized = authorizeUsecase.authorize(user).unwrap()
                token = authorized.token
            }

            @Test
            @Order(1)
            fun `user can be authenticated`() {
                val result = authenticateUsecase.authenticate(token)

                val activeUser = result.unwrap()

                assertTrue(result.isOk)
                assertEquals(user.name, activeUser.name)
            }

            @Test
            @Order(2)
            fun `session can be refreshed`() {
                val result = refreshSessionUsecase.refresh(token)

                assertTrue(result.isOk)
            }
        }
    }
}
