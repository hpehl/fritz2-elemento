import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.publish.maven.MavenPom

object Constants {
    const val group = "dev.fritz2"
    const val version = "0.0.3"
}

object PluginVersions {
    const val js = "1.4.10"
    const val dokka = "1.4.10"
}

object Versions {
    const val fritz2 = "0.8-SNAPSHOT"
    const val kotest = "4.3.0"
}

fun DependencyHandler.fritz2() {
    add("implementation", "dev.fritz2:core:${Versions.fritz2}")
}

fun DependencyHandler.kotest() {
    add("testImplementation", "io.kotest:kotest-framework-api:${Versions.kotest}")
    add("testImplementation", "io.kotest:kotest-assertions-core:${Versions.kotest}")
    add("testImplementation", "io.kotest:kotest-property:${Versions.kotest}")
    add("testImplementation", "io.kotest:kotest-framework-engine:${Versions.kotest}")
}

fun MavenPom.defaultPom() {
    name.set("elemento")
    description.set("Element helpers for Fritz2")
    url.set("https://github.com/hpehl/fritz2-elemento")
    licenses {
        license {
            name.set("Apache-2.0")
            url.set("https://opensource.org/licenses/Apache-2.0")
        }
    }
    developers {
        developer {
            id.set("hpehl")
            name.set("Harald Pehl")
            organization.set("Red Hat")
            organizationUrl.set("http://www.redhat.com")
        }
    }
    scm {
        url.set("https://github.com/hpehl/fritz2-elemento.git")
        connection.set("scm:git:git://github.com/hpehl/fritz2-elemento.git")
        developerConnection.set("scm:git:git://github.com/hpehl/fritz2-elemento.git")
    }
}
