package kurozorakit.core.mock

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kurozorakit.data.enums.KKLibrary
import kurozorakit.data.enums.ParentalGuideCategory
import kurozorakit.data.enums.ParentalGuideDepiction
import kurozorakit.data.enums.ParentalGuideFrequency
import kurozorakit.data.enums.ParentalGuideRating
import kurozorakit.data.enums.ReviewRecommendation
import kurozorakit.data.models.airseason.AirSeasonResponse
import kurozorakit.data.models.character.CharacterIdentity
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.character.CharacterResponse
import kurozorakit.data.models.digest.WeeklyDigestSectionResponse
import kurozorakit.data.models.editorial.EditorialResponse
import kurozorakit.data.models.episode.EpisodeIdentity
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.episode.EpisodeResponse
import kurozorakit.data.models.explore.ExploreCategoryResponse
import kurozorakit.data.models.favorite.library.FavoriteLibrary
import kurozorakit.data.models.favorite.library.FavoriteLibraryResponse
import kurozorakit.data.models.feed.message.FeedMessage
import kurozorakit.data.models.feed.message.FeedMessageIdentity
import kurozorakit.data.models.feed.message.FeedMessageIdentityResponse
import kurozorakit.data.models.feed.message.FeedMessageResponse
import kurozorakit.data.models.game.GameIdentity
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.game.GameResponse
import kurozorakit.data.models.genre.GenreResponse
import kurozorakit.data.models.language.LanguageResponse
import kurozorakit.data.models.language.MediaLanguageResponse
import kurozorakit.data.models.legal.LegalResponse
import kurozorakit.data.models.library.Library
import kurozorakit.data.models.library.LibraryResponse
import kurozorakit.data.models.library.LibrarySyncEntry
import kurozorakit.data.models.library.LibrarySyncResponse
import kurozorakit.data.models.literature.LiteratureIdentity
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.literature.LiteratureResponse
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaResponse
import kurozorakit.data.models.misc.SettingsResponse
import kurozorakit.data.models.parentalguide.ParentalGuideEntry
import kurozorakit.data.models.parentalguide.ParentalGuideEntryResponse
import kurozorakit.data.models.parentalguide.ParentalGuideResponse
import kurozorakit.data.models.parentalguide.ParentalGuideResponseData
import kurozorakit.data.models.parentalguide.ParentalGuideStats
import kurozorakit.data.models.person.PersonIdentity
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.person.PersonRelationshipResponse
import kurozorakit.data.models.person.PersonResponse
import kurozorakit.data.models.player.PlayerResponse
import kurozorakit.data.models.provider.ProviderResponse
import kurozorakit.data.models.recap.RecapResponse
import kurozorakit.data.models.recap.item.RecapItemResponse
import kurozorakit.data.models.recap.stat.RecapStatResponse
import kurozorakit.data.models.review.MediaRatingRevisionResponse
import kurozorakit.data.models.review.RatingCategoryResponse
import kurozorakit.data.models.review.Review
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.review.ReviewVoteResponse
import kurozorakit.data.models.review.ReviewVoteUpdate
import kurozorakit.data.models.schedule.ScheduleResponse
import kurozorakit.data.models.search.Search
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.SearchSuggesitonsResponse
import kurozorakit.data.models.season.SeasonIdentity
import kurozorakit.data.models.season.SeasonIdentityResponse
import kurozorakit.data.models.season.SeasonResponse
import kurozorakit.data.models.show.ShowIdentity
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.show.ShowResponse
import kurozorakit.data.models.show.cast.CastIdentity
import kurozorakit.data.models.show.cast.CastIdentityResponse
import kurozorakit.data.models.show.cast.CastResponse
import kurozorakit.data.models.show.mapping.AnimeMappingResponse
import kurozorakit.data.models.show.related.RelatedGameResponse
import kurozorakit.data.models.show.related.RelatedLiteratureResponse
import kurozorakit.data.models.show.related.RelatedShowResponse
import kurozorakit.data.models.show.song.ShowSongResponse
import kurozorakit.data.models.song.SongIdentity
import kurozorakit.data.models.song.SongIdentityResponse
import kurozorakit.data.models.song.SongResponse
import kurozorakit.data.models.museum.MuseumItem
import kurozorakit.data.models.museum.MuseumItemResponse
import kurozorakit.data.models.museum.MuseumYear
import kurozorakit.data.models.museum.MuseumYearResponse
import kurozorakit.data.models.song.lyric.SongLyricResponse
import kurozorakit.data.models.staff.StaffIdentity
import kurozorakit.data.models.staff.StaffIdentityResponse
import kurozorakit.data.models.staff.StaffResponse
import kurozorakit.data.models.store.StoreTransactionResponse
import kurozorakit.data.models.studio.StudioIdentity
import kurozorakit.data.models.studio.StudioIdentityResponse
import kurozorakit.data.models.studio.StudioResponse
import kurozorakit.data.models.theme.ThemeResponse
import kurozorakit.data.models.theme.app.AppThemeResponse
import kurozorakit.data.models.user.UserIdentity
import kurozorakit.data.models.user.UserIdentityResponse
import kurozorakit.data.models.user.UserResponse
import kurozorakit.data.models.user.achievement.AchievementResponse
import kurozorakit.data.models.user.notification.SingleNotificationResponse
import kurozorakit.data.models.user.notification.UserNotificationResponse
import kurozorakit.data.models.user.reminder.library.ReminderLibrary
import kurozorakit.data.models.user.reminder.library.ReminderLibraryResponse
import kurozorakit.data.models.user.session.SessionIdentity
import kurozorakit.data.models.user.session.SessionIdentityResponse
import kurozorakit.data.models.user.session.SessionResponse
import kurozorakit.data.models.user.settings.UserSettingResponse
import kurozorakit.data.models.user.timeout.TimeoutResponse
import kurozorakit.data.models.user.tokens.AccessTokenResponse
import kurozorakit.data.models.video.VideoResponse
import kurozorakit.shared.ApiError
import kurozorakit.shared.Meta
import kurozorakit.shared.MetaResponse
import kotlin.random.Random

/**
 * Stateful Ktor [HttpClientEngine] that simulates the complete Kurozora v1.15.0 REST API in memory.
 *
 * Used automatically when [kurozorakit.core.KurozoraKit.Builder.mockMode] is enabled.
 */
object KurozoraMockEngine {

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    /** Creates a Ktor [MockEngine] backed by [config] and [state]. */
    fun create(
        config: KurozoraMockConfig,
        state: KurozoraMockState,
    ): HttpClientEngine = MockEngine { request ->
        val ctx = buildContext(request)
        state.withLock { recordedRequests += ctx }

        // 1. Simulated latency & jitter
        val extraJitter = if (config.jitterMs > 0) Random.nextLong(0, config.jitterMs + 1) else 0L
        val totalDelay = config.latencyMs + extraJitter
        if (totalDelay > 0L) delay(totalDelay)

        // 2. Explicit status override rules
        val matchedStatus = config.statusOverrides.entries.firstOrNull { (key, _) ->
            ctx.path.contains(key)
        }?.value
        if (matchedStatus != null) {
            return@MockEngine respondError(matchedStatus, "Simulated HTTP $matchedStatus for ${ctx.path}")
        }

        // 3. Chaos error rate
        if (config.errorRate > 0.0 && Random.nextDouble() < config.errorRate) {
            return@MockEngine respondError(500, "Simulated chaos server error")
        }

        // 4. Custom route handler overrides
        val customHandler = config.customHandlers.entries.firstOrNull { (key, _) ->
            ctx.path.contains(key)
        }?.value
        if (customHandler != null) {
            val customResp = customHandler.handle(ctx, state)
            return@MockEngine toHttpResponse(customResp)
        }

        // 5. Optional auth gate for /me/* endpoints
        if (config.requireAuthForMeEndpoints && (ctx.path == "me" || ctx.path.startsWith("me/")) && !ctx.isAuthenticated) {
            return@MockEngine respondError(401, "Authentication required for /${ctx.path}")
        }

        // 6. Full Kurozora v1.15.0 router
        val response = route(ctx, config, state)
        toHttpResponse(response)
    }

    private fun io.ktor.client.engine.mock.MockRequestHandleScope.toHttpResponse(resp: MockRouteResponse) =
        if (resp.binaryBody != null) {
            respond(
                content = resp.binaryBody,
                status = HttpStatusCode.fromValue(resp.statusCode),
                headers = headersOf(HttpHeaders.ContentType, resp.contentType),
            )
        } else {
            respond(
                content = resp.body,
                status = HttpStatusCode.fromValue(resp.statusCode),
                headers = headersOf(HttpHeaders.ContentType, resp.contentType),
            )
        }

    private fun io.ktor.client.engine.mock.MockRequestHandleScope.respondError(code: Int, detail: String) =
        respond(
            content = json.encodeToString(
                MetaResponse(
                    meta = Meta(version = "1.15.0", minimumAppVersion = "1.0.0", isMaintenanceModeEnabled = false, isUserAuthenticated = false),
                    errors = listOf(ApiError(id = code, status = code, title = "Mock Error", detail = detail)),
                )
            ),
            status = HttpStatusCode.fromValue(code),
            headers = headersOf(HttpHeaders.ContentType, "application/json"),
        )

    private fun buildContext(request: HttpRequestData): MockRequestContext {
        val rawPath = request.url.encodedPath.trimStart('/')
        val path = rawPath.removePrefix("v1/").trimEnd('/')
        val body = request.body
        val formMap = (body as? FormDataContent)?.formData?.entries()?.associate { it.key to it.value } ?: emptyMap()
        val textBody = when (body) {
            is TextContent -> body.text
            is ByteArrayContent -> body.bytes().decodeToString()
            else -> null
        }
        val headersMap = request.headers.entries().associate { it.key to it.value }
        val authHeader = request.headers[HttpHeaders.Authorization]
        return MockRequestContext(
            method = request.method.value.uppercase(),
            path = path,
            queryParameters = request.url.parameters,
            formParameters = formMap,
            textBody = textBody,
            headers = headersMap,
            isAuthenticated = authHeader?.startsWith("Bearer ", ignoreCase = true) == true,
        )
    }

    private fun ok(body: String) = MockRouteResponse(statusCode = 200, body = body)
    private fun notFound(path: String) = MockRouteResponse(
        statusCode = 404,
        body = json.encodeToString(
            MetaResponse(
                meta = Meta(version = "1.15.0", minimumAppVersion = "1.0.0", isMaintenanceModeEnabled = false, isUserAuthenticated = false),
                errors = listOf(ApiError(id = 40400, status = 404, title = "Not Found", detail = "Mock resource not found: $path")),
            )
        ),
    )

    private fun route(ctx: MockRequestContext, config: KurozoraMockConfig, state: KurozoraMockState): MockRouteResponse {
        val segs = ctx.path.split('/').filter { it.isNotEmpty() }
        val m = ctx.method

        // --- Misc / Legal / Catalogs ---
        when (ctx.path) {
            "info" -> return ok(
                json.encodeToString(
                    MetaResponse(
                        meta = Meta(version = "1.15.0", minimumAppVersion = "1.0.0", isMaintenanceModeEnabled = false, isUserAuthenticated = ctx.isAuthenticated),
                        errors = emptyList(),
                    )
                )
            )
            "settings" -> return ok(json.encodeToString(SettingsResponse(state.buildAppSettings())))
            "players" -> return ok(json.encodeToString(PlayerResponse(state.players)))
            "providers" -> return ok(json.encodeToString(ProviderResponse(state.providers)))
            "languages" -> return ok(json.encodeToString(LanguageResponse(state.languages)))
            "legal/privacy-policy" -> return ok(json.encodeToString(LegalResponse(state.buildLegal("Privacy Policy"))))
            "legal/terms-of-use" -> return ok(json.encodeToString(LegalResponse(state.buildLegal("Terms of Use"))))
            "schedule" -> return ok(json.encodeToString(ScheduleResponse(state.buildSchedules(), next = null)))
            "images/random" -> {
                val limit = ctx.queryParameters["limit"]?.toIntOrNull()?.coerceAtLeast(1) ?: 1
                val imgs = (1..limit).map { idx ->
                    Media(url = "https://cdn.kurozora.app/mock/random/$idx.jpg", width = 1000, height = 1500, backgroundColor = "#1B263B")
                }
                return ok(json.encodeToString(MediaResponse(imgs)))
            }
            "genres" -> {
                val ids = ctx.queryParameters["ids"]?.split(",")?.toSet()
                val list = if (ids != null) state.genres.filter { it.id in ids } else state.genres
                return ok(json.encodeToString(GenreResponse(list)))
            }
            "themes" -> {
                val ids = ctx.queryParameters["ids"]?.split(",")?.toSet()
                val list = if (ids != null) state.themes.filter { it.id in ids } else state.themes
                return ok(json.encodeToString(ThemeResponse(list)))
            }
            "theme-store" -> return ok(json.encodeToString(AppThemeResponse(state.appThemes)))
            "explore" -> return ok(json.encodeToString(ExploreCategoryResponse(state.exploreCategories)))
            "search" -> return handleSearch(ctx, state)
            "search/suggestions" -> return handleSearchSuggestions(ctx, state)
            "anime/seasons", "manga/seasons", "games/seasons" ->
                return ok(json.encodeToString(AirSeasonResponse(state.buildAirSeasons(), next = null)))
            "anime/mapping" -> return ok(json.encodeToString(AnimeMappingResponse(state.buildAnimeMappings(), next = null)))
            "anime/upcoming" -> return ok(json.encodeToString(ShowIdentityResponse(state.shows.map { ShowIdentity(it.id, it.type, it.href) }, next = null)))
            "manga/upcoming", "manga/adapted" -> return ok(json.encodeToString(LiteratureIdentityResponse(state.literatures.map { LiteratureIdentity(it.id, it.type, it.href) }, next = null)))
            "games/upcoming", "games/adapted" -> return ok(json.encodeToString(GameIdentityResponse(state.games.map { GameIdentity(it.id, it.type, it.href) }, next = null)))
            "reviews/categories" -> return ok(json.encodeToString(RatingCategoryResponse(state.buildRatingCategories())))
            "store/verify", "store/restore-order" -> return ok("""{"data":[{"type":"receipts","attributes":{"isValid":true,"needsRefresh":false}}]}""")
            "store/transactions" -> return ok(json.encodeToString(StoreTransactionResponse(state.storeTransactions, next = null)))
            "store/purchases/google-play/verify" -> return ok("""{"status":"verified"}""")
        }

        if (segs.isEmpty()) return notFound(ctx.path)

        return when (segs[0]) {
            "languages" -> {
                val lang = state.languages.firstOrNull { it.code.equals(segs.getOrNull(1), ignoreCase = true) }
                    ?: state.languages.first()
                ok(json.encodeToString(LanguageResponse(listOf(lang))))
            }
            "genres" -> {
                val g = state.genres.firstOrNull { it.id == segs.getOrNull(1) } ?: return notFound(ctx.path)
                ok(json.encodeToString(GenreResponse(listOf(g))))
            }
            "themes" -> {
                val t = state.themes.firstOrNull { it.id == segs.getOrNull(1) } ?: return notFound(ctx.path)
                ok(json.encodeToString(ThemeResponse(listOf(t))))
            }
            "theme-store" -> {
                val id = segs.getOrNull(1) ?: return notFound(ctx.path)
                if (segs.getOrNull(2) == "download") {
                    MockRouteResponse(200, contentType = "application/octet-stream", binaryBody = "KUROZORA_MOCK_THEME_$id".encodeToByteArray())
                } else {
                    val t = state.appThemes.firstOrNull { it.id == id } ?: return notFound(ctx.path)
                    ok(json.encodeToString(AppThemeResponse(listOf(t))))
                }
            }
            "explore" -> {
                val id = segs.getOrNull(1)
                val cat = state.exploreCategories.firstOrNull { it.id == id || it.attributes.slug == id }
                    ?: state.exploreCategories.first()
                ok(json.encodeToString(ExploreCategoryResponse(listOf(cat))))
            }
            "charts" -> handleCharts(segs, ctx, state)
            "anime" -> handleShows(segs, ctx, state)
            "manga" -> handleLiteratures(segs, ctx, state)
            "games" -> handleGames(segs, ctx, state)
            "characters" -> handleCharacters(segs, ctx, state)
            "people" -> handlePeople(segs, ctx, state)
            "studios" -> handleStudios(segs, ctx, state)
            "songs" -> handleSongs(segs, ctx, state)
            "seasons" -> handleSeasons(segs, ctx, state)
            "episodes" -> handleEpisodes(segs, ctx, state)
            "cast", "show-cast", "literature-cast", "game-cast" ->
                ok(json.encodeToString(CastResponse(state.buildCastList(), next = null)))
            "reviews" -> handleReviews(segs, ctx, state)
            "feed" -> handleFeed(segs, ctx, state)
            "parentalguide" -> handleParentalGuideEntry(segs, ctx, state)
            "users" -> handleUsers(segs, ctx, config, state)
            "me" -> handleMe(segs, ctx, state)
            else -> notFound(ctx.path)
        }
    }

    // =========================================================================
    // Search
    // =========================================================================

    private fun handleSearch(ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        val q = ctx.queryParameters["query"]?.trim().orEmpty()
        val limit = ctx.queryParameters["limit"]?.toIntOrNull() ?: 20
        val types = ctx.queryParameters.getAll("types[]")?.toSet()
        val wantAll = types.isNullOrEmpty()

        val matchedShows = if (wantAll || "shows" in types!!) {
            state.shows.filter { q.isEmpty() || it.attributes.title.contains(q, ignoreCase = true) }
                .take(limit).map { ShowIdentity(it.id, it.type, it.href) }
        } else null
        val matchedLits = if (wantAll || "literatures" in types!!) {
            state.literatures.filter { q.isEmpty() || it.attributes.title.contains(q, ignoreCase = true) }
                .take(limit).map { LiteratureIdentity(it.id, it.type, it.href) }
        } else null
        val matchedGames = if (wantAll || "games" in types!!) {
            state.games.filter { q.isEmpty() || it.attributes.title.contains(q, ignoreCase = true) }
                .take(limit).map { GameIdentity(it.id, it.type, it.href) }
        } else null
        val matchedChars = if (wantAll || "characters" in types!!) {
            state.characters.filter { q.isEmpty() || it.attributes.name.contains(q, ignoreCase = true) }
                .take(limit).map { CharacterIdentity(it.id, it.type, it.href) }
        } else null
        val matchedPeople = if (wantAll || "people" in types!!) {
            state.people.filter { q.isEmpty() || it.attributes.fullName.contains(q, ignoreCase = true) }
                .take(limit).map { PersonIdentity(it.id, it.type, it.href) }
        } else null
        val matchedStudios = if (wantAll || "studios" in types!!) {
            state.studios.filter { q.isEmpty() || it.attributes.name.contains(q, ignoreCase = true) }
                .take(limit).map { StudioIdentity(it.id, it.type, it.href) }
        } else null
        val matchedSongs = if (wantAll || "songs" in types!!) {
            state.songs.filter { q.isEmpty() || it.attributes.title.contains(q, ignoreCase = true) }
                .take(limit).map { SongIdentity(it.id, it.type, it.href) }
        } else null
        val matchedEpisodes = if (wantAll || "episodes" in types!!) {
            state.episodes.filter { q.isEmpty() || it.attributes.title.contains(q, ignoreCase = true) }
                .take(limit).map { EpisodeIdentity(it.id, it.type, it.href) }
        } else null
        val matchedUsers = if (wantAll || "users" in types!!) {
            state.users.filter { q.isEmpty() || it.attributes.username.contains(q, ignoreCase = true) || it.attributes.slug.contains(q, ignoreCase = true) }
                .take(limit).map { UserIdentity(it.id, it.type, it.href) }
        } else null

        val result = SearchResponse(
            data = Search(
                shows = matchedShows?.let { ShowIdentityResponse(it, next = null) },
                literatures = matchedLits?.let { LiteratureIdentityResponse(it, next = null) },
                games = matchedGames?.let { GameIdentityResponse(it, next = null) },
                characters = matchedChars?.let { CharacterIdentityResponse(it, next = null) },
                people = matchedPeople?.let { PersonIdentityResponse(it, next = null) },
                studios = matchedStudios?.let { StudioIdentityResponse(it, next = null) },
                songs = matchedSongs?.let { SongIdentityResponse(it, next = null) },
                episodes = matchedEpisodes?.let { EpisodeIdentityResponse(it, next = null) },
                users = matchedUsers?.let { UserIdentityResponse(it, next = null) },
            )
        )
        return ok(json.encodeToString(result))
    }

    private fun handleSearchSuggestions(ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        val q = ctx.queryParameters["query"]?.trim().orEmpty()
        val candidates = (state.shows.map { it.attributes.title } +
            state.literatures.map { it.attributes.title } +
            state.games.map { it.attributes.title } +
            state.characters.map { it.attributes.name })
            .filter { q.isEmpty() || it.contains(q, ignoreCase = true) }
            .distinct()
            .take(10)
        return ok(json.encodeToString(SearchSuggesitonsResponse(candidates)))
    }

    // =========================================================================
    // Media Catalogs (Shows, Literatures, Games, Characters, People, Studios, Songs)
    // =========================================================================

    private fun handleCharts(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        val limit = ctx.queryParameters["limit"]?.toIntOrNull() ?: 25
        return when (segs.getOrNull(1)) {
            "anime" -> ok(json.encodeToString(ShowIdentityResponse(state.shows.take(limit).map { ShowIdentity(it.id, it.type, it.href) }, next = null)))
            "manga" -> ok(json.encodeToString(LiteratureIdentityResponse(state.literatures.take(limit).map { LiteratureIdentity(it.id, it.type, it.href) }, next = null)))
            "games" -> ok(json.encodeToString(GameIdentityResponse(state.games.take(limit).map { GameIdentity(it.id, it.type, it.href) }, next = null)))
            "episodes" -> ok(json.encodeToString(EpisodeIdentityResponse(state.episodes.take(limit).map { EpisodeIdentity(it.id, it.type, it.href) }, next = null)))
            "songs" -> ok(json.encodeToString(SongIdentityResponse(state.songs.take(limit).map { SongIdentity(it.id, it.type, it.href) }, next = null)))
            "characters" -> ok(json.encodeToString(CharacterIdentityResponse(state.characters.take(limit).map { CharacterIdentity(it.id, it.type, it.href) }, next = null)))
            "people" -> ok(json.encodeToString(PersonIdentityResponse(state.people.take(limit).map { PersonIdentity(it.id, it.type, it.href) }, next = null)))
            "studios" -> ok(json.encodeToString(StudioIdentityResponse(state.studios.take(limit).map { StudioIdentity(it.id, it.type, it.href) }, next = null)))
            else -> notFound(ctx.path)
        }
    }

    private fun handleShows(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 1) {
            val ids = ctx.queryParameters["ids"]?.split(",")?.toSet()
            return if (ids != null) {
                val matched = state.shows.filter { it.id in ids }.map { state.showWithUserLibrary(it) }
                ok(json.encodeToString(ShowResponse(matched, next = null)))
            } else {
                val limit = ctx.queryParameters["limit"]?.toIntOrNull() ?: 20
                ok(json.encodeToString(SearchResponse(Search(shows = ShowIdentityResponse(state.shows.take(limit).map { ShowIdentity(it.id, it.type, it.href) }, next = null)))))
            }
        }
        val id = segs[1]
        if (id == "trailers") {
            return ok(json.encodeToString(VideoResponse(state.buildVideos(state.shows.first().id), next = null)))
        }
        if (id == "museum") {
            return if (segs.size >= 3) {
                ok(json.encodeToString(MuseumItemResponse(state.shows.map { MuseumItem(it.id, it.type, it.href, MuseumItem.Attributes(it.attributes.title, it.attributes.poster)) }, next = null)))
            } else {
                ok(json.encodeToString(MuseumYearResponse(listOf(MuseumYear(2024, state.shows.size)), next = null)))
            }
        }
        val show = state.shows.firstOrNull { it.id == id || it.attributes.slug == id } ?: return notFound(ctx.path)
        return when (segs.getOrNull(2)) {
            null -> ok(json.encodeToString(ShowResponse(listOf(state.showWithUserLibrary(show)), next = null)))
            "cast" -> ok(json.encodeToString(CastIdentityResponse(state.buildCastList().map { CastIdentity(it.id, it.type, it.href) }, next = null)))
            "staff" -> ok(json.encodeToString(StaffResponse(state.buildStaffList(), next = null)))
            "characters" -> ok(json.encodeToString(CharacterIdentityResponse(state.characters.map { CharacterIdentity(it.id, it.type, it.href) }, next = null)))
            "videos" -> ok(json.encodeToString(VideoResponse(state.buildVideos(show.id), next = null)))
            "languages" -> ok(json.encodeToString(MediaLanguageResponse(state.buildMediaLanguages())))
            "editorial" -> ok(json.encodeToString(EditorialResponse(state.buildEditorial(show.id))))
            "seasons" -> ok(json.encodeToString(SeasonIdentityResponse(state.seasons.map { SeasonIdentity(it.id, it.type, it.href) }, next = null)))
            "songs" -> ok(json.encodeToString(ShowSongResponse(state.buildShowSongs(), next = null)))
            "studios" -> ok(json.encodeToString(StudioResponse(state.studios.take(1), next = null)))
            "more-by-studio" -> ok(json.encodeToString(ShowIdentityResponse(state.shows.filter { it.id != show.id }.map { ShowIdentity(it.id, it.type, it.href) }, next = null)))
            "related-shows" -> ok(json.encodeToString(RelatedShowResponse(emptyList(), next = null)))
            "related-literatures" -> ok(json.encodeToString(RelatedLiteratureResponse(emptyList(), next = null)))
            "related-games" -> ok(json.encodeToString(RelatedGameResponse(emptyList(), next = null)))
            "reviews" -> ok(json.encodeToString(ReviewResponse(state.reviews, next = null)))
            "rate" -> handleRate(ctx, state, showId = show.id)
            "parentalguide" -> handleParentalGuideMedia(ctx, state)
            else -> notFound(ctx.path)
        }
    }

    private fun handleLiteratures(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 1) {
            val ids = ctx.queryParameters["ids"]?.split(",")?.toSet()
            return if (ids != null) {
                val matched = state.literatures.filter { it.id in ids }.map { state.literatureWithUserLibrary(it) }
                ok(json.encodeToString(LiteratureResponse(matched, next = null)))
            } else {
                val limit = ctx.queryParameters["limit"]?.toIntOrNull() ?: 20
                ok(json.encodeToString(SearchResponse(Search(literatures = LiteratureIdentityResponse(state.literatures.take(limit).map { LiteratureIdentity(it.id, it.type, it.href) }, next = null)))))
            }
        }
        val id = segs[1]
        if (id == "museum") {
            return if (segs.size >= 3) {
                ok(json.encodeToString(MuseumItemResponse(state.literatures.map { MuseumItem(it.id, it.type, it.href, MuseumItem.Attributes(it.attributes.title, it.attributes.poster)) }, next = null)))
            } else {
                ok(json.encodeToString(MuseumYearResponse(listOf(MuseumYear(2024, state.literatures.size)), next = null)))
            }
        }
        val lit = state.literatures.firstOrNull { it.id == id || it.attributes.slug == id } ?: return notFound(ctx.path)
        return when (segs.getOrNull(2)) {
            null -> ok(json.encodeToString(LiteratureResponse(listOf(state.literatureWithUserLibrary(lit)), next = null)))
            "gallery" -> ok(json.encodeToString(MediaResponse(listOfNotNull(lit.attributes.poster))))
            "languages" -> ok(json.encodeToString(MediaLanguageResponse(state.buildMediaLanguages())))
            "editorial" -> ok(json.encodeToString(EditorialResponse(state.buildEditorial(lit.id))))
            "cast" -> ok(json.encodeToString(CastIdentityResponse(state.buildCastList().map { CastIdentity(it.id, it.type, it.href) }, next = null)))
            "staff" -> ok(json.encodeToString(StaffResponse(state.buildStaffList(), next = null)))
            "characters" -> ok(json.encodeToString(CharacterIdentityResponse(state.characters.map { CharacterIdentity(it.id, it.type, it.href) }, next = null)))
            "studios" -> ok(json.encodeToString(StudioResponse(state.studios.take(1), next = null)))
            "more-by-studio" -> ok(json.encodeToString(LiteratureIdentityResponse(state.literatures.filter { it.id != lit.id }.map { LiteratureIdentity(it.id, it.type, it.href) }, next = null)))
            "related-shows" -> ok(json.encodeToString(RelatedShowResponse(emptyList(), next = null)))
            "related-literatures" -> ok(json.encodeToString(RelatedLiteratureResponse(emptyList(), next = null)))
            "related-games" -> ok(json.encodeToString(RelatedGameResponse(emptyList(), next = null)))
            "reviews" -> ok(json.encodeToString(ReviewResponse(state.reviews, next = null)))
            "rate" -> handleRate(ctx, state, litId = lit.id)
            "parentalguide" -> handleParentalGuideMedia(ctx, state)
            else -> notFound(ctx.path)
        }
    }

    private fun handleGames(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 1) {
            val ids = ctx.queryParameters["ids"]?.split(",")?.toSet()
            return if (ids != null) {
                val matched = state.games.filter { it.id in ids }.map { state.gameWithUserLibrary(it) }
                ok(json.encodeToString(GameResponse(matched, next = null)))
            } else {
                val limit = ctx.queryParameters["limit"]?.toIntOrNull() ?: 20
                ok(json.encodeToString(SearchResponse(Search(games = GameIdentityResponse(state.games.take(limit).map { GameIdentity(it.id, it.type, it.href) }, next = null)))))
            }
        }
        val id = segs[1]
        if (id == "trailers") {
            return ok(json.encodeToString(VideoResponse(state.buildVideos(state.games.first().id), next = null)))
        }
        if (id == "museum") {
            return if (segs.size >= 3) {
                ok(json.encodeToString(MuseumItemResponse(state.games.map { MuseumItem(it.id, it.type, it.href, MuseumItem.Attributes(it.attributes.title, it.attributes.poster)) }, next = null)))
            } else {
                ok(json.encodeToString(MuseumYearResponse(listOf(MuseumYear(2024, state.games.size)), next = null)))
            }
        }
        val game = state.games.firstOrNull { it.id == id || it.attributes.slug == id } ?: return notFound(ctx.path)
        return when (segs.getOrNull(2)) {
            null -> ok(json.encodeToString(GameResponse(listOf(state.gameWithUserLibrary(game)), next = null)))
            "cast" -> ok(json.encodeToString(CastIdentityResponse(state.buildCastList().map { CastIdentity(it.id, it.type, it.href) }, next = null)))
            "staff" -> ok(json.encodeToString(StaffResponse(state.buildStaffList(), next = null)))
            "characters" -> ok(json.encodeToString(CharacterIdentityResponse(state.characters.map { CharacterIdentity(it.id, it.type, it.href) }, next = null)))
            "songs" -> ok(json.encodeToString(ShowSongResponse(state.buildShowSongs(), next = null)))
            "languages" -> ok(json.encodeToString(MediaLanguageResponse(state.buildMediaLanguages())))
            "editorial" -> ok(json.encodeToString(EditorialResponse(state.buildEditorial(game.id))))
            "studios" -> ok(json.encodeToString(StudioResponse(state.studios.take(1), next = null)))
            "more-by-studio" -> ok(json.encodeToString(GameIdentityResponse(state.games.filter { it.id != game.id }.map { GameIdentity(it.id, it.type, it.href) }, next = null)))
            "related-shows" -> ok(json.encodeToString(RelatedShowResponse(emptyList(), next = null)))
            "related-literatures" -> ok(json.encodeToString(RelatedLiteratureResponse(emptyList(), next = null)))
            "related-games" -> ok(json.encodeToString(RelatedGameResponse(emptyList(), next = null)))
            "reviews" -> ok(json.encodeToString(ReviewResponse(state.reviews, next = null)))
            "rate" -> handleRate(ctx, state, gameId = game.id)
            "parentalguide" -> handleParentalGuideMedia(ctx, state)
            else -> notFound(ctx.path)
        }
    }

    private fun handleCharacters(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 1) {
            val ids = ctx.queryParameters["ids"]?.split(",")?.toSet()
            return if (ids != null) {
                ok(json.encodeToString(CharacterResponse(state.characters.filter { it.id in ids }, next = null)))
            } else {
                ok(json.encodeToString(SearchResponse(Search(characters = CharacterIdentityResponse(state.characters.map { CharacterIdentity(it.id, it.type, it.href) }, next = null)))))
            }
        }
        val ch = state.characters.firstOrNull { it.id == segs[1] } ?: return notFound(ctx.path)
        return when (segs.getOrNull(2)) {
            null -> ok(json.encodeToString(CharacterResponse(listOf(ch), next = null)))
            "gallery" -> ok(json.encodeToString(MediaResponse(listOfNotNull(ch.attributes.profile))))
            "people" -> ok(json.encodeToString(PersonIdentityResponse(state.people.map { PersonIdentity(it.id, it.type, it.href) }, next = null)))
            "anime" -> ok(json.encodeToString(ShowIdentityResponse(state.shows.map { ShowIdentity(it.id, it.type, it.href) }, next = null)))
            "literatures" -> ok(json.encodeToString(LiteratureIdentityResponse(state.literatures.map { LiteratureIdentity(it.id, it.type, it.href) }, next = null)))
            "games" -> ok(json.encodeToString(GameIdentityResponse(state.games.map { GameIdentity(it.id, it.type, it.href) }, next = null)))
            "reviews" -> ok(json.encodeToString(ReviewResponse(state.reviews, next = null)))
            "rate" -> handleRate(ctx, state)
            else -> notFound(ctx.path)
        }
    }

    private fun handlePeople(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 1) {
            val ids = ctx.queryParameters["ids"]?.split(",")?.toSet()
            return if (ids != null) {
                ok(json.encodeToString(PersonResponse(state.people.filter { it.id in ids }, next = null)))
            } else {
                ok(json.encodeToString(SearchResponse(Search(people = PersonIdentityResponse(state.people.map { PersonIdentity(it.id, it.type, it.href) }, next = null)))))
            }
        }
        val person = state.people.firstOrNull { it.id == segs[1] } ?: return notFound(ctx.path)
        return when (segs.getOrNull(2)) {
            null -> ok(json.encodeToString(PersonResponse(listOf(person), next = null)))
            "gallery" -> ok(json.encodeToString(MediaResponse(listOfNotNull(person.attributes.profile))))
            "relationships" -> ok(json.encodeToString(PersonRelationshipResponse(state.buildPersonRelationships(), next = null)))
            "anime" -> ok(json.encodeToString(ShowIdentityResponse(state.shows.map { ShowIdentity(it.id, it.type, it.href) }, next = null)))
            "games" -> ok(json.encodeToString(GameIdentityResponse(state.games.map { GameIdentity(it.id, it.type, it.href) }, next = null)))
            "literatures" -> ok(json.encodeToString(LiteratureIdentityResponse(state.literatures.map { LiteratureIdentity(it.id, it.type, it.href) }, next = null)))
            "characters" -> ok(json.encodeToString(CharacterIdentityResponse(state.characters.map { CharacterIdentity(it.id, it.type, it.href) }, next = null)))
            "reviews" -> ok(json.encodeToString(ReviewResponse(state.reviews, next = null)))
            "rate" -> handleRate(ctx, state)
            else -> notFound(ctx.path)
        }
    }

    private fun handleStudios(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 1) {
            val ids = ctx.queryParameters["ids"]?.split(",")?.toSet()
            return if (ids != null) {
                ok(json.encodeToString(StudioResponse(state.studios.filter { it.id in ids }, next = null)))
            } else {
                ok(json.encodeToString(SearchResponse(Search(studios = StudioIdentityResponse(state.studios.map { StudioIdentity(it.id, it.type, it.href) }, next = null)))))
            }
        }
        val studio = state.studios.firstOrNull { it.id == segs[1] } ?: return notFound(ctx.path)
        return when (segs.getOrNull(2)) {
            null -> ok(json.encodeToString(StudioResponse(listOf(studio), next = null)))
            "predecessors", "successors" -> ok(json.encodeToString(StudioIdentityResponse(state.studios.filter { it.id != studio.id }.map { StudioIdentity(it.id, it.type, it.href) }, next = null)))
            "anime" -> ok(json.encodeToString(ShowIdentityResponse(state.shows.map { ShowIdentity(it.id, it.type, it.href) }, next = null)))
            "literatures" -> ok(json.encodeToString(LiteratureIdentityResponse(state.literatures.map { LiteratureIdentity(it.id, it.type, it.href) }, next = null)))
            "games" -> ok(json.encodeToString(GameIdentityResponse(state.games.map { GameIdentity(it.id, it.type, it.href) }, next = null)))
            "reviews" -> ok(json.encodeToString(ReviewResponse(state.reviews, next = null)))
            "rate" -> handleRate(ctx, state)
            else -> notFound(ctx.path)
        }
    }

    private fun handleSongs(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 1) {
            val ids = ctx.queryParameters["ids"]?.split(",")?.toSet()
            return if (ids != null) {
                ok(json.encodeToString(SongResponse(state.songs.filter { it.id in ids }, next = null)))
            } else {
                ok(json.encodeToString(SearchResponse(Search(songs = SongIdentityResponse(state.songs.map { SongIdentity(it.id, it.type, it.href) }, next = null)))))
            }
        }
        val song = state.songs.firstOrNull { it.id == segs[1] } ?: return notFound(ctx.path)
        return when (segs.getOrNull(2)) {
            null -> ok(json.encodeToString(SongResponse(listOf(song), next = null)))
            "lyrics" -> ok(json.encodeToString(SongLyricResponse(listOf(state.buildSongLyric(song.id)))))
            "anime" -> ok(json.encodeToString(ShowIdentityResponse(state.shows.map { ShowIdentity(it.id, it.type, it.href) }, next = null)))
            "games" -> ok(json.encodeToString(GameIdentityResponse(state.games.map { GameIdentity(it.id, it.type, it.href) }, next = null)))
            "reviews" -> ok(json.encodeToString(ReviewResponse(state.reviews, next = null)))
            "rate" -> handleRate(ctx, state)
            else -> notFound(ctx.path)
        }
    }

    // =========================================================================
    // Seasons & Episodes (Stateful Watch Progress)
    // =========================================================================

    private fun handleSeasons(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 1) {
            val ids = ctx.queryParameters["ids"]?.split(",")?.toSet()
            val list = if (ids != null) state.seasons.filter { it.id in ids } else state.seasons
            return ok(json.encodeToString(SeasonResponse(list, next = null)))
        }
        val season = state.seasons.firstOrNull { it.id == segs.getOrNull(1) } ?: return notFound(ctx.path)
        return when (segs.getOrNull(2)) {
            null -> ok(json.encodeToString(SeasonResponse(listOf(season), next = null)))
            "episodes" -> ok(json.encodeToString(EpisodeIdentityResponse(state.episodes.map { EpisodeIdentity(it.id, it.type, it.href) }, next = null)))
            "watched" -> state.withLock {
                val allWatched = episodes.all { it.id in watchedEpisodeIds }
                if (allWatched) watchedEpisodeIds.clear() else watchedEpisodeIds.addAll(episodes.map { it.id })
                ok("""{"data":{"isWatched":${!allWatched}}}""")
            }
            else -> notFound(ctx.path)
        }
    }

    private fun handleEpisodes(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 1) {
            val ids = ctx.queryParameters["ids"]?.split(",")?.toSet()
            val list = if (ids != null) state.episodes.filter { it.id in ids } else state.episodes
            return ok(json.encodeToString(EpisodeResponse(list, next = null)))
        }
        val ep = state.episodes.firstOrNull { it.id == segs[1] } ?: return notFound(ctx.path)
        return when (segs.getOrNull(2)) {
            null -> ok(json.encodeToString(EpisodeResponse(listOf(ep), next = null)))
            "suggestions" -> ok(json.encodeToString(EpisodeIdentityResponse(state.episodes.filter { it.id != ep.id }.map { EpisodeIdentity(it.id, it.type, it.href) }, next = null)))
            "watched" -> state.withLock {
                if (ctx.method == "DELETE") {
                    watchedEpisodeIds.remove(ep.id)
                    ok("""{"data":{"isWatched":false}}""")
                } else {
                    val nowWatched = if (ep.id in watchedEpisodeIds) {
                        watchedEpisodeIds.remove(ep.id); false
                    } else {
                        watchedEpisodeIds.add(ep.id); true
                    }
                    ok("""{"data":{"isWatched":$nowWatched}}""")
                }
            }
            "reviews" -> ok(json.encodeToString(ReviewResponse(state.reviews, next = null)))
            "rate" -> handleRate(ctx, state)
            else -> notFound(ctx.path)
        }
    }

    // =========================================================================
    // Ratings / Reviews & Parental Guide
    // =========================================================================

    private fun handleRate(
        ctx: MockRequestContext,
        state: KurozoraMockState,
        showId: String? = null,
        litId: String? = null,
        gameId: String? = null,
    ): MockRouteResponse = state.withLock {
        if (ctx.method == "DELETE") {
            reviews.removeAll { rev ->
                rev.relationships?.users?.data?.firstOrNull()?.id == currentUserId && (
                    (showId != null && rev.relationships?.shows?.data?.any { it.id == showId } == true) ||
                    (litId != null && rev.relationships?.literatures?.data?.any { it.id == litId } == true) ||
                    (gameId != null && rev.relationships?.games?.data?.any { it.id == gameId } == true)
                )
            }
            return@withLock ok("{}")
        }
        val score = ctx.formParam("rating")?.toDoubleOrNull() ?: 4.0
        val desc = ctx.formParam("description")
        val rec = ctx.formParam("recommendation")?.toIntOrNull()?.let { ReviewRecommendation.fromInt(it) }
        val isSpoiler = ctx.formParam("isSpoiler") == "1"
        val newReview = Review(
            id = nextId(),
            type = "reviews",
            href = "/v1/reviews",
            attributes = Review.Attributes(
                score = score,
                description = desc,
                isSpoiler = isSpoiler,
                recommendation = rec,
                createdAt = LocalDate(2025, 3, 1),
            ),
            relationships = Review.Relationships(
                shows = showId?.let { ShowIdentityResponse(listOf(ShowIdentity(it, "shows", "/v1/anime/$it")), null) },
                literatures = litId?.let { LiteratureIdentityResponse(listOf(LiteratureIdentity(it, "literatures", "/v1/manga/$it")), null) },
                games = gameId?.let { GameIdentityResponse(listOf(GameIdentity(it, "games", "/v1/games/$it")), null) },
                users = UserResponse(listOf(currentUser()), next = null),
            ),
        )
        reviews.add(0, newReview)
        ok("{}")
    }

    private fun handleReviews(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        val reviewId = segs.getOrNull(1) ?: return notFound(ctx.path)
        return when (segs.getOrNull(2)) {
            null -> {
                val r = state.reviews.firstOrNull { it.id == reviewId } ?: return notFound(ctx.path)
                ok(json.encodeToString(ReviewResponse(listOf(r), next = null)))
            }
            "revisions" -> ok(json.encodeToString(MediaRatingRevisionResponse(state.buildReviewRevisions(reviewId))))
            "vote" -> state.withLock {
                val vote = when (ctx.formParam("vote")) {
                    "helpful" -> true
                    "unhelpful" -> false
                    else -> null
                }
                reviewVotes[reviewId] = vote
                ok(json.encodeToString(ReviewVoteResponse(ReviewVoteUpdate(isHelpful = vote))))
            }
            "report", "elevate" -> ok("{}")
            "delete" -> state.withLock {
                reviews.removeAll { it.id == reviewId }
                ok("{}")
            }
            else -> notFound(ctx.path)
        }
    }

    private fun handleParentalGuideMedia(ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse = state.withLock {
        if (ctx.method == "POST") {
            val cat = ParentalGuideCategory.fromInt(ctx.formParam("category")?.toIntOrNull() ?: 0) ?: ParentalGuideCategory.violenceAndGore
            val rating = ParentalGuideRating.fromInt(ctx.formParam("rating")?.toIntOrNull() ?: 1) ?: ParentalGuideRating.mild
            val freq = ctx.formParam("frequency")?.toIntOrNull()?.let { ParentalGuideFrequency.fromInt(it) }
            val dep = ctx.formParam("depiction")?.toIntOrNull()?.let { ParentalGuideDepiction.fromInt(it) }
            val entry = ParentalGuideEntry(
                id = nextId(),
                type = "parental-guide-entries",
                href = "/v1/parentalguide/entries",
                attributes = ParentalGuideEntry.Attributes(
                    category = cat, rating = rating, frequency = freq, depiction = dep,
                    reason = ctx.formParam("reason"),
                    isSpoiler = ctx.formParam("is_spoiler") == "1",
                    userID = currentUserId,
                ),
            )
            parentalGuideEntries.add(0, entry)
            return@withLock ok(json.encodeToString(ParentalGuideEntryResponse(listOf(entry))))
        }
        ok(json.encodeToString(ParentalGuideResponse(ParentalGuideResponseData(ParentalGuideStats(emptyList()), parentalGuideEntries.toList()))))
    }

    private fun handleParentalGuideEntry(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        val entryId = segs.getOrNull(2) ?: return notFound(ctx.path)
        return when (segs.getOrNull(3)) {
            "update" -> state.withLock {
                val idx = parentalGuideEntries.indexOfFirst { it.id == entryId }
                val existing = if (idx >= 0) parentalGuideEntries[idx] else parentalGuideEntries.first()
                val updated = existing.copy(
                    attributes = existing.attributes.copy(
                        reason = ctx.formParam("reason") ?: existing.attributes.reason,
                        isSpoiler = ctx.formParam("is_spoiler")?.let { it == "1" } ?: existing.attributes.isSpoiler,
                    )
                )
                if (idx >= 0) parentalGuideEntries[idx] = updated
                ok(json.encodeToString(ParentalGuideEntryResponse(listOf(updated))))
            }
            "vote" -> state.withLock {
                val v = when (ctx.formParam("vote")) {
                    "helpful" -> true
                    "unhelpful" -> false
                    else -> null
                }
                parentalGuideVotes[entryId] = v
                ok("""{"data":{"isHelpful":$v}}""")
            }
            "report" -> ok("{}")
            "delete" -> state.withLock {
                parentalGuideEntries.removeAll { it.id == entryId }
                ok("{}")
            }
            else -> notFound(ctx.path)
        }
    }

    // =========================================================================
    // Social Feed
    // =========================================================================

    private fun handleFeed(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 1 && ctx.method == "POST") {
            return state.withLock {
                val bodyObj = ctx.textBody?.let { runCatching { json.decodeFromString<JsonObject>(it) }.getOrNull() }
                val content = bodyObj?.get("body")?.jsonPrimitive?.content
                    ?: bodyObj?.get("content")?.jsonPrimitive?.content
                    ?: ctx.formParam("body")
                    ?: ctx.formParam("content")
                    ?: ""
                val msg = FeedMessage(
                    id = nextId(),
                    type = "feed-messages",
                    href = "/v1/feed/messages",
                    attributes = FeedMessage.Attributes(
                        content = content,
                        contentHTML = "<p>$content</p>",
                        contentMarkdown = content,
                        metrics = FeedMessage.Attributes.Metrics(0, 0, 0),
                        isNSFW = bodyObj?.get("is_nsfw")?.jsonPrimitive?.content == "1",
                        isPinned = false,
                        isReply = bodyObj?.get("is_reply")?.jsonPrimitive?.content == "1",
                        isReShare = bodyObj?.get("is_reshare")?.jsonPrimitive?.content == "1",
                        isReShared = false,
                        isSpoiler = bodyObj?.get("is_spoiler")?.jsonPrimitive?.content == "1",
                        createdAtTimestamp = System.currentTimeMillis() / 1000L,
                    ),
                    relationships = FeedMessage.Relationships(users = UserResponse(listOf(currentUser()), next = null)),
                )
                feedMessages.add(0, msg)
                ok(json.encodeToString(FeedMessageResponse(listOf(msg), next = null)))
            }
        }
        return when (segs.getOrNull(1)) {
            "explore", "home" -> ok(json.encodeToString(FeedMessageResponse(state.feedMessages.toList(), next = null)))
            "messages" -> {
                val msgId = segs.getOrNull(2) ?: return notFound(ctx.path)
                val msg = state.feedMessages.firstOrNull { it.id == msgId }
                when (segs.getOrNull(3)) {
                    null -> if (msg != null) ok(json.encodeToString(FeedMessageResponse(listOf(msg), next = null))) else notFound(ctx.path)
                    "replies", "quotes", "re-shares" -> ok(json.encodeToString(FeedMessageResponse(emptyList(), next = null)))
                    "heart" -> state.withLock {
                        val hearted = if (msgId in heartedFeedIds) {
                            heartedFeedIds.remove(msgId); false
                        } else {
                            heartedFeedIds.add(msgId); true
                        }
                        msg?.attributes?.metrics?.let { m ->
                            m.heartCount = (m.heartCount + if (hearted) 1 else -1).coerceAtLeast(0)
                        }
                        ok("""{"data":{"isHearted":$hearted}}""")
                    }
                    "pin" -> state.withLock {
                        val pinned = !(msg?.attributes?.isPinned ?: false)
                        msg?.attributes?.isPinned = pinned
                        ok("""{"data":{"isPinned":$pinned}}""")
                    }
                    "update" -> state.withLock {
                        val bodyObj = ctx.textBody?.let { runCatching { json.decodeFromString<JsonObject>(it) }.getOrNull() }
                        val content = bodyObj?.get("content")?.jsonPrimitive?.content ?: msg?.attributes?.content.orEmpty()
                        val isNsfw = bodyObj?.get("is_nsfw")?.jsonPrimitive?.content == "1"
                        val isSpoiler = bodyObj?.get("is_spoiler")?.jsonPrimitive?.content == "1"
                        msg?.attributes?.let {
                            it.content = content
                            it.contentHTML = "<p>$content</p>"
                            it.contentMarkdown = content
                            it.isNSFW = isNsfw
                            it.isSpoiler = isSpoiler
                        }
                        ok("""{"data":{"content":${json.encodeToString(content)},"isNSFW":$isNsfw,"isSpoiler":$isSpoiler}}""")
                    }
                    "report" -> ok("{}")
                    "delete" -> state.withLock {
                        feedMessages.removeAll { it.id == msgId }
                        ok("{}")
                    }
                    else -> notFound(ctx.path)
                }
            }
            else -> notFound(ctx.path)
        }
    }

    // =========================================================================
    // Auth & Users
    // =========================================================================

    private fun handleUsers(
        segs: List<String>,
        ctx: MockRequestContext,
        config: KurozoraMockConfig,
        state: KurozoraMockState,
    ): MockRouteResponse {
        if (segs.size == 1 && ctx.method == "GET") {
            val ids = ctx.queryParameters["ids"]?.split(",")?.toSet()
            val list = if (ids != null) state.users.filter { it.id in ids } else state.users
            return ok(json.encodeToString(UserResponse(list, next = null)))
        }
        if (segs.size == 1 && ctx.method == "POST") {
            val userJson = json.encodeToString(state.currentUser())
            return ok("""{"data":[$userJson],"authenticationToken":"${state.activeAuthToken}"}""")
        }
        return when (segs.getOrNull(1)) {
            "signin" -> {
                if (config.requireTwoFactorOnSignIn) {
                    ok("""{"two_factor":true,"challenge_token":"${state.pendingTwoFactorToken}"}""")
                } else {
                    val userJson = json.encodeToString(state.currentUser())
                    ok("""{"data":[$userJson],"authenticationToken":"${state.activeAuthToken}"}""")
                }
            }
            "two-factor-challenge" -> {
                val userJson = json.encodeToString(state.currentUser())
                ok("""{"data":[$userJson],"authenticationToken":"${state.activeAuthToken}"}""")
            }
            "siwa" -> {
                val userJson = json.encodeToString(state.currentUser())
                ok("""{"data":[$userJson],"authenticationToken":"${state.activeAuthToken}","action":"signIn"}""")
            }
            "reset-password", "delete" -> ok("{}")
            "search" -> {
                val slug = segs.getOrNull(2).orEmpty()
                val matched = state.users.filter { it.attributes.slug.contains(slug, ignoreCase = true) || it.attributes.username.contains(slug, ignoreCase = true) }
                ok(json.encodeToString(UserIdentityResponse(matched.map { UserIdentity(it.id, it.type, it.href) }, next = null)))
            }
            else -> {
                val userId = segs[1]
                val user = state.users.firstOrNull { it.id == userId } ?: state.currentUser()
                when (segs.getOrNull(2)) {
                    "profile", null -> ok(json.encodeToString(UserResponse(listOf(user), next = null)))
                    "follow" -> state.withLock {
                        val followed = if (userId in followingUserIds) {
                            followingUserIds.remove(userId); false
                        } else {
                            followingUserIds.add(userId); true
                        }
                        ok("""{"data":{"isFollowed":$followed}}""")
                    }
                    "block" -> state.withLock {
                        val blocked = if (userId in blockedUserIds) {
                            blockedUserIds.remove(userId); false
                        } else {
                            blockedUserIds.add(userId); true
                        }
                        ok("""{"data":{"isBlocked":$blocked}}""")
                    }
                    "followers" -> ok(json.encodeToString(UserIdentityResponse(state.users.filter { it.id != userId }.map { UserIdentity(it.id, it.type, it.href) }, next = null)))
                    "following" -> ok(json.encodeToString(UserIdentityResponse(state.users.filter { it.id in state.followingUserIds }.map { UserIdentity(it.id, it.type, it.href) }, next = null)))
                    "favorites" -> ok(json.encodeToString(buildFavorites(state)))
                    "library" -> ok(json.encodeToString(buildLibraryResponse(ctx, state)))
                    "feed-messages" -> ok(json.encodeToString(FeedMessageResponse(state.feedMessages.toList(), next = null)))
                    "reviews" -> ok(json.encodeToString(ReviewResponse(state.reviews.toList(), next = null)))
                    "achievements" -> ok(json.encodeToString(AchievementResponse(state.achievements, next = null)))
                    else -> notFound(ctx.path)
                }
            }
        }
    }

    // =========================================================================
    // Authenticated User (/me/*)
    // =========================================================================

    private fun handleMe(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 1) {
            return if (ctx.method == "POST") {
                val me = state.currentUser()
                ok("""{"data":{"username":"${me.attributes.slug}","nickname":"${me.attributes.username}"},"message":"Profile updated successfully."}""")
            } else {
                ok(json.encodeToString(UserResponse(listOf(state.currentUser()), next = null)))
            }
        }
        return when (segs[1]) {
            "followers" -> ok(json.encodeToString(UserIdentityResponse(state.users.filter { it.id != state.currentUserId }.map { UserIdentity(it.id, it.type, it.href) }, next = null)))
            "following" -> ok(json.encodeToString(UserIdentityResponse(state.users.filter { it.id in state.followingUserIds }.map { UserIdentity(it.id, it.type, it.href) }, next = null)))
            "blocked" -> ok(json.encodeToString(UserIdentityResponse(state.users.filter { it.id in state.blockedUserIds }.map { UserIdentity(it.id, it.type, it.href) }, next = null)))
            "reviews" -> ok(json.encodeToString(ReviewResponse(state.reviews.toList(), next = null)))
            "achievements" -> ok(json.encodeToString(AchievementResponse(state.achievements, next = null)))
            "feed-messages" -> ok(json.encodeToString(FeedMessageIdentityResponse(state.feedMessages.map { FeedMessageIdentity(it.id, it.type, it.href) }, next = null)))
            "settings" -> state.withLock {
                if (ctx.method == "POST") {
                    val cur = userSettings.attributes
                    userSettings = userSettings.copy(
                        attributes = cur.copy(
                            scrobbleThreshold = ctx.formParam("scrobbleThreshold")?.toIntOrNull() ?: cur.scrobbleThreshold,
                            discordRichPresenceEnabled = ctx.formParam("discordRichPresenceEnabled")?.let { it == "1" } ?: cur.discordRichPresenceEnabled,
                            discordPresenceImage = ctx.formParam("discordPresenceImage")?.toIntOrNull()?.let { kurozorakit.data.enums.DiscordPresenceImage.fromInt(it) } ?: cur.discordPresenceImage,
                            discordActivityName = ctx.formParam("discordActivityName")?.toIntOrNull()?.let { kurozorakit.data.enums.DiscordActivityName.fromInt(it) } ?: cur.discordActivityName,
                            ratingStyle = ctx.formParam("ratingStyle")?.toIntOrNull()?.let { kurozorakit.data.enums.RatingStyle.fromInt(it) } ?: cur.ratingStyle,
                        )
                    )
                }
                ok(json.encodeToString(UserSettingResponse(listOf(userSettings))))
            }
            "digest" -> {
                val section = segs.getOrNull(2)
                val all = state.buildWeeklyDigest()
                val filtered = if (section != null) all.filter { it.id == section } else all
                ok(json.encodeToString(WeeklyDigestSectionResponse(filtered)))
            }
            "episodes" -> when (segs.getOrNull(2)) {
                "up-next" -> ok(json.encodeToString(EpisodeIdentityResponse(state.episodes.filter { it.id !in state.watchedEpisodeIds }.map { EpisodeIdentity(it.id, it.type, it.href) }, next = null)))
                "watched" -> ok(json.encodeToString(EpisodeIdentityResponse(state.episodes.filter { it.id in state.watchedEpisodeIds }.map { EpisodeIdentity(it.id, it.type, it.href) }, next = null)))
                else -> notFound(ctx.path)
            }
            "timeout" -> state.withLock {
                activeTimeoutAppealMessage = ctx.formParam("message") ?: activeTimeoutAppealMessage
                ok(json.encodeToString(TimeoutResponse(listOf(buildTimeout()))))
            }
            "access-tokens" -> handleAccessTokens(segs, ctx, state)
            "sessions" -> handleSessions(segs, ctx, state)
            "favorites" -> handleMeFavorites(ctx, state)
            "library" -> handleMeLibrary(segs, ctx, state)
            "notifications" -> handleMeNotifications(segs, ctx, state)
            "recap" -> when {
                segs.size == 2 -> ok(json.encodeToString(RecapResponse(state.recaps, next = null)))
                segs.size == 4 -> ok(json.encodeToString(RecapItemResponse(state.recapItems)))
                segs.getOrNull(4) == "stats" -> ok(json.encodeToString(RecapStatResponse(state.recapStats)))
                else -> notFound(ctx.path)
            }
            "reminders" -> handleMeReminders(segs, ctx, state)
            else -> notFound(ctx.path)
        }
    }

    private fun handleAccessTokens(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 2) return ok(json.encodeToString(AccessTokenResponse(state.accessTokens.toList(), next = null)))
        if (segs[2] == "delete") {
            val ids = ctx.formParam("identities")?.split(",")?.toSet().orEmpty()
            state.withLock { accessTokens.removeAll { it.id in ids } }
            return ok("{}")
        }
        val tokenId = segs[2]
        return when (segs.getOrNull(3)) {
            null -> ok(json.encodeToString(AccessTokenResponse(state.accessTokens.filter { it.id == tokenId }, next = null)))
            "update" -> ok("{}")
            "delete" -> state.withLock { accessTokens.removeAll { it.id == tokenId }; ok("{}") }
            else -> notFound(ctx.path)
        }
    }

    private fun handleSessions(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 2) return ok(json.encodeToString(SessionIdentityResponse(state.sessions.map { SessionIdentity(it.id, it.type, it.href) }, next = null)))
        if (segs[2] == "delete") {
            val ids = ctx.formParam("identities")?.split(",")?.toSet().orEmpty()
            state.withLock { sessions.removeAll { it.id in ids } }
            return ok("{}")
        }
        val sessId = segs[2]
        return when (segs.getOrNull(3)) {
            null -> ok(json.encodeToString(SessionResponse(state.sessions.filter { it.id == sessId }, next = null)))
            "delete" -> state.withLock { sessions.removeAll { it.id == sessId }; ok("{}") }
            else -> notFound(ctx.path)
        }
    }

    private fun handleMeFavorites(ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (ctx.method == "GET") return ok(json.encodeToString(buildFavorites(state)))
        return state.withLock {
            val kind = (ctx.formParam("kind") ?: ctx.formParam("library"))?.toIntOrNull() ?: 0
            val ids = ctx.formParam("model_ids")?.split(",")?.filter { it.isNotBlank() }.orEmpty()
            val explicit = ctx.formParam("is_favorited")?.let { it == "1" }
            var finalState = false
            ids.forEach { id ->
                val key = kind to id
                finalState = when (explicit) {
                    true -> { favorites.add(key); true }
                    false -> { favorites.remove(key); false }
                    null -> if (key in favorites) { favorites.remove(key); false } else { favorites.add(key); true }
                }
            }
            ok("""{"data":{"isFavorited":$finalState}}""")
        }
    }

    private fun handleMeLibrary(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 2 && ctx.method == "GET") return ok(json.encodeToString(buildLibraryResponse(ctx, state)))
        return when (segs.getOrNull(2)) {
            null -> state.withLock {
                val kind = (ctx.formParam("kind") ?: ctx.formParam("library"))?.toIntOrNull() ?: 0
                val statusStr = ctx.formParam("status") ?: "InProgress"
                val status = KKLibrary.Status.all.firstOrNull {
                    it.sectionValue.equals(statusStr, ignoreCase = true) || it.value.toString() == statusStr
                } ?: KKLibrary.Status.INPROGRESS
                val ids = ctx.formParam("model_ids")?.split(",")?.filter { it.isNotBlank() }.orEmpty()
                ids.forEach { id ->
                    library[kind to id] = MockLibraryEntry(kind, id, status)
                }
                ok("""{"data":{"attributes":{"libraryStatus":${status.value},"isFavorited":false,"isReminded":false,"isHidden":false,"rewatchCount":0}}}""")
            }
            "update" -> state.withLock {
                val kind = (ctx.formParam("kind") ?: ctx.formParam("library"))?.toIntOrNull() ?: 0
                val ids = ctx.formParam("model_ids")?.split(",")?.filter { it.isNotBlank() }.orEmpty()
                val rewatch = ctx.formParam("rewatch_count")?.toIntOrNull()
                val isHidden = ctx.formParam("is_hidden")?.let { it == "1" }
                var last: MockLibraryEntry? = null
                ids.forEach { id ->
                    val entry = library.getOrPut(kind to id) { MockLibraryEntry(kind, id, KKLibrary.Status.INPROGRESS) }
                    rewatch?.let { entry.rewatchCount = it }
                    isHidden?.let { entry.isHidden = it }
                    last = entry
                }
                ok("""{"data":{"attributes":{"libraryStatus":${last?.status?.value ?: 0},"isHidden":${last?.isHidden ?: false},"rewatchCount":${last?.rewatchCount ?: 0}}}}""")
            }
            "delete" -> state.withLock {
                val kind = (ctx.formParam("kind") ?: ctx.formParam("library"))?.toIntOrNull() ?: 0
                val ids = ctx.formParam("model_ids")?.split(",")?.filter { it.isNotBlank() }.orEmpty()
                ids.forEach { library.remove(kind to it) }
                ok("""{"data":{"attributes":{"libraryStatus":null,"isFavorited":false,"isReminded":false,"isHidden":false,"rewatchCount":0}}}""")
            }
            "clear" -> state.withLock {
                val kind = (ctx.formParam("kind") ?: ctx.formParam("library"))?.toIntOrNull() ?: 0
                library.keys.removeAll { it.first == kind }
                ok("{}")
            }
            "sync" -> state.withLock {
                val entries = library.values.mapIndexed { idx, e ->
                    LibrarySyncEntry(
                        id = "sync-$idx",
                        kind = e.kind,
                        trackableID = e.modelId,
                        status = e.status,
                        rewatchCount = e.rewatchCount,
                        isHidden = e.isHidden,
                        isFavorited = (e.kind to e.modelId) in favorites,
                        isReminded = (e.kind to e.modelId) in reminders,
                    )
                }
                ok(
                    json.encodeToString(
                        LibrarySyncResponse(
                            data = LibrarySyncResponse.Data(
                                attributes = LibrarySyncResponse.Attributes(syncTime = System.currentTimeMillis() / 1000L),
                                relationships = LibrarySyncResponse.Relationships(entries = entries),
                            )
                        )
                    )
                )
            }
            "import" -> ok("""{"message":"Mock library import completed."}""")
            else -> notFound(ctx.path)
        }
    }

    private fun handleMeNotifications(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.size == 2) return ok(json.encodeToString(UserNotificationResponse(state.notifications.toList())))
        return when (segs[2]) {
            "delete" -> state.withLock {
                val target = ctx.formParam("notification").orEmpty()
                if (target == "all") {
                    notifications.clear()
                } else {
                    val ids = target.split(",").toSet()
                    notifications.removeAll { it.id in ids }
                }
                ok("{}")
            }
            "update" -> state.withLock {
                val id = ctx.formParam("notification").orEmpty()
                val isRead = ctx.formParam("read") == "1"
                notificationReadOverrides[id] = isRead
                ok("""{"data":{"isRead":$isRead}}""")
            }
            else -> {
                val n = state.notifications.firstOrNull { it.id == segs[2] } ?: return notFound(ctx.path)
                ok(json.encodeToString(SingleNotificationResponse(n)))
            }
        }
    }

    private fun handleMeReminders(segs: List<String>, ctx: MockRequestContext, state: KurozoraMockState): MockRouteResponse {
        if (segs.getOrNull(2) == "download") {
            val ics = buildString {
                appendLine("BEGIN:VCALENDAR")
                appendLine("VERSION:2.0")
                appendLine("PRODID:-//Kurozora//Mock//EN")
                state.reminders.forEach { (k, id) ->
                    appendLine("BEGIN:VEVENT")
                    appendLine("SUMMARY:Kurozora Reminder ($k:$id)")
                    appendLine("END:VEVENT")
                }
                appendLine("END:VCALENDAR")
            }
            return MockRouteResponse(200, contentType = "text/calendar", binaryBody = ics.encodeToByteArray())
        }
        if (ctx.method == "GET") {
            val showsList = state.shows.filter { (KKLibrary.Kind.SHOWS.value to it.id) in state.reminders }
            val litsList = state.literatures.filter { (KKLibrary.Kind.LITERATURES.value to it.id) in state.reminders }
            val gamesList = state.games.filter { (KKLibrary.Kind.GAMES.value to it.id) in state.reminders }
            return ok(json.encodeToString(ReminderLibraryResponse(ReminderLibrary(shows = showsList, literatures = litsList, games = gamesList), next = null)))
        }
        return state.withLock {
            val kind = ctx.formParam("library")?.toIntOrNull() ?: 0
            val ids = ctx.formParam("model_ids")?.split(",")?.filter { it.isNotBlank() }.orEmpty()
            val explicit = ctx.formParam("is_reminded")?.let { it == "1" }
            var finalState = false
            ids.forEach { id ->
                val key = kind to id
                finalState = when (explicit) {
                    true -> { reminders.add(key); true }
                    false -> { reminders.remove(key); false }
                    null -> if (key in reminders) { reminders.remove(key); false } else { reminders.add(key); true }
                }
            }
            ok("""{"data":{"isReminded":$finalState}}""")
        }
    }

    private fun buildFavorites(state: KurozoraMockState): FavoriteLibraryResponse {
        val showsList = state.shows.filter { (KKLibrary.Kind.SHOWS.value to it.id) in state.favorites }.map { state.showWithUserLibrary(it) }
        val litsList = state.literatures.filter { (KKLibrary.Kind.LITERATURES.value to it.id) in state.favorites }.map { state.literatureWithUserLibrary(it) }
        val gamesList = state.games.filter { (KKLibrary.Kind.GAMES.value to it.id) in state.favorites }.map { state.gameWithUserLibrary(it) }
        return FavoriteLibraryResponse(FavoriteLibrary(shows = showsList, literatures = litsList, games = gamesList), next = null)
    }

    private fun buildLibraryResponse(ctx: MockRequestContext, state: KurozoraMockState): LibraryResponse {
        val statusStr = ctx.queryParameters["status"]
        val targetStatus = statusStr?.let { s ->
            KKLibrary.Status.all.firstOrNull { it.sectionValue.equals(s, ignoreCase = true) || it.value.toString() == s }
        }
        val showIds = state.library.values
            .filter { it.kind == KKLibrary.Kind.SHOWS.value && (targetStatus == null || it.status == targetStatus) }
            .map { it.modelId }.toSet()
        val litIds = state.library.values
            .filter { it.kind == KKLibrary.Kind.LITERATURES.value && (targetStatus == null || it.status == targetStatus) }
            .map { it.modelId }.toSet()
        val gameIds = state.library.values
            .filter { it.kind == KKLibrary.Kind.GAMES.value && (targetStatus == null || it.status == targetStatus) }
            .map { it.modelId }.toSet()
        return LibraryResponse(
            data = Library(
                shows = state.shows.filter { it.id in showIds }.map { state.showWithUserLibrary(it) },
                literatures = state.literatures.filter { it.id in litIds }.map { state.literatureWithUserLibrary(it) },
                games = state.games.filter { it.id in gameIds }.map { state.gameWithUserLibrary(it) },
            ),
            next = null,
        )
    }
}
