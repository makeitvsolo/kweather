package github.makeitvsolo.kweather.boot.api.controller.user.access.request

import jakarta.validation.constraints.NotNull

data class RefreshTokenRequest(

    @NotNull
    val token: String
)
