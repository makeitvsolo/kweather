package github.makeitvsolo.kweather.user.access.api.security.session

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.user.access.domain.User

data class Token(
    val access: String,
    val refresh: String
)

data class TokenPayload(
    val userId: String,
    val userName: String
)

interface MapDecodeTokenErrorInto<out R> : Into<R> {

    fun fromInvalidTokenError(details: String): R
    fun fromInternalError(throwable: Throwable): R
}

sealed interface DecodeTokenError {

    fun <R, M : MapDecodeTokenErrorInto<R>> into(map: M): R

    data class InvalidTokenError(private val details: String) : DecodeTokenError {

        override fun <R, M : MapDecodeTokenErrorInto<R>> into(map: M): R =
            map.fromInvalidTokenError(details)
    }

    data class InternalError(private val throwable: Throwable) : DecodeTokenError {

        override fun <R, M : MapDecodeTokenErrorInto<R>> into(map: M): R =
            map.fromInternalError(throwable)
    }
}

interface EncodeToken {

    fun encode(user: User): Token
    fun refresh(token: Token): Result<Token, DecodeTokenError>

    fun decode(token: Token): Result<TokenPayload, DecodeTokenError>
}