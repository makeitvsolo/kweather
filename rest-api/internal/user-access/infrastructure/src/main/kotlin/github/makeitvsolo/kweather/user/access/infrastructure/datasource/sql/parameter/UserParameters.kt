package github.makeitvsolo.kweather.user.access.infrastructure.datasource.sql.parameter

import github.makeitvsolo.kweather.user.access.domain.MapUserInto

internal data class UserParameters(
    val id: String,
    val name: String,
    val password: String
) {

    object FromUser : MapUserInto<UserParameters> {

        override fun from(
            id: String,
            name: String,
            password: String
        ): UserParameters =
            UserParameters(id, name, password)
    }
}
