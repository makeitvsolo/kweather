package github.makeitvsolo.kweather.weather.infrastructure.configure.mongo

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.weather.infrastructure.configure.mongo.error.ConfigureMongoDatasourceError

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoClients

class ConfigureMongoDatasource internal constructor(
    private var host: String? = null,
    private var port: Int? = null,
    private var database: String? = null,
    private var authDatabase: String? = null,
    private var username: String? = null,
    private var password: String? = null
) {

    fun host(host: String) = apply { this.host = host }
    fun port(port: Int) = apply { this.port = port }
    fun database(database: String) = apply { this.database = database }
    fun authDatabase(database: String) = apply { this.authDatabase = database }
    fun username(username: String) = apply { this.username = username }
    fun password(password: String) = apply { this.password = password }

    fun configured(): Result<MongoDatasource, ConfigureMongoDatasourceError> {
        val appliedHost = host
        val appliedPort = port
        val appliedDatabase = database
        val appliedAuthDatabase = authDatabase
        val appliedUsername = username
        val appliedPassword = password

        appliedHost ?: return Result.error(
            ConfigureMongoDatasourceError.MongoUrlError("missing mongo host")
        )

        appliedPort ?: return Result.error(
            ConfigureMongoDatasourceError.MongoUrlError("missing mongo port")
        )

        appliedDatabase ?: return Result.error(
            ConfigureMongoDatasourceError.MongoDatabaseError("missing mongo database")
        )

        appliedAuthDatabase ?: return Result.error(
            ConfigureMongoDatasourceError.InvalidCredentialsError("missing mongo auth database")
        )

        appliedUsername ?: return Result.error(
            ConfigureMongoDatasourceError.InvalidCredentialsError("missing username")
        )

        appliedPassword ?: return Result.error(
            ConfigureMongoDatasourceError.InvalidCredentialsError("missing password")
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
        return Result.ok(MongoDatasource(client.getDatabase(appliedDatabase)))
    }

    companion object {

        fun with(): ConfigureMongoDatasource = ConfigureMongoDatasource()
    }
}
