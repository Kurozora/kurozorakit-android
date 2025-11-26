plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0" apply false
    id("com.vanniktech.maven.publish") version "0.35.0"
}

group = "kurozorakit"
version = "1.2.4"

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
        jvmToolchain(17)
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":shared"))
}

/**
 * Build a single fat JAR combining root + all subprojects.
 */
tasks.register<Jar>("fatJar") {
    archiveBaseName.set("kurozorakit")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // main module outputs
    from(sourceSets.main.get().output)

    // include all subproject outputs
    from(subprojects.map { it.sourceSets["main"].output })
}
