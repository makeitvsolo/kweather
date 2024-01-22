package github.makeitvsolo.kweather.boot.configuration.user.access

import github.makeitvsolo.kweather.core.type.Unique
import github.makeitvsolo.kweather.user.access.infrastructure.unique.UniqueId

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class UserAccessIdGenConfiguration {

    @Bean
    fun unique(): Unique<String> =
        UniqueId()
}
