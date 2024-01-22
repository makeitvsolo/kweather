package github.makeitvsolo.kweather.boot.configuration.jdbc

import github.makeitvsolo.kweather.weather.infrastructure.configure.sql.ConfigureJdbcDatasource

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

import javax.sql.DataSource

@Configuration
open class JdbcConfiguration(env: Environment) {

    private val jdbcUrl: String = env.getProperty("postgres.url") ?: ""
    private val jdbcUser: String = env.getProperty("postgres.username") ?: ""
    private val jdbcPassword: String = env.getProperty("postgres.password") ?: ""

    @Bean
    fun jdbcDatasource(): DataSource =
        ConfigureJdbcDatasource.with()
            .url(jdbcUrl)
            .username(jdbcUser)
            .password(jdbcPassword)
            .configured()
            .unwrapOrElseThrow { it.intoThrowable() }
}
