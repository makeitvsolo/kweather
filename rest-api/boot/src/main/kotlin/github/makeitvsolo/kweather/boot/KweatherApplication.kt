package github.makeitvsolo.kweather.boot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
open class KweatherApplication

fun main(args: Array<String>) {
    runApplication<KweatherApplication>(*args)
}
