package github.makeitvsolo.kweather.boot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class KweatherApplication

fun main(args: Array<String>) {
    runApplication<KweatherApplication>(*args)
}
