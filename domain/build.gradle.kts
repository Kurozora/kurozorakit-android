plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":api"))
    api(project(":data"))
    api(project(":shared"))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.serialization)
    implementation(Deps.Kotlin.dateTime)
    implementation(Deps.Ktor.core)
}