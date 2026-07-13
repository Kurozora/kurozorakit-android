plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("app.cash.sqldelight") version "2.0.2"
}

sqldelight {
    databases {
        create("KurozoraDatabase") {
            packageName.set("kurozorakit.store.db")
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
            verifyMigrations.set(true)
        }
    }
}

dependencies {
    // Project modules
    implementation(project(":shared"))
    implementation(project(":api"))
    implementation(project(":data"))

    // Kotlin
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.serialization)
    implementation(Deps.Kotlin.dateTime)
    implementation(Deps.Kotlin.atomicfu)

    // Store5
    implementation(Deps.Store5.store)
    implementation(Deps.Store5.cache)

    // SQLDelight
    implementation(Deps.SQLDelight.runtime)
    implementation(Deps.SQLDelight.coroutinesExtensions)
    implementation(Deps.SQLDelight.primitiveAdapters)
    implementation(Deps.SQLDelight.sqliteDriver)

    // Ktor (for API client types)
    implementation(Deps.Ktor.core)
}
