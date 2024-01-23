package github.makeitvsolo.kweather.boot.api.message

import org.springframework.http.HttpStatus

data class ErrorMessage(
    val code: Int,
    val status: String,
    val details: String? = null
) {

    companion object {

        fun from(status: HttpStatus): ErrorMessage =
            ErrorMessage(status.value(), status.reasonPhrase)

        fun from(status: HttpStatus, details: String) =
            ErrorMessage(status.value(), status.reasonPhrase, details)
    }
}
