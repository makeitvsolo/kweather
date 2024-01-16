dependencies {
    implementation(project(":internal:core"))
    implementation(project(":internal:weather:domain"))
    implementation(project(":internal:weather:api"))

    implementation(workspace.datasource.postgres)
    implementation(workspace.datasource.hikari)
}
