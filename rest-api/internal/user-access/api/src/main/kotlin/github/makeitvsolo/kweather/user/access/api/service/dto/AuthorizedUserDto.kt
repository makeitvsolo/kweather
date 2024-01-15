package github.makeitvsolo.kweather.user.access.api.service.dto

data class AuthorizedUserDto(
    val token: AccessTokenDto,
    val user: ActiveUserDto
)
