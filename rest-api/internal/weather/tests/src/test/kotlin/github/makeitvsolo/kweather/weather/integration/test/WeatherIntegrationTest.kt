package github.makeitvsolo.kweather.weather.integration.test

import github.makeitvsolo.kweather.weather.infrastructure.datasource.account.sql.configure.ConfigureSqlAccountRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.location.base.configure.ConfigureBaseLocationRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.cache.configure.ConfigureCachedWeatherRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.configure.ConfigureMongoForecastRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.weatherapi.configure.ConfigureWeatherApiWeatherRepository
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.Duration

@Testcontainers
abstract class WeatherIntegrationTest {

    protected val accountRepository = ConfigureSqlAccountRepository.with()
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

    protected val locationRepository = ConfigureBaseLocationRepository.with()
        .datasourceUrl(
            "jdbc:postgresql://${postgresContainer.host}:${
                postgresContainer.getMappedPort(
                    PostgresConfiguration.POSTGRES_PORT
                )
            }/${PostgresConfiguration.POSTGRES_DATABASE}"
        )
        .username(PostgresConfiguration.POSTGRES_USER)
        .password(PostgresConfiguration.POSTGRES_PASSWORD)
        .apiKey(WeatherApiConfiguration.WEATHER_API_KEY)
        .configured()
        .unwrap()

    protected val weatherApiWeatherRepository = ConfigureWeatherApiWeatherRepository.with()
        .apiKey(WeatherApiConfiguration.WEATHER_API_KEY)
        .forecastDays(WeatherApiConfiguration.FORECAST_DAYS)
        .configured()
        .unwrap()

    protected val mongoForecastRepository = ConfigureMongoForecastRepository.with()
        .mongoUrl("mongodb://${mongoContainer.host}:${mongoContainer.getMappedPort(MongoConfiguration.MONGO_PORT)}")
        .mongoDatabase(MongoConfiguration.MONGO_DATABASE)
        .username(MongoConfiguration.MONGO_USER)
        .password(MongoConfiguration.MONGO_PASSWORD)
        .configured()
        .unwrap()

    protected val weatherRepository = ConfigureCachedWeatherRepository.with()
        .base(weatherApiWeatherRepository)
        .cache(mongoForecastRepository)
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

    object WeatherApiConfiguration {

        const val WEATHER_API_KEY = "e99433e012be4f73ab9133834241101"
        const val FORECAST_DAYS = 2
    }

    object MongoConfiguration {

        const val MONGO_IMAGE = "mongo:4.0.10"
        const val MONGO_PORT = 27017
        const val MONGO_DATABASE = "test"
        const val MONGO_USER = "testuser"
        const val MONGO_PASSWORD = "testpassword"
        const val MONGO_START_COMMAND = "--replSet docker-rs"
        const val MONGO_HEALTH_LOG_MESSAGE = ".*waiting for connections.*"
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

        @Container
        val mongoContainer = GenericContainer(
            DockerImageName.parse(MongoConfiguration.MONGO_IMAGE)
        )
            .withExposedPorts(MongoConfiguration.MONGO_PORT)
            .withCommand(MongoConfiguration.MONGO_START_COMMAND)
            .waitingFor(
                Wait.forLogMessage(MongoConfiguration.MONGO_HEALTH_LOG_MESSAGE, 1)
                    .withStartupTimeout(Duration.ofSeconds(60))
            )
    }
}
