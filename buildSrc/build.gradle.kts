plugins {
    kotlin("jvm") version "2.2.0"
}

group = "app.kurozora"
version = "1.2.4-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
