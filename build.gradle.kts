import java.io.ByteArrayOutputStream
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.insprill"
version = getFullVersion()

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots") // Kord 0.8 SNAPSHOT, Tesseract Platform 5.3 SNAPSHOT
}

dependencies {
    // Discord
    implementation("dev.kord:kord-core:0.8.x-SNAPSHOT")

    // Configuration
    implementation("com.sksamuel.hoplite:hoplite-core:2.7.1")
    implementation("com.sksamuel.hoplite:hoplite-yaml:2.7.1")
    implementation("com.sksamuel.hoplite:hoplite-datetime:2.7.1")

    // Web requests
    implementation("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation("com.github.kittinunf.fuel:fuel-coroutines:2.3.1")
    implementation("com.github.kittinunf.fuel:fuel-kotlinx-serialization:2.3.1")

    // OCR
    implementation("org.bytedeco:tesseract-platform:5.3.0-1.5.9-SNAPSHOT")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier.set("")

        mergeServiceFiles()

        manifest {
            attributes["Main-Class"] = "net.insprill.robotinsprill.RobotInsprillKt"
        }
    }

    build {
        dependsOn(shadowJar)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

fun getFullVersion(): String {
    val version = project.property("version")!! as String
    return if (version.contains("-SNAPSHOT")) {
        "$version+rev.${getGitHash()}"
    } else {
        version
    }
}

fun getGitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--verify", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}
