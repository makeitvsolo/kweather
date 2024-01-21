package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.MongoForecastRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.error.MongoForecastRepositoryConfigurationError

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoClients

class ConfigureMongoForecastRepository internal constructor(
    private var host: String? = null,
    private var port: Int? = null,
    private var database: String? = null,
    private var authDatabase: String? = null,
    private var username: String? = null,
    private var password: String? = null
) {

    fun mongoHost(host: String) = apply { this.host = host }
    fun mongoPort(port: Int) = apply { this.port = port }
    fun database(database: String) = apply { this.database = database }
    fun authDatabase(database: String) = apply { this.authDatabase = database }
    fun username(username: String) = apply { this.username = username }
    fun password(password: String) = apply { this.password = password }

    fun configured(): Result<MongoForecastRepository, MongoForecastRepositoryConfigurationError> {
        val appliedHost = host
        val appliedPort = port
        val appliedDatabase = database
        val appliedAuthDatabase = authDatabase
        val appliedUsername = username
        val appliedPassword = password

        appliedHost ?: return Result.error(
            MongoForecastRepositoryConfigurationError.MongoUrlError("missing mongo host")
        )

        appliedPort ?: return Result.error(
            MongoForecastRepositoryConfigurationError.MongoUrlError("missing mongo port")
        )

        appliedDatabase ?: return Result.error(
            MongoForecastRepositoryConfigurationError.MongoDatabaseError("missing mongo database")
        )

        appliedAuthDatabase ?: return Result.error(
            MongoForecastRepositoryConfigurationError.InvalidCredentialsError("missing mongo auth database")
        )

        appliedUsername ?: return Result.error(
            MongoForecastRepositoryConfigurationError.InvalidCredentialsError("missing username")
        )

        appliedPassword ?: return Result.error(
            MongoForecastRepositoryConfigurationError.InvalidCredentialsError("missing password")
        )

        val address = ServerAddress(appliedHost, appliedPort)
        val credentials = MongoCredential.createCredential(
            appliedUsername, appliedAuthDatabase, appliedPassword.toCharArray()
        )

        val settings = MongoClientSettings.builder()
            .applyToClusterSettings { it.hosts(listOf(address)) }
            .credential(credentials)
            .build()

        val client = MongoClients.create(settings)
        val database = client.getDatabase(appliedDatabase)

        return Result.ok(
            MongoForecastRepository(database)
        )
    }

    companion object {

        fun with(): ConfigureMongoForecastRepository = ConfigureMongoForecastRepository()
    }
}
