dependencies {
    implementation(project(":internal:core"))
    implementation(project(":internal:user-access:domain"))
    implementation(project(":internal:user-access:api"))

    testImplementation(testWorkspace.kotlin.test)
    testImplementation(testWorkspace.mockito.kotlin)
}
