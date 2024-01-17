rootProject.name = "kweather-api"

include("internal:core")

include("internal:weather")
include("internal:weather:domain")
include("internal:weather:api")
include("internal:weather:application")
include("internal:weather:infrastructure")

include("internal:user-access")
include("internal:user-access:domain")
include("internal:user-access:api")
include("internal:user-access:application")
include("internal:user-access:infrastructure")
include("internal:user-access:tests")

dependencyResolutionManagement {
    versionCatalogs {
        create("workspace") {
            val bcrypt = version("bcrypt", "0.10.2")
            val jwt = version("jwt", "4.4.0")

            val postgres = version("postgres", "42.7.1")
            val hikari = version("hikari", "5.1.0")

            val fuel = version("fuel", "2.3.1")

            library("security-bcrypt", "at.favre.lib", "bcrypt").versionRef(bcrypt)
            library("security-jwt", "com.auth0", "java-jwt").versionRef(jwt)

            library("datasource-postgres", "org.postgresql", "postgresql").versionRef(postgres)
            library("datasource-hikari", "com.zaxxer", "HikariCP").versionRef(hikari)
            library("datasource-fuel", "com.github.kittinunf.fuel", "fuel").versionRef(fuel)
            library("datasource-fuel-gson", "com.github.kittinunf.fuel", "fuel-gson").versionRef(fuel)
        }

        create("testWorkspace") {
            val kotlinTest = version("kotlin-test", "1.9.21")
            val mockito = version("mockito", "5.2.1")
            val testcontainers = version("testcontainers", "1.19.3")

            library("kotlin-test", "org.jetbrains.kotlin", "kotlin-test").versionRef(kotlinTest)

            library("mockito-kotlin", "org.mockito.kotlin", "mockito-kotlin").versionRef(mockito)

            library("testcontainers-core", "org.testcontainers", "testcontainers").versionRef(testcontainers)
            library("testcontainers-junit", "org.testcontainers", "junit-jupiter").versionRef(testcontainers)
        }
    }
}
