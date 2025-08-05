plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0" apply false
}

group = "kurozora.kit"
version = "1.0-SNAPSHOT"

buildscript {
    repositories {
        mavenCentral()
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        testImplementation(kotlin("test"))
    }

    tasks.test {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(22)
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":shared"))
}