# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2026-11-26

### Breaking Changes
- Removed the `:store` module along with Store5, SQLDelight, and `atomicfu` dependencies (`DatabaseDriverFactory`, `KurozoraStore`, `Builder.databaseDriverFactory`, and `kit.store()`). All 27 repositories now execute directly against `KurozoraApiClient`.
- Extracted domain repositories into a dedicated `:domain` module (`app.kurozora:kurozorakit-domain`).
- Upgraded `getShowStaff`, `getLiteratureStaff`, and `getGameStaff` to return `Result<StaffResponse>`.
- Updated `Literature.Relationships` and `Game.Relationships` to use full `CharacterResponse` and `StaffResponse` models.
- Made `User.Attributes.activityStatus` nullable.

### Added
- **Charts Repository (`kit.charts()`):** Full support for all 8 ranked chart endpoints (anime, manga, games, episodes, songs, characters, people, studios).
- **Offline Mock Mode (`KurozoraKit.mock()`):** Built-in stateful in-memory mock engine (`KurozoraMockEngine`, `KurozoraMockState`, `KurozoraMockConfig`) and `Builder.httpClientEngine(...)` injection.
- **Parental Guide APIs (`kit.parentalGuide()`):** Full CRUD, voting, and reporting support for content advisories.
- **Two-Factor Authentication (2FA):** `SignInResult` sealed class, `TwoFactorChallenge` endpoint, and `submitTwoFactorChallenge(...)` in `AuthRepository`.
- **Expanded API v1 Parity:**
  - Blocked users list (`getMyBlockedList()` and `isBlockedBy`), feed re-share/quote/report, batch delete notifications, theme download (`ByteArray`), paginated achievements, and batch library operations.
  - Trailers, museum/year archives, editorial, videos, gallery, anime mappings, song lyrics, person relationships, studio predecessors/successors, review voting/reporting/revisions, `/me` digest/settings/up-next/watched/recap-stats/library-sync/batch-token-and-session deletion, and batch `getSeasons`, `getUsers`, and cast hydration.
- **Multi-Sink Logging Architecture:** Console, rotating file, in-memory ring buffer, and custom `Logger` sinks integrated into `KurozoraKit.Builder`.
- **20 New Data Models & 14 Resilient Enum Serializers:** Safe fallback deserialization for unknown backend enum values.
- **Comprehensive Unit Test Suite:** Contract, client, mock mode, and serialization test suites across `:api`, `:core`, `:data`, and `:domain`, plus an opt-in `LiveApiSmokeTest`.

### Fixed & Changed
- Normalized pagination cursor URLs in `KurozoraApiClient` (`resolveUrl` and `appendMissingParameters`) to prevent duplicate `/v1/v1/` path segments or query parameters.
- Fixed Ktor bearer token caching issue across account/session switches.
- Enabled lenient/coercing JSON deserialization, gated HTTP body logging behind `LogLevel.DEBUG` with header redaction, ordered `HttpRequestRetry` before `HttpTimeout`, and added `DELETE` request body support.
- Fixed root Maven POM metadata (`name`, `description`, `master` LICENSE URL), replaced deprecated `.capitalize()`, declared `:domain` explicitly in root dependencies, and added unit test gating to the Maven Central publish workflow.

## [1.2.5] - 2026-02-11

### Added
- Unified SDK Maven Central publishing configuration and complete Dokka API documentation across modules.
- Custom `UserAgent` support during `KurozoraKit` initialization with extended platform fields.
- Domain enum definitions and updated filter enum field types.

### Fixed & Changed
- Migrated date serialization from `LocalDate` to `Instant` with a custom serializer.
- Fixed `FeedRepository` behavior and ensured library values are serialized as `String` when building `FormData`.

## [1.2.4] - 2025-11-27

### Added
- Initial Maven Central publishing setup under `app.kurozora:kurozorakit`.
- Staff endpoints, batch fetching endpoints in `KKEndpoint`, simplified `TokenProvider`, and `Platform` abstraction.
- Project governance files (`LICENSE`, `SECURITY.md`, `CODE_OF_CONDUCT.md`, `README.md`) and Gradle Wrapper 8.14.

[2.0.0]: https://github.com/Kurozora/kurozorakit-android/compare/1.2.5...2.0.0
[1.2.5]: https://github.com/Kurozora/kurozorakit-android/compare/1.2.4...1.2.5
[1.2.4]: https://github.com/Kurozora/kurozorakit-android/releases/tag/1.2.4
