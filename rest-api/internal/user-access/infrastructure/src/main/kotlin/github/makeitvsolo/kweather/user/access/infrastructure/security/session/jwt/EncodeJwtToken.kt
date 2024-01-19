package github.makeitvsolo.kweather.user.access.infrastructure.security.session.jwt

import github.makeitvsolo.kweather.core.error.handling.Result
import github.makeitvsolo.kweather.user.access.api.security.session.EncodeToken
import github.makeitvsolo.kweather.user.access.api.security.session.Token
import github.makeitvsolo.kweather.user.access.api.security.session.TokenPayload
import github.makeitvsolo.kweather.user.access.api.security.session.error.DecodeTokenError
import github.makeitvsolo.kweather.user.access.domain.User

import java.time.Instant

class EncodeJwtToken internal constructor(
    private val access: EncodeJwt,
    private val refresh: EncodeJwt
) : EncodeToken {

    fun encode(payload: TokenPayload): Token {
        val now = Instant.now()

        return Token(
            access.encode(payload, now),
            refresh.encode(payload, now)
        )
    }

    override fun encode(user: User): Token =
        encode(user.into(TokenPayload.FromUser))

    override fun decode(token: Token): Result<TokenPayload, DecodeTokenError> =
        access.decode(token.access)

    override fun refresh(token: Token): Result<Token, DecodeTokenError> =
        refresh.decode(token.refresh).map { payload ->
            encode(payload)
        }
}
