import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm

plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0" apply false
    id("com.vanniktech.maven.publish") version "0.35.0"
    id("org.jetbrains.dokka") version "1.9.10"
}

group = "app.kurozora"
version = "1.2.5"

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.dokka")

    // Alt projelerin dökümantasyon ayarlarını buradan yapabilirsin
    tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
        dokkaSourceSets {
            configureEach {
                reportUndocumented.set(true)
                //çalışmadı en son tekrar dene sonra
//                pluginsMapConfiguration.set(
//                    mapOf(
//                        "org.jetbrains.dokka.base.DokkaBase" to """
//                            {
//                              "footerMessage": "© 2026 Kurozora Team - KurozoraKit Documentation",
//                            }
//                        """.trimIndent()
//                    )
//                )
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
}

dependencies {
    implementation(project(":api"))
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":shared"))
}

// --- KRİTİK BÖLÜM: FAT JAR OLUŞTURMA ---
// Yeni bir task yaratmak yerine, var olan 'jar' taskını modifiye ediyoruz.
// Böylece Maven plugin'i otomatik olarak bu dolu jar'ı alıp yayınlar.
tasks.named<Jar>("jar") {
    archiveBaseName.set("kurozora-kit")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // 1. Root projenin çıktılarını al
    from(sourceSets.main.get().output)

    // 2. Tüm alt projelerin çıktılarını al ve içine göm
    subprojects.forEach { subproject ->
        from(subproject.sourceSets.main.get().output)
    }
}

// Kaynak kodları da (Sources) birleştirmek istersen (Opsiyonel ama önerilir):
//tasks.named<Jar>("sourcesJar") {
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//    subprojects.forEach { subproject ->
//        from(subproject.sourceSets.main.get().allSource)
//    }
//}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), project.rootProject.name, version.toString())

    configure(
        KotlinJvm(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true
        )
    )

    pom {
        name = "KurozoraKit"
        description = "KurozoraKit lets users manage their anime, manga, games and music library and access many other services from your app. When users provide permission to access their Kurozora account, they can use your app to share anime, add it to their library, and discover any of the thousands of content in the Kurozora catalog. If your app detects that the user is not yet a Kurozora member, you can offer them to create an account within your app."
        inceptionYear = "2025"
        url = "https://github.com/Kurozora/kurozorakit-android"
        licenses {
            license {
                name = "MIT"
                url = "https://github.com/Kurozora/kurozorakit-android/blob/main/LICENSE"
            }
        }
        developers {
            developer {
                id = "kurozora"
                name = "Kurozora"
                url = "https://github.com/Kurozora/"
            }
        }
        scm {
            url = "https://github.com/Kurozora/kurozorakit-android"
            connection = "scm:git:git://github.com/Kurozora/kurozorakit-android.git"
            developerConnection = "scm:git:ssh://git@github.com/Kurozora/kurozorakit-android.git"
        }
    }
}

// Çoklu modül birleştirme taskı
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