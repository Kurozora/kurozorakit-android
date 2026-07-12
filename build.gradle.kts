import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm

plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0" apply false
    id("com.vanniktech.maven.publish") version "0.35.0"
    id("org.jetbrains.dokka") version "1.9.10"
}

group = "app.kurozora"
version = "1.2.6"

dependencies {
    api(project(":api"))
    api(project(":core"))
    api(project(":data"))
    api(project(":shared"))
}

allprojects {
    project.group = rootProject.group
    project.version = rootProject.version
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.vanniktech.maven.publish")
    apply(plugin = "org.jetbrains.dokka")

    tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
        dokkaSourceSets {
            configureEach {
                reportUndocumented.set(true)
            }
        }
    }

    dependencies {
        testImplementation(kotlin("test"))
    }

    tasks.test {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(17)
    }

    // Her alt modül kendi adıyla mavenLocal veya MavenCentral'e basılacak
    mavenPublishing {
        publishToMavenCentral()

        if (!project.gradle.startParameter.taskNames.any { it.contains("Local") }) {
            signAllPublications()
        }

        // artifactId dinamik olarak alt modülün adı olacak (örn: kurozora-kit-api)
        coordinates(rootProject.group.toString(), "kurozorakit-${project.name}", rootProject.version.toString())

        configure(
            KotlinJvm(
                javadocJar = JavadocJar.Dokka("dokkaHtml"), // Her modülün kendi dokümantasyonu
                sourcesJar = true
            )
        )

        pom {
            name.set("KurozoraKit ${project.name.capitalize()}")
            description.set("KurozoraKit ${project.name} module lets users manage their anime, manga, games and music library.")
            inceptionYear.set("2025")
            url.set("https://github.com/Kurozora/kurozorakit-android")
            licenses {
                license {
                    name.set("MIT")
                    url.set("https://github.com/Kurozora/kurozorakit-android/blob/main/LICENSE")
                }
            }
            developers {
                developer {
                    id.set("kurozora")
                    name.set("Kurozora")
                    url.set("https://github.com/Kurozora/")
                }
            }
            scm {
                url.set("https://github.com/Kurozora/kurozorakit-android")
                connection.set("scm:git:git://github.com/Kurozora/kurozorakit-android.git")
                developerConnection.set("scm:git:ssh://git@github.com/Kurozora/kurozorakit-android.git")
            }
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    if (!project.gradle.startParameter.taskNames.any { it.contains("Local") }) { signAllPublications() }

    coordinates(group.toString(), "kurozorakit", version.toString())

    configure(com.vanniktech.maven.publish.KotlinJvm(
        javadocJar = com.vanniktech.maven.publish.JavadocJar.Dokka("dokkaHtml"),
        sourcesJar = true
    ))

    pom {
        name.set("KurozoraKit ${project.name.capitalize()}")
        description.set("KurozoraKit ${project.name} module lets users manage their anime, manga, games and music library.")
        inceptionYear.set("2025")
        url.set("https://github.com/Kurozora/kurozorakit-android")
        licenses {
            license {
                name.set("MIT")
                url.set("https://github.com/Kurozora/kurozorakit-android/blob/main/LICENSE")
            }
        }
        developers {
            developer {
                id.set("kurozora")
                name.set("Kurozora")
                url.set("https://github.com/Kurozora/")
            }
        }
        scm {
            url.set("https://github.com/Kurozora/kurozorakit-android")
            connection.set("scm:git:git://github.com/Kurozora/kurozorakit-android.git")
            developerConnection.set("scm:git:ssh://git@github.com/Kurozora/kurozorakit-android.git")
        }
    }
}

// Opsiyonel: Tüm modüllerin dokümantasyonunu tek bir web sitesinde toplamak istersen root'ta kalabilir
tasks.withType<org.jetbrains.dokka.gradle.DokkaMultiModuleTask>().configureEach {
    outputDirectory.set(layout.buildDirectory.dir("dokka/htmlMultiModule"))
    moduleName.set("KurozoraKit SDK")
    pluginsMapConfiguration.set(
        mapOf(
            "org.jetbrains.dokka.base.DokkaBase" to """
                {
                  "footerMessage": "© 2026 Kurozora Team - KurozoraKit Documentation",
                  "separateInheritedMembers": true
                }
            """.trimIndent()
        )
    )
}