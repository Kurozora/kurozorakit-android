plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(Deps.Kotlin.dateTime)
    implementation(Deps.Kotlin.serialization)
    implementation(Deps.Kotlin.coroutinesCore)
}