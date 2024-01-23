package github.makeitvsolo.kweather.boot.configuration.mongo

import github.makeitvsolo.kweather.weather.infrastructure.configure.mongo.ConfigureMongoDatasource
import github.makeitvsolo.kweather.weather.infrastructure.configure.mongo.MongoDatasource

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
open class MongoConfiguration(env: Environment) {

    private val mongoHost: String = env.getProperty("mongo.host") ?: ""
    private val mongoPort: Int = env.getProperty("mongo.port", Int::class.java) ?: 0
    private val mongoDatabase: String = env.getProperty("mongo.database") ?: ""
    private val mongoAuthDatabase: String = env.getProperty("mongo.auth-database") ?: ""
    private val mongoUser: String = env.getProperty("mongo.username") ?: ""
    private val mongoPassword: String = env.getProperty("mongo.password") ?: ""

    @Bean
    open fun mongoDatasource(): MongoDatasource =
        ConfigureMongoDatasource.with()
            .host(mongoHost)
            .port(mongoPort)
            .database(mongoDatabase)
            .authDatabase(mongoAuthDatabase)
            .username(mongoUser)
            .password(mongoPassword)
            .configured()
            .unwrapOrElseThrow { it.intoThrowable() }
}
