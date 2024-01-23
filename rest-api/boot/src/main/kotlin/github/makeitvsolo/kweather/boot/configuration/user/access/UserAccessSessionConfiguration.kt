package github.makeitvsolo.kweather.boot.configuration.user.access

import github.makeitvsolo.kweather.user.access.api.security.session.EncodeToken
import github.makeitvsolo.kweather.user.access.infrastructure.security.session.jwt.configure.ConfigureEncodeJwtToken

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
open class UserAccessSessionConfiguration(env: Environment) {

    private val algorithm: String = env.getProperty("jwt.algorithm") ?: ""
    private val accessSecret: String = env.getProperty("jwt.access-secret") ?: ""
    private val refreshSecret: String = env.getProperty("jwt.refresh-secret") ?: ""
    private val accessTtl: Long = env.getProperty("jwt.access-time-to-live", Long::class.java) ?: 0L
    private val refreshTtl: Long = env.getProperty("jwt.refresh-time-to-live", Long::class.java) ?: 0L

    @Bean
    open fun encodeToken(): EncodeToken =
        ConfigureEncodeJwtToken.with()
            .accessAlgorithm(algorithm)
            .refreshAlgorithm(algorithm)
            .accessSecretKey(accessSecret)
            .refreshSecretKey(refreshSecret)
            .accessTimeToLive(accessTtl)
            .refreshTimeToLive(refreshTtl)
            .configured()
            .unwrapOrElseThrow { it.intoThrowable() }
}
