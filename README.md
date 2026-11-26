<p></p>

<p align="center"><img src=".github/Assets/KurozoraKit.png" width="200px"></p>

<p align="center">
    <sup><b>The magic behind Kurozora apps for Android</b></sup>
</p>

# KurozoraKit [![Kotlin](https://img.shields.io/badge/Kotlin-white.svg?style=flat&logo=Kotlin)](https://kotlinlang.org) [![Kurozora Discord Server](https://img.shields.io/discord/449250093623934977?style=flat&label=&logo=Discord&logoColor=white&color=7289DA)](https://discord.gg/f3QFzGqsah) [![Documentation](https://img.shields.io/badge/Documentation-100%25-green.svg?style=flat)](https://developer.kurozora.app/KurozoraKit) [![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat)](LICENSE)

[KurozoraKit](https://developer.kurozora.app/kurozorakit) lets users manage their anime, manga, games and music library and access many other services from your app. When users provide permission to access their Kurozora account, they can use your app to share anime, add it to their library, and discover any of the thousands of content in the Kurozora catalog. If your app detects that the user is not yet a Kurozora member, you can offer them to create an account within your app.

KurozoraKit is designed to be:

* **🛠 Intuitive:** KurozoraKit is built with Kotlin, a **concise** and **multiplatform** programming language.

* **🧵 Asynchronous:** By utilizing the power of Kotlin Coroutines, KurozoraKit is more readable and less prone to errors like data races and deadlocks by design.

* **✨ Magical:** The kit is carefully designed to work as efficient and reliable as you would expect it to.

* **⚙️ Reliable:** Built for the best [API](https://github.com/kurozora/kurozora-web). The way KurozoraKit works together with the Kurozora API is truly otherworldly.

# Requirements

KurozoraKit supports **Android API 21+ / JVM 17+** and is built with **Kotlin 2.2+**.

To use KurozoraKit in your project, you need to install it first.

## Installation

### Gradle

KurozoraKit is available through Maven Central using the [Gradle Build Tool](https://gradle.org/). To install it, add the dependency to your `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("app.kurozora:kurozorakit:2.0.0")
}
```

## Highlights in 2.0.0

* **27 Domain Repositories:** Full Kurozora API v1 coverage including Shows, Literature, Games, Seasons, Episodes, Songs & Synced Lyrics, Characters, People, Studios, Feed, Reviews, Library, Schedule, Air Seasons, Achievements, Recaps, Theme Store, **Charts** (`kit.charts()`), and **Parental Guide** (`kit.parentalGuide()`).
* **Two-Factor Authentication (2FA):** Typed `SignInResult` sealed class with `submitTwoFactorChallenge(...)` support in `AuthRepository`.
* **Built-In Offline Mock Mode:** Zero-network stateful simulation via `KurozoraKit.mock()` for Compose Previews, UI tests, and demos.
* **Multi-Sink Logging:** Configurable console, rotating file, in-memory ring buffer, and custom `Logger` sinks with automatic auth header redaction.

## Usage
KurozoraKit can be initialized as simply as:

```kotlin
val kurozoraKit = KurozoraKit.Builder()
    .apiKey("your_api_key")
    .platform(getPlatform())
    .userAgent(UserAgent(appName = "your_app_name", appID = "com.username.kurozora", platformName = "your_platform_name", platformVersion = "your_platform_version"))
    .build()
```

KurozoraKit allows you to set your own API endpoint. For example, if you have a custom API endpoint for debugging purposes, you can set it like this:

```kotlin
val kurozoraKit = KurozoraKit.Builder()
    .apiEndpoint(KurozoraApi.V1.baseUrl)
    .apiKey("your_api_key")
    .platform(getPlatform())
    .userAgent(UserAgent(appName = "your_app_name", appID = "com.username.kurozora", platformName = "your_platform_name", platformVersion = "your_platform_version"))
    .build()
```

KurozoraKit also accepts a `TokenProvider` object to enable and manage user accounts. For example, you can do something like the following:

```kotlin
// Prepare token provider.
object KurozoraTokenProvider: TokenProvider {
    val accountManager = GlobalContext.get().get<AccountManager>()

    override suspend fun saveToken(user: AccountUser) {
        accountManager.addAccount(user)
    }

    override suspend fun getToken(): String? {
        return accountManager.activeAccount.value?.token
    }
}

// Pass KurozoraTokenProvider
val kurozoraKit = KurozoraKit.Builder()
    .apiEndpoint(KurozoraApi.V1.baseUrl)
    .apiKey("your_api_key")
    .userAgent(UserAgent(appName = "your_app_name", appID = "com.username.kurozora", platformName = "your_platform_name", platformVersion = "your_platform_version"))
    .tokenProvider(KurozoraTokenProvider)
    .platform(getPlatform())
    .build()
```
An example of [AccountManager](https://github.com/Kurozora/kurozora-android/blob/master/composeApp/src/commonMain/kotlin/app/kurozora/core/settings/AccountManager.kt) can be found on the [Kurozora Android](https://github.com/Kurozora/kurozora-android) repo.

After setting up KurozoraKit you can use an API by calling its repository method. For example, to get the explore page data, you do the following:

```kotlin
val genreId = "1"

val exploreResponse = kurozoraKit.explore()
    .getExplore(genreId = genreId)
    .onSuccess { res ->
        // Handle success case…
    }
```

### Offline Mock Mode

For Compose Previews, instrumented/UI tests, or offline development, you can instantiate a stateful in-memory mock instance with zero network requests:

```kotlin
val mockKit = KurozoraKit.mock {
    // Optional: configure simulated latency, error injection, 2FA, or custom routes
}

val exploreResponse = mockKit.explore().getExplore()
```

# Contributing

Contributions are welcome! Feel free to open an [Issue](https://github.com/Kurozora/kurozorakit-android/issues) or submit a [Pull Request](https://github.com/Kurozora/kurozorakit-android/pulls) on GitHub.

# Security

Read our [Security Policy](SECURITY.md) to learn about reporting security issues.

# Getting in Touch

If you have any questions or just want to say hi, join the Kurozora [Discord](https://discord.gg/f3QFzGqsah) and drop a message on the #development channel.

# Code of Conduct

This project has a [Code of Conduct](CODE_OF_CONDUCT.md). By interacting with this repository, or community you agree to abide by its terms.

# More by Kurozora

- [KurozoraKit (Swift)](https://github.com/Kurozora/kurozorakit) — Simple to use framework for interacting with the Kurozora API on Apple platforms
- [Kurozora Android App](https://github.com/Kurozora/kurozora-android) — Android client app
- [Kurozora Discord Bot](https://github.com/Kurozora/kurozora-discord-bot) — A versatile Discord bot with access to Kurozora services
- [Kurozora iOS App](https://github.com/Kurozora/kurozora-app) — iOS/iPadOS/MacOS client app
- [Kurozora Linux App](https://github.com/Kurozora/kurozora-linux) — Linux client app
- [Kurozora Web](https://github.com/Kurozora/kurozora-web) — Home to the Kurozora website and API
- [Kurozora Web Extension](https://github.com/Kurozora/kurozora-extension) — Anime, Manga and Game search engine for FireFox and Chrome

# License

KurozoraKit is an Open Source project covered by the [MIT License](LICENSE).
