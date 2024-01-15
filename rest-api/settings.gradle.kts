rootProject.name = "kweather-api"

include("internal:core")

include("internal:weather")
include("internal:weather:domain")
include("internal:weather:api")

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

            library("security-bcrypt", "at.favre.lib", "bcrypt").versionRef(bcrypt)
            library("security-jwt", "com.auth0", "java-jwt").versionRef(jwt)

            library("datasource-postgres", "org.postgresql", "postgresql").versionRef(postgres)
            library("datasource-hikari", "com.zaxxer", "HikariCP").versionRef(hikari)
        }

        create("testWorkspace") {
            val kotlinTest = version("kotlin-test", "1.9.21")
            val mockito = version("mockito", "5.2.1")

            library("kotlin-test", "org.jetbrains.kotlin", "kotlin-test").versionRef(kotlinTest)

            library("mockito-kotlin", "org.mockito.kotlin", "mockito-kotlin").versionRef(mockito)
        }
    }
}
