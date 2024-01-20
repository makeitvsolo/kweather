package github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.configure

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.MongoForecastRepository
import github.makeitvsolo.kweather.weather.infrastructure.datasource.weather.mongo.error.MongoForecastRepositoryConfigurationError

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClients

class ConfigureMongoForecastRepository internal constructor(
    private var url: String? = null,
    private var database: String? = null,
    private var username: String? = null,
    private var password: String? = null
) {

    fun mongoUrl(url: String) = apply { this.url = url }
    fun mongoDatabase(database: String) = apply { this.database = database }
    fun username(username: String) = apply { this.username = username }
    fun password(password: String) = apply { this.password = password }

    fun configured(): Result<MongoForecastRepository, MongoForecastRepositoryConfigurationError> {
        val appliedUrl = url
        val appliedDatabase = database
        val appliedUsername = username
        val appliedPassword = password

        appliedUrl ?: return Result.error(
            MongoForecastRepositoryConfigurationError.MongoUrlError("missing mongo url")
        )

        appliedDatabase ?: return Result.error(
            MongoForecastRepositoryConfigurationError.MongoDatabaseError("missing mongo database")
        )

        appliedUsername ?: return Result.error(
            MongoForecastRepositoryConfigurationError.InvalidCredentialsError("missing username")
        )

        appliedPassword ?: return Result.error(
            MongoForecastRepositoryConfigurationError.InvalidCredentialsError("missing password")
        )

        val connectionString = ConnectionString(appliedUrl)
        val credentials = MongoCredential.createCredential(appliedUrl, appliedDatabase, appliedPassword.toCharArray())
        val settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
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
