plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    api(project(":shared"))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.serialization)
    implementation(Deps.Ktor.core)
    implementation(Deps.Ktor.contentNegotiation)
    implementation(Deps.Ktor.json)
    implementation(Deps.Ktor.logging)
    implementation(Deps.Ktor.auth)
    implementation(Deps.Ktor.cio)

    testImplementation(Deps.Ktor.mock)
    testImplementation(Deps.Test.coroutinesTest)
}