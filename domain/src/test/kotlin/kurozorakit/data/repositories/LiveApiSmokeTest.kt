package kurozorakit.data.repositories

import kotlinx.coroutines.test.runTest
import kurozorakit.api.AccountUser
import kurozorakit.api.KurozoraApiClient
import kurozorakit.api.RetryConfig
import kurozorakit.api.TokenProvider
import kurozorakit.data.enums.KKScheduleType
import kurozorakit.data.enums.KKSearchScope
import kurozorakit.data.enums.KKSearchType
import kurozorakit.data.enums.MediaCollection
import kurozorakit.data.enums.MediaKind
import kurozorakit.data.enums.ReviewKind
import kurozorakit.data.enums.SeasonOfYear
import kurozorakit.data.repositories.airseason.AirSeasonRepositoryImpl
import kurozorakit.data.repositories.auth.AuthRepositoryImpl
import kurozorakit.data.repositories.cast.CastRepositoryImpl
import kurozorakit.data.repositories.character.CharacterRepositoryImpl
import kurozorakit.data.repositories.charts.ChartsRepositoryImpl
import kurozorakit.data.repositories.episode.EpisodeRepositoryImpl
import kurozorakit.data.repositories.explore.ExploreRepositoryImpl
import kurozorakit.data.repositories.feed.FeedRepositoryImpl
import kurozorakit.data.repositories.game.GameRepositoryImpl
import kurozorakit.data.repositories.genre.GenreRepositoryImpl
import kurozorakit.data.repositories.image.ImageRepositoryImpl
import kurozorakit.data.repositories.legal.LegalRepositoryImpl
import kurozorakit.data.repositories.literature.LiteratureRepositoryImpl
import kurozorakit.data.repositories.misc.MiscRepositoryImpl
import kurozorakit.data.repositories.parentalguide.ParentalGuideRepositoryImpl
import kurozorakit.data.repositories.person.PersonRepositoryImpl
import kurozorakit.data.repositories.review.ReviewRepositoryImpl
import kurozorakit.data.repositories.schedule.ScheduleRepositoryImpl
import kurozorakit.data.repositories.search.SearchRepositoryImpl
import kurozorakit.data.repositories.season.SeasonRepositoryImpl
import kurozorakit.data.repositories.show.ShowRepositoryImpl
import kurozorakit.data.repositories.song.SongRepositoryImpl
import kurozorakit.data.repositories.studio.StudioRepositoryImpl
import kurozorakit.data.repositories.theme.ThemeRepositoryImpl
import kurozorakit.data.repositories.themestore.ThemeStoreRepositoryImpl
import kurozorakit.data.repositories.user.UserRepositoryImpl
import kurozorakit.shared.Result
import kurozorakit.shared.UserAgent
import kotlin.test.Test
import kotlin.test.fail

/**
 * Opt-in live integration smoke test that hits the real Kurozora API (`https://api.kurozora.app/v1`),
 * automatically discovers valid resource IDs from index endpoints, and produces a per-endpoint
 * pass/fail diagnostic report across all public (and optionally authenticated) repositories.
 *
 * Disabled by default so `./gradlew test --offline` stays fast and deterministic.
 *
 * **Required by Kurozora API (`AuthenticateAPIClient` middleware):**
 * Every request to `api.kurozora.app/v1` requires a valid developer JWT in `X-API-Key`.
 *
 * Usage:
 * ```bash
 * KUROZORA_LIVE_TESTS=true \
 * KUROZORA_API_KEY="<your-developer-token-jwt>" \
 *   ./gradlew :domain:test --tests "*LiveApiSmokeTest*" --info
 *
 * # Optional authenticated /me read checks:
 * KUROZORA_LIVE_TESTS=true \
 * KUROZORA_API_KEY="<your-developer-token-jwt>" \
 * KUROZORA_BEARER_TOKEN="<your-user-bearer-token>" \
 *   ./gradlew :domain:test --tests "*LiveApiSmokeTest*" --info
 * ```
 */
class LiveApiSmokeTest {

    private fun readConfig(envName: String, propName: String): String? {
        return System.getenv(envName)?.takeIf { it.isNotBlank() }
            ?: System.getProperty(propName)?.takeIf { it.isNotBlank() }
    }

    @Test
    fun `live read-only smoke test across repositories with auto-discovered IDs`() = runTest {
        val enabled = readConfig("KUROZORA_LIVE_TESTS", "kurozora.liveTests")
        if (enabled != "true") {
            return@runTest
        }

        val apiKey = readConfig("KUROZORA_API_KEY", "kurozora.apiKey")
        if (apiKey.isNullOrBlank()) {
            fail(
                "KUROZORA_LIVE_TESTS=true requires KUROZORA_API_KEY (Kurozora Developer Token JWT for X-API-Key header). " +
                    "Pass KUROZORA_API_KEY=<token> or -Dkurozora.apiKey=<token>."
            )
        }

        val bearerToken = readConfig("KUROZORA_BEARER_TOKEN", "kurozora.bearerToken")
        val tokenProvider = bearerToken?.let { token ->
            object : TokenProvider {
                override suspend fun saveToken(user: AccountUser) = Unit
                override suspend fun getToken(): String = token
            }
        }

        val appId = readConfig("KUROZORA_APP_ID", "kurozora.appId") ?: ""
        val appName = readConfig("KUROZORA_APP_NAME", "kurozora.appName") ?: "KurozoraKit"
        val client = KurozoraApiClient(
            baseUrl = readConfig("KUROZORA_BASE_URL", "kurozora.baseUrl") ?: RepositoryTestHarness.BASE_URL,
            apiKey = apiKey,
            tokenProvider = tokenProvider,
            platform = RepositoryTestHarness.testPlatform,
            userAgent = UserAgent(appName = appName, appID = appId),
            retryConfig = RetryConfig(maxRetries = 1),
        )

        val failures = mutableListOf<String>()
        var passed = 0

        suspend fun <T> check(label: String, block: suspend () -> Result<T>): T? {
            val start = System.currentTimeMillis()
            val result = block()
            val elapsed = System.currentTimeMillis() - start
            return when (result) {
                is Result.Success -> {
                    passed++
                    println("[LIVE OK]   $label (${elapsed}ms)")
                    result.data
                }
                is Result.Error -> {
                    val msg = "$label -> ${result.error}"
                    failures += msg
                    println("[LIVE FAIL] $msg (${elapsed}ms)")
                    null
                }
            }
        }

        // 1. Misc / Legal / Catalogs
        val miscRepo = MiscRepositoryImpl(client)
        check("GET /info") { miscRepo.getAppInfo() }
        check("GET /settings") { miscRepo.getSettings() }
        check("GET /players") { miscRepo.getPlayers() }
        check("GET /providers") { miscRepo.getProviders() }
        val langs = check("GET /languages") { miscRepo.getLanguages() }
        langs?.data?.firstOrNull()?.code?.let { langCode ->
            check("GET /languages/$langCode") { miscRepo.getLanguage(langCode) }
        }

        val legalRepo = LegalRepositoryImpl(client)
        check("GET /legal/privacy-policy") { legalRepo.getPrivacyPolicy() }
        check("GET /legal/terms-of-use") { legalRepo.getTermsOfUse() }

        val genreRepo = GenreRepositoryImpl(client)
        val genres = check("GET /genres") { genreRepo.getGenres() }
        genres?.data?.firstOrNull()?.id?.let { id ->
            check("GET /genres/$id") { genreRepo.getGenre(id) }
        }

        val themeRepo = ThemeRepositoryImpl(client)
        val themes = check("GET /themes") { themeRepo.getThemes() }
        themes?.data?.firstOrNull()?.id?.let { id ->
            check("GET /themes/$id") { themeRepo.getTheme(id) }
        }

        val themeStoreRepo = ThemeStoreRepositoryImpl(client)
        val appThemes = check("GET /theme-store") { themeStoreRepo.getThemeStore() }
        appThemes?.data?.firstOrNull()?.id?.let { id ->
            check("GET /theme-store/$id") { themeStoreRepo.getThemeStoreItem(id) }
        }

        val exploreRepo = ExploreRepositoryImpl(client)
        val explore = check("GET /explore") { exploreRepo.getExplore() }
        explore?.data?.firstOrNull()?.id?.let { catId ->
            check("GET /explore/$catId") { exploreRepo.getExplore(catId, limit = 5) }
        }

        val airSeasonRepo = AirSeasonRepositoryImpl(client)
        check("GET /anime/seasons?year=2025&season=0") { airSeasonRepo.getAnimeSeason("2025", SeasonOfYear.WINTER) }
        check("GET /manga/seasons?year=2025&season=0") { airSeasonRepo.getMangaSeason("2025", SeasonOfYear.WINTER) }
        check("GET /games/seasons?year=2025&season=0") { airSeasonRepo.getGameSeason("2025", SeasonOfYear.WINTER) }

        val scheduleRepo = ScheduleRepositoryImpl(client)
        check("GET /schedule") { scheduleRepo.getSchedule(KKScheduleType.shows, "2025-01-15") }

        val imageRepo = ImageRepositoryImpl(client)
        check("GET /images/random") { imageRepo.getRandomImages(MediaKind.shows, MediaCollection.poster, 1) }

        val searchRepo = SearchRepositoryImpl(client)
        check("GET /search") { searchRepo.search(KKSearchScope.kurozora, listOf(KKSearchType.shows), "naruto", limit = 5) }
        check("GET /search/suggestions") { searchRepo.getSearchSuggestions(KKSearchScope.kurozora, listOf(KKSearchType.shows), "nar") }

        val reviewRepo = ReviewRepositoryImpl(client)
        check("GET /reviews/categories") { reviewRepo.getRatingCategories(ReviewKind.anime) }

        val feedRepo = FeedRepositoryImpl(client)
        val feed = check("GET /feed/explore") { feedRepo.getExploreFeed(limit = 5) }
        feed?.data?.firstOrNull()?.id?.let { msgId ->
            check("GET /feed/messages/$msgId") { feedRepo.getFeedMessage(msgId) }
            check("GET /feed/messages/$msgId/replies") { feedRepo.getFeedMessageReplies(msgId, limit = 5) }
        }

        // 2. Auto-discover real IDs and verify detail + sub-resource endpoints
        val showRepo = ShowRepositoryImpl(client)
        val shows = check("GET /anime") { showRepo.getShows(limit = 3) }?.data.orEmpty()
        shows.firstOrNull()?.id?.let { showId ->
            check("GET /anime/$showId") { showRepo.getShow(showId) }
            check("GET /anime/$showId/cast") { showRepo.getShowCast(showId, limit = 5) }
            check("GET /anime/$showId/staff") { showRepo.getShowStaff(showId, limit = 5) }
            check("GET /anime/$showId/characters") { showRepo.getShowCharacters(showId, limit = 5) }
            check("GET /anime/$showId/videos") { showRepo.getShowVideos(showId, limit = 5) }
            check("GET /anime/$showId/languages") { showRepo.getShowLanguages(showId, limit = 5) }
            check("GET /anime/$showId/editorial") { showRepo.getShowEditorial(showId) }
            check("GET /anime/$showId/reviews") { showRepo.getShowReviews(showId, limit = 5) }
            check("GET /anime/$showId/songs") { showRepo.getShowSongs(showId, limit = 5) }
            check("GET /anime/$showId/studios") { showRepo.getShowStudios(showId, limit = 5) }
            check("GET /anime/$showId/parentalguide") { ParentalGuideRepositoryImpl(client).getShowParentalGuide(showId) }

            val seasons = check("GET /anime/$showId/seasons") { showRepo.getShowSeasons(showId, limit = 3) }?.data.orEmpty()
            seasons.firstOrNull()?.id?.let { seasonId ->
                val seasonRepo = SeasonRepositoryImpl(client)
                check("GET /seasons/$seasonId") { seasonRepo.getDetails(seasonId) }
                val episodes = check("GET /seasons/$seasonId/episodes") { seasonRepo.getEpisodes(seasonId, limit = 3) }?.data.orEmpty()
                episodes.firstOrNull()?.id?.let { epId ->
                    val epRepo = EpisodeRepositoryImpl(client)
                    check("GET /episodes/$epId") { epRepo.getEpisode(epId) }
                    check("GET /episodes/$epId/reviews") { epRepo.getEpisodeReviews(epId, limit = 3) }
                }
            }

            val castIds = check("GET /anime/$showId/cast (CastRepo)") { CastRepositoryImpl(client).getShowCast(showId, limit = 3) }?.data.orEmpty()
            castIds.firstOrNull()?.id?.let { castId ->
                check("GET /show-cast/$castId") { CastRepositoryImpl(client).getShowCastDetails(castId) }
            }
        }

        val litRepo = LiteratureRepositoryImpl(client)
        val lits = check("GET /manga") { litRepo.getLiteratures(limit = 3) }?.data.orEmpty()
        lits.firstOrNull()?.id?.let { litId ->
            check("GET /manga/$litId") { litRepo.getLiterature(litId) }
            check("GET /manga/$litId/languages") { litRepo.getLiteratureLanguages(litId, limit = 5) }
            check("GET /manga/$litId/gallery") { litRepo.getLiteratureGallery(litId, limit = 5) }
        }

        val gameRepo = GameRepositoryImpl(client)
        val games = check("GET /games") { gameRepo.getGames(limit = 3) }?.data.orEmpty()
        games.firstOrNull()?.id?.let { gameId ->
            check("GET /games/$gameId") { gameRepo.getGame(gameId) }
            check("GET /games/$gameId/cast") { gameRepo.getGameCast(gameId, limit = 5) }
            check("GET /games/$gameId/languages") { gameRepo.getGameLanguages(gameId, limit = 5) }
        }

        val charRepo = CharacterRepositoryImpl(client)
        val chars = check("GET /characters") { charRepo.getCharacters(limit = 3) }?.data.orEmpty()
        chars.firstOrNull()?.id?.let { charId ->
            check("GET /characters/$charId") { charRepo.getCharacter(charId) }
            check("GET /characters/$charId/gallery") { charRepo.getCharacterGallery(charId, limit = 5) }
        }

        val personRepo = PersonRepositoryImpl(client)
        val people = check("GET /people") { personRepo.getPeople(limit = 3) }?.data.orEmpty()
        people.firstOrNull()?.id?.let { personId ->
            check("GET /people/$personId") { personRepo.getPerson(personId) }
            check("GET /people/$personId/relationships") { personRepo.getPersonRelationships(personId, limit = 5) }
        }

        val studioRepo = StudioRepositoryImpl(client)
        val studios = check("GET /studios") { studioRepo.getStudios(limit = 3) }?.data.orEmpty()
        studios.firstOrNull()?.id?.let { studioId ->
            check("GET /studios/$studioId") { studioRepo.getStudio(studioId) }
            check("GET /studios/$studioId/predecessors") { studioRepo.getStudioPredecessors(studioId, limit = 5) }
            check("GET /studios/$studioId/successors") { studioRepo.getStudioSuccessors(studioId, limit = 5) }
        }

        val songRepo = SongRepositoryImpl(client)
        val songs = check("GET /songs") { songRepo.getSongs(limit = 3) }?.data.orEmpty()
        songs.firstOrNull()?.id?.let { songId ->
            check("GET /songs/$songId") { songRepo.getSong(songId) }
            check("GET /songs/$songId/lyrics") { songRepo.getSongLyrics(songId) }
        }

        // 3. Optional authenticated read-only checks
        if (bearerToken != null) {
            val userRepo = UserRepositoryImpl(client)
            check("GET /me") { userRepo.getMyProfile() }
            check("GET /me/settings") { userRepo.getMySettings() }
            check("GET /me/digest") { userRepo.getMyDigest() }
            check("GET /me/recap") { userRepo.getMyRecaps() }
            check("GET /me/notifications") { userRepo.getMyNotifications() }
            check("GET /me/episodes/up-next") { userRepo.getMyUpNextEpisodes(limit = 5) }
            check("GET /me/episodes/watched") { userRepo.getMyWatchedEpisodes(limit = 5) }
        }

        println("=== LIVE SMOKE SUMMARY: $passed passed, ${failures.size} failed ===")
        if (failures.isNotEmpty()) {
            fail("Live API smoke test had ${failures.size} failures:\n" + failures.joinToString("\n"))
        }
    }

    @Test
    fun `live phase 1 critical deserialization and endpoint verification`() = runTest {
        val enabled = readConfig("KUROZORA_LIVE_TESTS", "kurozora.liveTests")
        if (enabled != "true") {
            return@runTest
        }

        val apiKey = readConfig("KUROZORA_API_KEY", "kurozora.apiKey")
        if (apiKey.isNullOrBlank()) {
            fail("KUROZORA_LIVE_TESTS=true requires KUROZORA_API_KEY.")
        }

        val appId = readConfig("KUROZORA_APP_ID", "kurozora.appId") ?: ""
        val appName = readConfig("KUROZORA_APP_NAME", "kurozora.appName") ?: "KtorClient"

        val client = KurozoraApiClient(
            baseUrl = readConfig("KUROZORA_BASE_URL", "kurozora.baseUrl") ?: RepositoryTestHarness.BASE_URL,
            apiKey = apiKey,
            platform = RepositoryTestHarness.testPlatform,
            userAgent = UserAgent(appName = appName, appVersion = "1.0.0", appID = appId),
            retryConfig = RetryConfig(maxRetries = 1),
        )

        val failures = mutableListOf<String>()
        var passed = 0

        suspend fun <T> check(label: String, verify: (T) -> String? = { null }, block: suspend () -> Result<T>): T? {
            val start = System.currentTimeMillis()
            val result = block()
            val elapsed = System.currentTimeMillis() - start
            return when (result) {
                is Result.Success -> {
                    val problem = verify(result.data)
                    if (problem != null) {
                        val msg = "$label -> ASSERTION FAILED: $problem"
                        failures += msg
                        println("[LIVE FAIL] $msg (${elapsed}ms)")
                        null
                    } else {
                        passed++
                        println("[LIVE OK]   $label (${elapsed}ms)")
                        result.data
                    }
                }
                is Result.Error -> {
                    val msg = "$label -> ${result.error}"
                    failures += msg
                    println("[LIVE FAIL] $msg (${elapsed}ms)")
                    null
                }
            }
        }

        val showRepo = ShowRepositoryImpl(client)
        val litRepo = LiteratureRepositoryImpl(client)
        val gameRepo = GameRepositoryImpl(client)
        val songRepo = SongRepositoryImpl(client)
        val feedRepo = FeedRepositoryImpl(client)
        val exploreRepo = ExploreRepositoryImpl(client)
        val chartsRepo = ChartsRepositoryImpl(client)
        val authRepo = AuthRepositoryImpl(client)
        val userRepo = UserRepositoryImpl(client)
        val castRepo = CastRepositoryImpl(client)
        val seasonRepo = SeasonRepositoryImpl(client)
        val studioRepo = StudioRepositoryImpl(client)

        // Reviews (missing activityStatus fix!)
        check("GET /anime/1/reviews", { if (it.data.isEmpty()) "empty" else null }) { showRepo.getShowReviews("1") }
        check("GET /anime/22598/reviews", { if (it.data.isEmpty()) "empty" else null }) { showRepo.getShowReviews("22598") }

        // Show staff (full StaffResponse), related, more-by-studio
        check("GET /anime/22598/staff", { if (it.data.isEmpty() || it.data.first().attributes.role.name.isBlank()) "empty or missing role" else null }) {
            showRepo.getShowStaff("22598")
        }
        check("GET /anime/22598/related-shows", { if (it.data.isEmpty()) "empty" else null }) { showRepo.getRelatedShows("22598") }
        check("GET /anime/22598/related-literatures", { if (it.data.isEmpty()) "empty" else null }) { showRepo.getRelatedLiteratures("22598") }
        check("GET /anime/22598/related-games", { if (it.data.isEmpty()) "empty" else null }) { showRepo.getRelatedGames("22598") }
        check("GET /anime/22598/more-by-studio", { if (it.data.isEmpty()) "empty" else null }) { showRepo.getMoreByStudio("22598") }

        // Synced lyrics (object-array agents fix!)
        check("GET /songs/21189/lyrics", { resp ->
            val lyric = resp.data.firstOrNull() ?: return@check "empty"
            if (lyric.attributes.agents.isEmpty() || lyric.attributes.lines.isEmpty()) "missing agents/lines" else null
        }) { songRepo.getSongLyrics("21189") }

        // Social feed
        check("GET /feed/explore", { if (it.data.isEmpty()) "empty" else null }) { feedRepo.getExploreFeed() }

        // Explore + category + page 2 via next cursor
        check("GET /explore", { if (it.data.isEmpty()) "empty" else null }) { exploreRepo.getExplore() }
        val exp1 = check("GET /explore/1?limit=20", { if (it.data.isEmpty()) "empty" else null }) {
            exploreRepo.getExplore("1", limit = 20)
        }
        val expNext = exp1?.data?.firstOrNull()?.relationships?.shows?.next
        if (expNext != null) {
            check("GET /explore/1 page 2 ($expNext)", {
                if (it.data.firstOrNull()?.relationships?.shows?.data.isNullOrEmpty()) "page 2 empty" else null
            }) { exploreRepo.getExplore("1", next = expNext, limit = 20) }
        } else {
            failures += "GET /explore/1?limit=20 -> missing next cursor"
        }

        // All 8 Charts endpoints
        check("GET /charts/anime", { if (it.data.isEmpty()) "empty" else null }) { chartsRepo.getChartsAnime() }
        check("GET /charts/manga", { if (it.data.isEmpty()) "empty" else null }) { chartsRepo.getChartsManga() }
        check("GET /charts/games", { if (it.data.isEmpty()) "empty" else null }) { chartsRepo.getChartsGames() }
        check("GET /charts/episodes", { if (it.data.isEmpty()) "empty" else null }) { chartsRepo.getChartsEpisodes() }
        check("GET /charts/songs", { if (it.data.isEmpty()) "empty" else null }) { chartsRepo.getChartsSongs() }
        check("GET /charts/characters", { if (it.data.isEmpty()) "empty" else null }) { chartsRepo.getChartsCharacters() }
        check("GET /charts/people", { if (it.data.isEmpty()) "empty" else null }) { chartsRepo.getChartsPeople() }
        val chartStudios = check("GET /charts/studios", { if (it.data.isEmpty()) "empty" else null }) { chartsRepo.getChartsStudios() }

        // User profile
        check("GET /users/2/profile", { if (it.data.isEmpty()) "empty" else null }) { authRepo.getUserProfile("2") }

        // Trailers & Museum
        check("GET /anime/trailers", { if (it.data.isEmpty()) "empty" else null }) { showRepo.getShowTrailers() }
        check("GET /games/trailers", { if (it.data.isEmpty()) "empty" else null }) { gameRepo.getGameTrailers() }
        check("GET /anime/museum", { if (it.data.isEmpty()) "empty" else null }) { showRepo.getShowMuseum() }
        check("GET /anime/museum/2024", { if (it.data.isEmpty()) "empty" else null }) { showRepo.getShowMuseumYear(2024) }
        check("GET /manga/museum", { if (it.data.isEmpty()) "empty" else null }) { litRepo.getLiteratureMuseum() }
        check("GET /games/museum", { if (it.data.isEmpty()) "empty" else null }) { gameRepo.getGameMuseum() }

        // Batch hydration endpoints
        val castIds = showRepo.getShowCast("22598", limit = 3).let { if (it is Result.Success) it.data.data.map { c -> c.id } else emptyList() }
        check("GET /show-cast?ids=...", { if (it.data.isEmpty()) "empty" else null }) { castRepo.getShowCastByIds(castIds) }

        val seasonIds = showRepo.getShowSeasons("22598", limit = 2).let { if (it is Result.Success) it.data.data.map { s -> s.id } else emptyList() }
        check("GET /seasons?ids=...", { if (it.data.isEmpty()) "empty" else null }) { seasonRepo.getSeasons(seasonIds) }

        check("GET /users?ids=2 (AuthRepo)", { if (it.data.isEmpty()) "empty" else null }) { authRepo.getUsers(listOf("2")) }
        check("GET /users?ids=2 (UserRepo)", { if (it.data.isEmpty()) "empty" else null }) { userRepo.getUsers(listOf("2")) }

        val studioIds = chartStudios?.data?.take(2)?.map { it.id }.orEmpty()
        check("GET /studios?ids=...", { if (it.data.isEmpty()) "empty" else null }) { studioRepo.getStudios(studioIds) }

        // Manga/1 + characters/staff & Games/1 + characters/staff
        check("GET /manga/1?include[]=characters&include[]=staff", { resp ->
            val lit = resp.data.firstOrNull() ?: return@check "empty"
            if (lit.relationships?.characters?.data.isNullOrEmpty() || lit.relationships?.staff?.data.isNullOrEmpty()) "missing included rels" else null
        }) { litRepo.getLiterature("1", listOf("characters", "staff")) }
        check("GET /manga/1/characters", { if (it.data.isEmpty()) "empty" else null }) { litRepo.getLiteratureCharacters("1") }
        check("GET /manga/1/staff", { if (it.data.isEmpty()) "empty" else null }) { litRepo.getLiteratureStaff("1") }

        check("GET /games/1?include[]=characters&include[]=staff", { resp ->
            val game = resp.data.firstOrNull() ?: return@check "empty"
            if (game.relationships?.characters?.data.isNullOrEmpty() || game.relationships?.staff?.data.isNullOrEmpty()) "missing included rels" else null
        }) { gameRepo.getGame("1", listOf("characters", "staff")) }
        check("GET /games/1/characters", { if (it.data.isEmpty()) "empty" else null }) { gameRepo.getGameCharacters("1") }
        check("GET /games/1/staff", { if (it.data.isEmpty()) "empty" else null }) { gameRepo.getGameStaff("1") }

        println("=== LIVE PHASE 1 VERIFICATION: $passed passed, ${failures.size} failed ===")
        if (failures.isNotEmpty()) {
            fail("Live Phase 1 verification had ${failures.size} failures:\n" + failures.joinToString("\n"))
        }
    }
}
