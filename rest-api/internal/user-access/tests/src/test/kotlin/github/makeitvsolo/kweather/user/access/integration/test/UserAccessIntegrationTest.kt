package github.makeitvsolo.kweather.user.access.integration.test

import github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.SqlUserRepository
import github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.configure.ConfigureSqlUserRepository
import github.makeitvsolo.kweather.user.access.infrastructure.security.hash.bcrypt.BcryptHash
import github.makeitvsolo.kweather.user.access.infrastructure.security.hash.bcrypt.configure.ConfigureBcryptHash
import github.makeitvsolo.kweather.user.access.infrastructure.security.session.jwt.EncodeJwtToken
import github.makeitvsolo.kweather.user.access.infrastructure.security.session.jwt.configure.ConfigureEncodeJwtToken
import github.makeitvsolo.kweather.user.access.infrastructure.unique.UniqueId

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

import java.time.Duration

@Testcontainers
abstract class UserAccessIntegrationTest {

    protected val unique: UniqueId = UniqueId()

    protected val bcrypt: BcryptHash = ConfigureBcryptHash.with()
        .cost(BcryptConfiguration.BCRYPT_COST)
        .salt(BcryptConfiguration.BCRYPT_SALT.toByteArray())
        .configured()
        .unwrap()

    protected val jwt: EncodeJwtToken = ConfigureEncodeJwtToken.with()
        .accessAlgorithm(JwtConfiguration.ALGORITHM)
        .refreshAlgorithm(JwtConfiguration.ALGORITHM)
        .accessSecretKey(JwtConfiguration.SECRET)
        .refreshSecretKey(JwtConfiguration.SECRET)
        .accessTimeToLive(JwtConfiguration.ACCESS_TIME_TO_LIVE)
        .refreshTimeToLive(JwtConfiguration.REFRESH_TIME_TO_LIVE)
        .configured()
        .unwrap()

    protected val repository: SqlUserRepository = ConfigureSqlUserRepository.with()
        .datasourceUrl(
            "jdbc:postgresql://${postgresContainer.host}:${
                postgresContainer.getMappedPort(
                    PostgresConfiguration.POSTGRES_PORT
                )
            }/${PostgresConfiguration.POSTGRES_DATABASE}"
        )
        .username(PostgresConfiguration.POSTGRES_USER)
        .password(PostgresConfiguration.POSTGRES_PASSWORD)
        .configured()
        .unwrap()

    object PostgresConfiguration {

        const val POSTGRES_IMAGE = "postgres:15"
        const val POSTGRES_PORT = 5432
        const val POSTGRES_DATABASE = "test"
        const val POSTGRES_USER = "testuser"
        const val POSTGRES_PASSWORD = "testpassword"
        const val POSTGRES_START_COMMAND = "postgres -c fsync=off"
        const val POSTGRES_HEALTH_LOG_MESSAGE = ".*database system is ready to accept connections.*\\s"
    }

    object BcryptConfiguration {

        const val BCRYPT_COST = 6
        const val BCRYPT_SALT = "supersecret_salt"
    }

    object JwtConfiguration {

        const val ALGORITHM = "hmac256"
        const val SECRET = "supersecret"
        const val ACCESS_TIME_TO_LIVE = 500L
        const val REFRESH_TIME_TO_LIVE = 3600L
    }

    companion object {

        @Container
        val postgresContainer = GenericContainer(
            DockerImageName.parse(PostgresConfiguration.POSTGRES_IMAGE)
        )
            .withExposedPorts(PostgresConfiguration.POSTGRES_PORT)
            .withEnv("POSTGRES_DB", PostgresConfiguration.POSTGRES_DATABASE)
            .withEnv("POSTGRES_USER", PostgresConfiguration.POSTGRES_USER)
            .withEnv("POSTGRES_PASSWORD", PostgresConfiguration.POSTGRES_PASSWORD)
            .withCommand(PostgresConfiguration.POSTGRES_START_COMMAND)
            .waitingFor(
                Wait.forLogMessage(PostgresConfiguration.POSTGRES_HEALTH_LOG_MESSAGE, 2)
                    .withStartupTimeout(Duration.ofSeconds(60))
            )
    }
}
