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

KurozoraKit has been tested to work on Android 15.0+. It also works best with Kotlin 5.0+

To use KurozoraKit in your project, you need to install it first.

## Installation

### Gradle

KurozoraKit is also available through [Gradle Build Tool](https://gradle.org/). To install it, simply add the package to your `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("app.kurozora:kurozorakit:1.0.0")
}
```

## Usage
KurozoraKit can be initialized as simply as:

```kotlin
val kurozoraKit = KurozoraKit.Builder()
    .apiKey("your_api_key")
    .build()
```

KurozoraKit allows you to set your own API endpoint. For example, if you have a custom API endpoint for debugging purposes, you can set it like this:

```kotlin
val kurozoraKit = KurozoraKit.Builder()
    .apiEndpoint(KurozoraApi.V1.baseUrl)
    .apiKey("your_api_key")
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
    .tokenProvider(KurozoraTokenProvider)
    .platform(getPlatform())
    .build()
```
An example of [AccountManager](https://github.com/Kurozora/kurozora-android/blob/master/composeApp/src/commonMain/kotlin/app/kurozora/core/settings/AccountManager.kt) can be found on the [Kurozora Android](https://github.com/Kurozora/kurozora-android) repo.

After setting up KurozoraKit you can use an API by calling its own method. For example, to get the explore page data, you do the following:

```kotlin
val genreId = "1"

val kurozoraKit = KurozoraKit.explore()
    .getExplore(genreId = genreId)
    .onSuccess { res ->
        // Handle success case…
    }
```

# Contributing

Read the [Contributing Guide](CONTRIBUTING) to learn about reporting issues, contributing code, and more ways to contribute.

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
