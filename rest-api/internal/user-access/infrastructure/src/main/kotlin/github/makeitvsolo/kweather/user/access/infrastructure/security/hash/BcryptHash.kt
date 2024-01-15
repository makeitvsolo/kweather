package github.makeitvsolo.kweather.user.access.infrastructure.security.hash

import github.makeitvsolo.kweather.user.access.api.security.hash.Hash

import at.favre.lib.crypto.bcrypt.BCrypt

class BcryptHash internal constructor(
    private val cost: Int,
    private val salt: ByteArray
) : Hash {

    override fun hash(password: String): String =
        BCrypt.withDefaults()
            .hash(cost, salt, password.toByteArray(Charsets.UTF_8))
            .toString(Charsets.UTF_8)
}
