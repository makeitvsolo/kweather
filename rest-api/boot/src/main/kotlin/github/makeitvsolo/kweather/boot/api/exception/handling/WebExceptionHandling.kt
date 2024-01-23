package github.makeitvsolo.kweather.boot.api.exception.handling

import github.makeitvsolo.kweather.boot.api.message.ErrorMessage
import github.makeitvsolo.kweather.boot.configuration.spring.session.exception.UnauthorizedException

import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
open class WebExceptionHandling {

    @ExceptionHandler(
        MethodArgumentNotValidException::class,
        MissingServletRequestParameterException::class
    )
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun handleInvalidPayload(ex: Throwable): ResponseEntity<*> {
        log.debug("invalid payload error")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorMessage.from(HttpStatus.BAD_REQUEST))
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun handleHttpMediaTypeNotSupportedException(ex: HttpMediaTypeNotSupportedException): ResponseEntity<*> {
        log.debug("unsupported media type error")
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .build<Any>()
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun handleHttpRequestMethodNotSupportedException(ex: HttpRequestMethodNotSupportedException): ResponseEntity<*> {
        log.debug("unsupported method error")
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .build<Any>()
    }

    @ExceptionHandler(UnauthorizedException::class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun handleUnauthorized(ex: UnauthorizedException): ResponseEntity<*> {
        log.debug("unauthorized error")
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ErrorMessage.from(HttpStatus.UNAUTHORIZED, ex.message ?: "missing authorization"))
    }

    @ExceptionHandler(Throwable::class)
    fun otherwise(ex: Throwable): ResponseEntity<*> {
        log.error("Internal Error occurs:", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build<Any>()
    }

    companion object {

        private val log = LoggerFactory.getLogger(WebExceptionHandling::class.java)
    }
}
