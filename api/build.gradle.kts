plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":cache"))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.serialization)
    implementation(Deps.Ktor.core)
    implementation(Deps.Ktor.contentNegotiation)
    implementation(Deps.Ktor.json)
    implementation(Deps.Ktor.logging)
    implementation(Deps.Ktor.auth)
    implementation(Deps.Ktor.cio)
}