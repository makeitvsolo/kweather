dependencies {
    implementation(project(":internal:core"))
    implementation(project(":internal:user-access:domain"))
    implementation(project(":internal:user-access:api"))

    implementation(workspace.datasource.postgres)
    implementation(workspace.datasource.hikari)
    implementation(workspace.security.bcrypt)
    implementation(workspace.security.jwt)

    testImplementation(testWorkspace.kotlin.test)
}
