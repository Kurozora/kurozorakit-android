plugins {
    kotlin("jvm") version "2.2.0"
    id("com.vanniktech.maven.publish") version "0.35.0"
}

group = "kurozorakit"
version = "1.2.4"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "kurozorakit", version.toString())

    pom {
        name = "KurozoraKit"
        description = ""
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
