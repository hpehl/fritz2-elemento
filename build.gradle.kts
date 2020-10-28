plugins {
    kotlin("js") version "1.4.10"
    id("org.jetbrains.dokka") version "1.4.10"
    `maven-publish`
}

group = "dev.fritz2"
version = "0.8-SNAPSHOT"
val fritz2Version = "0.8-SNAPSHOT"
val kotestVersion = "4.3.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://oss.jfrog.org/artifactory/jfrog-dependencies")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    jcenter()
}

dependencies {
    implementation("dev.fritz2:core:$fritz2Version")
    testImplementation("io.kotest:kotest-framework-api:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-engine:$kotestVersion")
}

kotlin {
    explicitApi()
    js {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }
}
