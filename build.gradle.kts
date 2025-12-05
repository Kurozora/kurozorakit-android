import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm

plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0" apply false
    id("com.vanniktech.maven.publish") version "0.35.0"
}

allprojects {
    group = "app.kurozora"
    version = "1.2.5"

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
val fatJar = tasks.register<Jar>("fatJar") {
    archiveBaseName.set(rootProject.name)
    archiveClassifier.set("")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // main module outputs
    from(sourceSets.main.get().output)

    // include all subproject outputs
    from(subprojects.map { it.sourceSets["main"].output })
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), project.rootProject.name, version.toString())

    configure(
        KotlinJvm(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = true,
            publication = "fatJar"
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
        organization {
            name = "Kurozora"
            url = "https://kurozora.app"
        }
        developers {
            developer {
                id = "kurozora"
                name = "Kurozora"
                url = "https://github.com/Kurozora/"
            }
        }
        issueManagement {
            system = "GitHub Issues"
            url = "https://github.com/Kurozora/kurozorakit-android/issues"
        }
        scm {
            url = "https://github.com/Kurozora/kurozorakit-android"
            connection = "scm:git:git://github.com/Kurozora/kurozorakit-android.git"
            developerConnection = "scm:git:ssh://git@github.com/Kurozora/kurozorakit-android.git"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("fatJar") {
            artifact(tasks.named("fatJar"))

            groupId = group.toString()
            artifactId = rootProject.name
            version = version.toString()

            pom {
                name.set("KurozoraKit")
                description.set("KurozoraKit SDK")
                url.set("https://github.com/Kurozora/kurozorakit-android")
            }
        }
    }
}
