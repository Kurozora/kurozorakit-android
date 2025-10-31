plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.2.0"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":api"))
    implementation(project(":shared"))
    implementation(project(":cache"))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.serialization)
    implementation(Deps.Kotlin.dateTime)
}