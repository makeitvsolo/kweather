package github.makeitvsolo.kweather.boot.configuration.spring.session.jwt

import github.makeitvsolo.kweather.boot.configuration.spring.session.exception.UnauthorizedException
import github.makeitvsolo.kweather.user.access.api.service.user.dto.TokenDto
import github.makeitvsolo.kweather.user.access.api.service.user.usecase.AuthenticateUser

import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class AuthenticatedArgumentResolver(
    private val authenticateUsecase: AuthenticateUser
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.getParameterAnnotation(Authenticated::class.java) != null

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val request = webRequest.nativeRequest as HttpServletRequest

        val header = request.getHeader(JWT_AUTH_HEADER)
        if (header != null && header.startsWith(JWT_PREFIX)) {
            val token = TokenDto(header.substring(JWT_PREFIX.length))

            return authenticateUsecase.authenticate(token)
                .unwrapOrElseThrow { UnauthorizedException("invalid token") }
        }

        throw UnauthorizedException("missing $JWT_AUTH_HEADER header")
    }

    companion object {

        private const val JWT_AUTH_HEADER = "Authorization"
        private const val JWT_PREFIX = "Bearer "
    }
}
