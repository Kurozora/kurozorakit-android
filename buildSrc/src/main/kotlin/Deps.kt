object Deps {
    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerialization}"
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0"
        const val atomicfu = "org.jetbrains.kotlinx:atomicfu:${Versions.atomicfu}"
    }

    object Ktor {
        const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
        const val json = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
        const val logging = "io.ktor:ktor-client-logging:${Versions.ktor}"
        const val auth = "io.ktor:ktor-client-auth:${Versions.ktor}"
        const val mock = "io.ktor:ktor-client-mock:${Versions.ktor}"
        const val cio = "io.ktor:ktor-client-cio:${Versions.ktor}"
    }

    object Store5 {
        const val store = "org.mobilenativefoundation.store:store5:${Versions.store5}"
        const val cache = "org.mobilenativefoundation.store:cache5:${Versions.store5}"
    }

    object SQLDelight {
        const val runtime = "app.cash.sqldelight:runtime:${Versions.sqldelight}"
        const val coroutinesExtensions = "app.cash.sqldelight:coroutines-extensions:${Versions.sqldelight}"
        const val primitiveAdapters = "app.cash.sqldelight:primitive-adapters:${Versions.sqldelight}"
        const val sqliteDriver = "app.cash.sqldelight:sqlite-driver:${Versions.sqldelight}"
        const val androidDriver = "app.cash.sqldelight:android-driver:${Versions.sqldelight}"
    }

    object Test {
        const val mockk = "io.mockk:mockk:${Versions.mockk}"
    }
}
