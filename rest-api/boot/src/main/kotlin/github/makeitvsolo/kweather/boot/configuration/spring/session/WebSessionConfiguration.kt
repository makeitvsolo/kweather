package github.makeitvsolo.kweather.boot.configuration.spring.session

import github.makeitvsolo.kweather.boot.configuration.spring.session.jwt.AuthenticatedArgumentResolver
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthenticateUser

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class WebSessionConfiguration(
    private val authenticateUsecase: AuthenticateUser
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(
            AuthenticatedArgumentResolver(authenticateUsecase)
        )
    }
}
