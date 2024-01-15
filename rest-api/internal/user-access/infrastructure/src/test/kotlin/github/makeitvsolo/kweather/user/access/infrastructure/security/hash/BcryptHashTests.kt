package github.makeitvsolo.kweather.user.access.infrastructure.security.hash

import github.makeitvsolo.kweather.user.access.infrastructure.InfrastructureUnitTest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BcryptHashTests : InfrastructureUnitTest() {

    private val bcrypt: BcryptHash = BcryptHash(DEFAULT_COST, DEFAULT_SALT.toByteArray(Charsets.UTF_8))

    @Test
    fun `encoded password not equals with raw password`() {
        val raw = "rawpasswd"

        assertNotEquals(raw, bcrypt.hash(raw))
    }

    @Test
    fun `passwords are matches when both raw passwords are matches`() {
        val raw = "rawpasswd"
        val sameAsRaw = "rawpasswd"

        assertEquals(bcrypt.hash(raw), bcrypt.hash(sameAsRaw))
    }

    @Test
    fun `passwords aren't matches when raw passwords aren't matches`() {
        val raw = "rawpasswd"
        val otherRaw = "other_rawpasswd"

        assertNotEquals(bcrypt.hash(raw), bcrypt.hash(otherRaw))
    }

    companion object {

        const val DEFAULT_COST = 6
        const val DEFAULT_SALT = "supersecret_salt"
    }
}
