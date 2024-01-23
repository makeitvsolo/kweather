package github.makeitvsolo.kweather.weather.domain.account

import github.makeitvsolo.kweather.core.mapping.Into

interface MapAccountInto<out R> : Into<R> {

    fun from(id: String, name: String): R
}

class Account internal constructor(
    private val id: String,
    private val name: String
) {

    fun <R, M : MapAccountInto<R>> into(map: M): R = map.from(id, name)

    override fun equals(other: Any?): Boolean = (other is Account) && id == other.id

    override fun hashCode(): Int = id.hashCode()

    companion object {

        fun from(
            id: String,
            name: String
        ): Account =
            Account(id, name)
    }
}
