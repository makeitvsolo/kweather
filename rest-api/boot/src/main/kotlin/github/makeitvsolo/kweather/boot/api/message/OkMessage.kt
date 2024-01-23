package github.makeitvsolo.kweather.boot.api.message

import org.springframework.http.HttpStatus

data class OkMessage<T>(
    val code: Int,
    val status: String,
    val data: T? = null
) {

    companion object {

        fun from(status: HttpStatus): OkMessage<Any> =
            OkMessage(status.value(), status.reasonPhrase)

        fun <D> from(status: HttpStatus, data: D): OkMessage<D> =
            OkMessage(status.value(), status.reasonPhrase, data)
    }
}
