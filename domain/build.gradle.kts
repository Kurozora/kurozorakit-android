plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":api"))
    api(project(":data"))
    api(project(":shared"))
    api(project(":store"))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.serialization)
    implementation(Deps.Kotlin.dateTime)
    implementation(Deps.Ktor.core)

    testImplementation(Deps.Ktor.mock)
    testImplementation(Deps.Test.coroutinesTest)
}