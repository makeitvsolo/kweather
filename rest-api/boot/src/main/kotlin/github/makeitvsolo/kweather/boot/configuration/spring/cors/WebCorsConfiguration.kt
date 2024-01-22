package github.makeitvsolo.kweather.boot.configuration.spring.cors

import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class WebCorsConfiguration(env: Environment) : WebMvcConfigurer {

    private val origins: List<String> = env.getProperty("web.cors.allowed-origins")
        ?.split(",")
        ?.toList() ?: listOf()
    private val headers: List<String> = env.getProperty("web.cors.allowed-headers")
        ?.split(",")
        ?.toList() ?: listOf()
    private val methods: List<String> = env.getProperty("web.cors.allowed-methods")
        ?.split(",")
        ?.toList() ?: listOf()

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*origins.toTypedArray())
            .allowedHeaders(*headers.toTypedArray())
            .allowedMethods(*methods.toTypedArray())
    }
}
