package github.makeitvsolo.kweather.workspace.configuration

import org.cqfn.diktat.plugin.gradle.DiktatExtension
import org.cqfn.diktat.plugin.gradle.DiktatGradlePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

fun Project.useDiktat() {
    apply<DiktatGradlePlugin>()

    configure<DiktatExtension> {
        diktatConfigFile = rootProject.file("diktat-analysis.yml")
        debug = true

        inputs {
            include("src/**/*.kt", "**/*.kts")
            exclude("src/test/*.kt")
        }
    }
}
