package github.makeitvsolo.kweather.user.access.api.security.session

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.security.session.error.DecodeTokenError
import github.makeitvsolo.kweather.user.access.domain.MapUserInto
import github.makeitvsolo.kweather.user.access.domain.User

data class Token(
    val access: String,
    val refresh: String
)

data class TokenPayload(
    val userId: String,
    val userName: String
) {

    object FromUser : MapUserInto<TokenPayload> {

        override fun from(
            id: String,
            name: String,
            password: String
        ): TokenPayload =
            TokenPayload(id, name)
    }
}

interface EncodeToken {

    fun encode(user: User): Token
    fun refresh(token: String): Result<Token, DecodeTokenError>

    fun decode(token: String): Result<TokenPayload, DecodeTokenError>
}
