dependencies {
    implementation(project(":internal:core"))
    implementation(project(":internal:weather:domain"))
    implementation(project(":internal:weather:api"))
    implementation(project(":internal:weather:application"))
    implementation(project(":internal:weather:infrastructure"))

    testImplementation(testWorkspace.kotlin.test)
    testImplementation(testWorkspace.testcontainers.core)
    testImplementation(testWorkspace.testcontainers.junit)
}
