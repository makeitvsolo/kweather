dependencies {
    implementation(project(":internal:core"))
    implementation(project(":internal:user-access:domain"))
    implementation(project(":internal:user-access:api"))
    implementation(project(":internal:user-access:application"))
    implementation(project(":internal:user-access:infrastructure"))

    testImplementation(testWorkspace.kotlin.test)
    testImplementation(testWorkspace.testcontainers.core)
    testImplementation(testWorkspace.testcontainers.junit)
}
