plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.serialization)
    implementation(Deps.Ktor.core)
    implementation(Deps.Ktor.cio)
}