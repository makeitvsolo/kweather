package github.makeitvsolo.kweather.boot.configuration.user.access

import github.makeitvsolo.kweather.user.access.api.security.hash.Hash
import github.makeitvsolo.kweather.user.access.infrastructure.security.hash.bcrypt.configure.ConfigureBcryptHash

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
open class UserAccessEncodingConfiguration(env: Environment) {

    private val cost: Int = env.getProperty("encoding.cost", Int::class.java) ?: 0
    private val salt: String = env.getProperty("encoding.salt") ?: ""

    @Bean
    fun hash(): Hash =
        ConfigureBcryptHash.with()
            .cost(cost)
            .salt(salt.toByteArray())
            .configured()
            .unwrapOrElseThrow { it.intoThrowable() }
}
