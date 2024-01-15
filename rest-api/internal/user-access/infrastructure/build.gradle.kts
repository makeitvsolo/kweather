dependencies {
    implementation(project(":internal:core"))
    implementation(project(":internal:user-access:domain"))
    implementation(project(":internal:user-access:api"))

    implementation(workspace.security.bcrypt)

    testImplementation(testWorkspace.kotlin.test)
}
