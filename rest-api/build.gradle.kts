import github.makeitvsolo.kweather.workspace.configuration.useDiktat

plugins {
    kotlin("jvm") version "1.9.21"
}

allprojects {
    apply(plugin = "kotlin")

    group = "github.makeitvsolo.kweather"
    version = "0.1.0-dev"

    repositories {
        mavenCentral()
    }

    kotlin {
        jvmToolchain(17)
    }

    useDiktat()

    tasks.test {
        useJUnitPlatform()
    }
}
