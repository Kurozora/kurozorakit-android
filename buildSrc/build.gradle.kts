plugins {
    kotlin("jvm") version "2.2.0"
}

allprojects {
    group = "app.kurozora"
    version = "1.2.5"

    repositories {
        mavenCentral()
    }
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
