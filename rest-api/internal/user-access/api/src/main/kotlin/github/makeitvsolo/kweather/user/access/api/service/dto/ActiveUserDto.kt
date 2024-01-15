package github.makeitvsolo.kweather.user.access.api.service.dto

import github.makeitvsolo.kweather.user.access.domain.MapUserInto

data class ActiveUserDto(
    val id: String,
    val name: String
) {

    object FromUser : MapUserInto<ActiveUserDto> {

        override fun from(
            id: String,
            name: String,
            password: String
        ): ActiveUserDto =
            ActiveUserDto(id, name)
    }
}
