group = "github.makeitvsolo.kweather.boot"

dependencies {
    implementation(project(":internal:core"))

    implementation(project(":internal:user-access:domain"))
    implementation(project(":internal:user-access:api"))
    implementation(project(":internal:user-access:application"))
    implementation(project(":internal:user-access:infrastructure"))

    implementation(project(":internal:weather:domain"))
    implementation(project(":internal:weather:api"))
    implementation(project(":internal:weather:application"))
    implementation(project(":internal:weather:infrastructure"))
}
