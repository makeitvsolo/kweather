dependencies {
    implementation(project(":internal:core"))
    implementation(project(":internal:weather:domain"))
    implementation(project(":internal:weather:api"))

    testImplementation(testWorkspace.kotlin.test)
    testImplementation(testWorkspace.mockito.kotlin)
}
