package github.makeitvsolo.kweather.boot.api.controller.user.access.request

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UserCredentialsRequest(

    @NotNull
    @Size(min = MIN_NAME_SIZE)
    val name: String,

    @NotNull
    @Size(min = MIN_PASSWORD_SIZE)
    val password: String
) {

    companion object {

        private const val MIN_NAME_SIZE = 5
        private const val MIN_PASSWORD_SIZE = 5
    }
}
