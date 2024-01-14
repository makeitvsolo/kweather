package github.makeitvsolo.kweather.user.access.api.security.hash

interface Hash {

    fun hash(string: String): String
}
