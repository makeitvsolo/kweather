package github.makeitvsolo.kweather.user.access.infrastructure.security.hash.bcrypt

import github.makeitvsolo.kweather.user.access.api.security.hash.Hash

import at.favre.lib.crypto.bcrypt.BCrypt

class BcryptHash internal constructor(
    private val cost: Int,
    private val salt: ByteArray
) : Hash {

    override fun hash(string: String): String =
        BCrypt.withDefaults()
            .hash(cost, salt, string.toByteArray(Charsets.UTF_8))
            .toString(Charsets.UTF_8)
}
