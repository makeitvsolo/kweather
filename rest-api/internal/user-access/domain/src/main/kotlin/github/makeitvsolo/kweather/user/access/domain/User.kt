package github.makeitvsolo.kweather.user.access.domain

import github.makeitvsolo.kweather.core.mapping.Into
import github.makeitvsolo.kweather.core.type.Unique

interface MapUserInto<out R> : Into<R> {

    fun from(
        id: String,
        name: String,
        password: String
    ): R
}

class User internal constructor(
    private val id: String,
    private val name: String,
    private val password: String
) {

    fun isValidCredentials(name: String, password: String): Boolean =
        this.password == password && this.name == name

    fun <R, M : MapUserInto<R>> into(map: M): R =
        map.from(id, name, password)

    override fun equals(other: Any?): Boolean = (other is User) && id == other.id

    override fun hashCode(): Int = id.hashCode()

    companion object {

        fun from(
            id: String,
            name: String,
            password: String
        ): User =
            User(id, name, password)

        fun create(
            id: Unique<String>,
            name: String,
            password: String
        ): User =
            User(id.unique(), name, password)
    }
}
