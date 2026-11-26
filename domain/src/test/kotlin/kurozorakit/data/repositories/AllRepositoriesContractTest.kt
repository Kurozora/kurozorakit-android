package kurozorakit.data.repositories

import kotlinx.coroutines.test.runTest
import kurozorakit.data.enums.DiscordActivityName
import kurozorakit.data.enums.DiscordPresenceImage
import kurozorakit.data.enums.KKLibrary
import kurozorakit.data.enums.KKScheduleType
import kurozorakit.data.enums.KKSearchScope
import kurozorakit.data.enums.KKSearchType
import kurozorakit.data.enums.MediaCollection
import kurozorakit.data.enums.MediaKind
import kurozorakit.data.enums.ParentalGuideReportReason
import kurozorakit.data.enums.RatingStyle
import kurozorakit.data.enums.ReadStatus
import kurozorakit.data.enums.ReportReason
import kurozorakit.data.enums.ReviewKind
import kurozorakit.data.enums.ReviewRecommendation
import kurozorakit.data.enums.SeasonOfYear
import kurozorakit.data.enums.UsersListType
import kurozorakit.data.enums.WeeklyDigestSectionType
import kurozorakit.data.models.feed.message.FeedMessageRequest
import kurozorakit.data.models.misc.LibraryImport
import kurozorakit.data.models.parentalguide.request.ParentalGuideEntryRequest
import kurozorakit.data.models.user.update.UserUpdate
import kurozorakit.data.repositories.RepositoryTestHarness.createClient
import kurozorakit.data.repositories.RepositoryTestHarness.respondBytes
import kurozorakit.data.repositories.RepositoryTestHarness.respondJson
import kurozorakit.data.repositories.charts.ChartsRepositoryImpl
import kurozorakit.data.repositories.airseason.AirSeasonRepositoryImpl
import kurozorakit.data.repositories.auth.AuthRepositoryImpl
import kurozorakit.data.repositories.cast.CastRepositoryImpl
import kurozorakit.data.repositories.character.CharacterRepositoryImpl
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
import kurozorakit.data.repositories.store.StoreRepositoryImpl
import kurozorakit.data.repositories.studio.StudioRepositoryImpl
import kurozorakit.data.repositories.theme.ThemeRepositoryImpl
import kurozorakit.data.repositories.themestore.ThemeStoreRepositoryImpl
import kurozorakit.data.repositories.user.UserRepositoryImpl
import kurozorakit.shared.Result
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Comprehensive contract test suite covering ALL 26 domain repositories against
 * the kurozora-web v1.15.0 API specification (HTTP methods, paths, query parameters,
 * request body encodings, pagination cursors, and JSON response deserialization).
 */
class AllRepositoriesContractTest {

    // =========================================================================
    // 1. AirSeasonRepository
    // =========================================================================

    @Test
    fun `AirSeasonRepository sends query params year and integer season`() = runTest {
        val requests = mutableListOf<RecordedRequest>()
        val client = createClient(requests) {
            respondJson("""{"data":[],"next":null}""")
        }
        val repo = AirSeasonRepositoryImpl(client)

        assertIs<Result.Success<*>>(repo.getAnimeSeason("2025", SeasonOfYear.WINTER))
        assertIs<Result.Success<*>>(repo.getMangaSeason("2025", SeasonOfYear.SPRING))
        assertIs<Result.Success<*>>(repo.getGameSeason("2025", SeasonOfYear.FALL))

        assertEquals(3, requests.size)
        assertEquals("/v1/anime/seasons", requests[0].path)
        assertEquals("2025", requests[0].queryParameters["year"])
        assertEquals("0", requests[0].queryParameters["season"])

        assertEquals("/v1/manga/seasons", requests[1].path)
        assertEquals("2025", requests[1].queryParameters["year"])
        assertEquals("1", requests[1].queryParameters["season"])

        assertEquals("/v1/games/seasons", requests[2].path)
        assertEquals("2025", requests[2].queryParameters["year"])
        assertEquals("3", requests[2].queryParameters["season"])
    }

    // =========================================================================
    // 2. AuthRepository
    // =========================================================================

    @Test
    fun `AuthRepository contract covers signin, 2FA, SIWA, deleteUser, blocked, library, and social routes`() = runTest {
        val requests = mutableListOf<RecordedRequest>()
        val client = createClient(requests) { req ->
            when {
                req.url.encodedPath == "/v1/users/signin" ->
                    respondJson("""{"two_factor":true,"challenge_token":"tok_2fa"}""")
                req.url.encodedPath == "/v1/users/siwa/signin" ->
                    respondJson("""{"authenticationToken":"siwa_tok","action":"signIn"}""")
                req.url.encodedPath == "/v1/users/reset-password" ->
                    respondJson("""{}""")
                req.url.encodedPath == "/v1/users/delete" ->
                    respondJson("""{}""")
                req.url.encodedPath.endsWith("/follow") ->
                    respondJson("""{"data":{"isFollowed":true}}""")
                req.url.encodedPath.endsWith("/block") ->
                    respondJson("""{"data":{"isBlocked":true}}""")
                req.url.encodedPath.endsWith("/library") ->
                    respondJson("""{"data":{"shows":[],"literatures":[],"games":[]},"next":null}""")
                req.url.encodedPath.endsWith("/favorites") ->
                    respondJson("""{"data":{"shows":[],"literatures":[],"games":[]},"next":null}""")
                req.url.encodedPath.endsWith("/profile") ->
                    respondJson("""{"data":[]}""")
                else ->
                    respondJson("""{"data":[],"next":null}""")
            }
        }
        val repo = AuthRepositoryImpl(client)

        assertIs<Result.Success<*>>(repo.signIn("a@b.com", "pw"))
        assertIs<Result.Success<*>>(repo.signInWithApple("idTok", "authCode"))
        assertIs<Result.Success<*>>(repo.resetPassword("a@b.com"))
        assertIs<Result.Success<*>>(repo.getFollowList("42", UsersListType.followers))
        assertIs<Result.Success<*>>(repo.updateFollowStatus("42"))
        assertIs<Result.Success<*>>(repo.updateBlockStatus("42"))
        assertIs<Result.Success<*>>(repo.getBlockedUsers("42"))
        assertIs<Result.Success<*>>(repo.getUserFavorites("42", KKLibrary.Kind.SHOWS))
        assertIs<Result.Success<*>>(repo.getUserLibrary("42", KKLibrary.Kind.SHOWS, KKLibrary.Status.INPROGRESS, KKLibrary.SortType.ALPHABETICALLY, KKLibrary.Option.ASCENDING))
        assertIs<Result.Success<*>>(repo.getUserFeedMessages("42"))
        assertIs<Result.Success<*>>(repo.getUserFollowers("42"))
        assertIs<Result.Success<*>>(repo.getUserFollowing("42"))
        assertIs<Result.Success<*>>(repo.getUserProfile("42"))
        assertIs<Result.Success<*>>(repo.getUserReviews("42"))
        assertIs<Result.Success<*>>(repo.searchUsers("kirito"))
        assertIs<Result.Success<*>>(repo.deleteUser("my-pass"))

        // Verify SIWA uses form urlencoded body
        val siwaReq = requests.first { it.path == "/v1/users/siwa/signin" }
        assertEquals("POST", siwaReq.method)
        assertEquals(listOf("idTok"), siwaReq.formParameters["identity_token"])
        assertEquals(listOf("authCode"), siwaReq.formParameters["authorization_code"])

        // Verify getBlockedUsers hits /me/blocked (users/{id}/blocked does not exist)
        val blockedReq = requests.first { it.path == "/v1/me/blocked" }
        assertEquals("GET", blockedReq.method)

        // Verify deleteUser uses HTTP DELETE with password in form body
        val deleteReq = requests.first { it.path == "/v1/users/delete" }
        assertEquals("DELETE", deleteReq.method)
        assertEquals(listOf("my-pass"), deleteReq.formParameters["password"])

        // Verify library sends kind=0 and sort
        val libReq = requests.first { it.path == "/v1/users/42/library" }
        assertEquals("0", libReq.queryParameters["kind"])
        assertNotNull(libReq.queryParameters["sort"])
    }

    // =========================================================================
    // 3. UserRepository
    // =========================================================================

    @Test
    fun `UserRepository contract covers library mutations, reminders, settings, digest, recap stats, and timeout appeals`() = runTest {
        val requests = mutableListOf<RecordedRequest>()
        val client = createClient(requests) { req ->
            when (req.url.encodedPath) {
                "/v1/me" -> if (req.method.value == "POST") {
                    respondJson("""{"data":{"username":"u1","nickname":"n1"},"message":"Profile updated"}""")
                } else {
                    respondJson("""{"data":[]}""")
                }
                "/v1/me/settings" ->
                    respondJson("""{"data":[{"id":"1","type":"user-settings","href":"/v1/me/settings","attributes":{"scrobbleThreshold":85,"discordRichPresenceEnabled":true,"discordPresenceImage":1,"discordActivityName":0,"ratingStyle":0}}]}""")
                "/v1/me/digest", "/v1/me/digest/up-next" ->
                    respondJson("""{"data":[{"id":"up-next","type":"weekly-digest-sections","attributes":{"kind":"up-next","title":"Up Next","position":0}}]}""")
                "/v1/me/timeout/appeal", "/v1/me/timeout/appeal/update" ->
                    respondJson("""{"data":[{"id":"9","type":"timeouts","attributes":{"reasonKey":1,"reasonLabel":"Spam","expiresAt":1800000000,"isPermanent":false}}]}""")
                "/v1/me/favorites" -> if (req.method.value == "POST") {
                    respondJson("""{"data":{"isFavorited":true}}""")
                } else {
                    respondJson("""{"data":{"shows":[],"literatures":[],"games":[]},"next":null}""")
                }
                "/v1/me/library" -> if (req.method.value == "POST") {
                    respondJson("""{"data":{"attributes":{"libraryStatus":0,"isFavorited":false,"isReminded":false,"isHidden":false,"rewatchCount":0}}}""")
                } else {
                    respondJson("""{"data":{"shows":[],"literatures":[],"games":[]},"next":null}""")
                }
                "/v1/me/library/update", "/v1/me/library/delete" ->
                    respondJson("""{"data":{"attributes":{"libraryStatus":0,"isFavorited":false,"isReminded":false,"isHidden":true,"rewatchCount":2}}}""")
                "/v1/me/library/sync" ->
                    respondJson("""{"data":{"attributes":{"syncTime":1700000000,"streams":{}},"relationships":{"entries":[],"trackables":[],"episodes":[]}}}""")
                "/v1/me/library/import" ->
                    respondJson("""{"message":"Import queued"}""")
                "/v1/me/notifications/1" ->
                    respondJson("""{"data":{"id":"1","type":"notifications","href":"/v1/me/notifications/1","attributes":{"type":"NewFollower","description":"hi","payload":{},"createdAt":1700000000,"isRead":false}}}""")
                "/v1/me/notifications/update" ->
                    respondJson("""{"data":{"isRead":true}}""")
                "/v1/me/reminders" -> if (req.method.value == "POST") {
                    respondJson("""{"data":{"isReminded":true}}""")
                } else {
                    respondJson("""{"data":{"shows":[],"literatures":[],"games":[]},"next":null}""")
                }
                "/v1/me/reminders/download" ->
                    respondBytes("BEGIN:VCALENDAR".encodeToByteArray())
                "/v1/me/recap/2025/12/stats" ->
                    respondJson("""{"data":[{"id":"1","type":"recap-stats","attributes":{"year":2025,"month":12,"stat":3,"value":10}}]}""")
                else ->
                    respondJson("""{"data":[],"next":null}""")
            }
        }
        val repo = UserRepositoryImpl(client)

        assertIs<Result.Success<*>>(repo.getMyProfile())
        assertIs<Result.Success<*>>(repo.getUsers(listOf("1", "2")))
        assertIs<Result.Success<*>>(repo.updateMyProfile(UserUpdate(nickname = "NewNick")))
        assertIs<Result.Success<*>>(repo.getMyFollowers())
        assertIs<Result.Success<*>>(repo.getMyFollowing())
        assertIs<Result.Success<*>>(repo.getMyBlockedList())
        assertIs<Result.Success<*>>(repo.getMyReviews())
        assertIs<Result.Success<*>>(repo.getMyAchievements())

        val settings = assertIs<Result.Success<*>>(repo.getMySettings())
        assertNotNull(settings.data)
        assertIs<Result.Success<*>>(
            repo.updateMySettings(
                scrobbleThreshold = 90,
                discordRichPresenceEnabled = true,
                discordPresenceImage = DiscordPresenceImage.poster,
                discordActivityName = DiscordActivityName.title,
                ratingStyle = RatingStyle.standard,
            )
        )
        assertIs<Result.Success<*>>(repo.getMyDigest())
        assertIs<Result.Success<*>>(repo.getMyDigestSection(WeeklyDigestSectionType.upNext))
        assertIs<Result.Success<*>>(repo.getMyUpNextEpisodes(modelId = "10"))
        assertIs<Result.Success<*>>(repo.getMyWatchedEpisodes(modelId = "10"))
        assertIs<Result.Success<*>>(repo.submitTimeoutAppeal("Sorry!"))
        assertIs<Result.Success<*>>(repo.updateTimeoutAppeal("Updated appeal"))

        assertIs<Result.Success<*>>(repo.getAccessTokens())
        assertIs<Result.Success<*>>(repo.getAccessToken("99|secret"))
        assertIs<Result.Success<*>>(repo.updateAccessToken("99|secret", "apn_tok"))
        assertIs<Result.Success<*>>(repo.deleteAccessToken("99|secret"))
        assertIs<Result.Success<*>>(repo.deleteAccessTokens("pw", listOf("99", "100")))

        assertIs<Result.Success<*>>(repo.getMyFavorites(KKLibrary.Kind.SHOWS))
        assertIs<Result.Success<*>>(repo.updateMyFavorites(KKLibrary.Kind.SHOWS, listOf("1", "2"), isFavorited = true))
        assertIs<Result.Success<*>>(repo.getMyFeedMessages())

        assertIs<Result.Success<*>>(repo.getMyLibrary(KKLibrary.Kind.SHOWS, KKLibrary.Status.INPROGRESS))
        assertIs<Result.Success<*>>(repo.addToLibrary(KKLibrary.Kind.SHOWS, KKLibrary.Status.INPROGRESS, listOf("1", "2")))
        assertIs<Result.Success<*>>(repo.updateMyLibrary(KKLibrary.Kind.SHOWS, listOf("1", "2"), rewatchCount = 2, isHidden = true))
        assertIs<Result.Success<*>>(repo.removeFromMyLibrary(KKLibrary.Kind.SHOWS, listOf("1", "2")))
        assertIs<Result.Success<*>>(repo.clearMyLibrary(KKLibrary.Kind.LITERATURES, "my-pass"))
        assertIs<Result.Success<*>>(repo.syncMyLibrary(limit = 500))
        assertIs<Result.Success<*>>(repo.importToLibrary(KKLibrary.Kind.SHOWS, LibraryImport.Service.MAL, LibraryImport.Behavior.MERGE, "<xml/>".encodeToByteArray()))

        assertIs<Result.Success<*>>(repo.getUserAchievements("42"))
        assertIs<Result.Success<*>>(repo.getMyNotifications())
        assertIs<Result.Success<*>>(repo.getMyNotification("1"))
        assertIs<Result.Success<*>>(repo.deleteMyNotification("1"))
        assertIs<Result.Success<*>>(repo.deleteMyNotifications(listOf("1", "2")))
        assertIs<Result.Success<*>>(repo.deleteAllMyNotifications())
        assertIs<Result.Success<*>>(repo.updateMyNotification("1", ReadStatus.read))

        assertIs<Result.Success<*>>(repo.getMyRecaps())
        assertIs<Result.Success<*>>(repo.getMyRecapDetails("2025", "12"))
        assertIs<Result.Success<*>>(repo.getMyRecapStats("2025", "12"))

        assertIs<Result.Success<*>>(repo.getMyReminders(KKLibrary.Kind.SHOWS))
        val reminderRes = assertIs<Result.Success<*>>(repo.updateReminderStatus(KKLibrary.Kind.SHOWS, listOf("1", "2"), isReminded = true))
        assertNotNull(reminderRes.data)
        val ics = assertIs<Result.Success<ByteArray>>(repo.downloadMyReminders())
        assertContentEquals("BEGIN:VCALENDAR".encodeToByteArray(), ics.data)

        assertIs<Result.Success<*>>(repo.getMySessions())
        assertIs<Result.Success<*>>(repo.getMySession("sess-1"))
        assertIs<Result.Success<*>>(repo.deleteMySession("sess-1"))
        assertIs<Result.Success<*>>(repo.deleteMySessions("pw", listOf("sess-1", "sess-2")))

        // Verify critical bug fixes:
        val addLib = requests.first { it.path == "/v1/me/library" && it.method == "POST" }
        assertEquals(listOf("0"), addLib.formParameters["kind"])
        assertEquals(listOf("1,2"), addLib.formParameters["model_ids"])

        val updLib = requests.first { it.path == "/v1/me/library/update" }
        assertEquals(listOf("0"), updLib.formParameters["kind"])
        assertEquals(listOf("1,2"), updLib.formParameters["model_ids"])
        assertEquals(listOf("2"), updLib.formParameters["rewatch_count"])
        assertEquals(listOf("1"), updLib.formParameters["is_hidden"])

        val clearLib = requests.first { it.path == "/v1/me/library/clear" }
        assertEquals("DELETE", clearLib.method)
        assertEquals(listOf("1"), clearLib.formParameters["kind"])
        assertEquals(listOf("my-pass"), clearLib.formParameters["password"])

        val updToken = requests.first { it.path == "/v1/me/access-tokens/99/update" }
        assertEquals(listOf("apn_tok"), updToken.formParameters["apn_device_token"])

        val updReminder = requests.first { it.path == "/v1/me/reminders" && it.method == "POST" }
        assertEquals(listOf("0"), updReminder.formParameters["library"])
        assertEquals(listOf("1,2"), updReminder.formParameters["model_ids"])
        assertEquals(listOf("1"), updReminder.formParameters["is_reminded"])
    }

    // =========================================================================
    // 4. ShowRepository
    // =========================================================================

    @Test
    fun `ShowRepository covers all endpoints, cast-staff next pagination, rating recommendation, and v1_15 routes`() = runTest {
        val requests = mutableListOf<RecordedRequest>()
        val client = createClient(requests) { req ->
            when {
                req.url.encodedPath == "/v1/anime" && req.url.parameters["ids"] == null ->
                    respondJson("""{"data":{"shows":{"data":[],"next":null}}}""")
                req.url.encodedPath == "/v1/anime/mapping" ->
                    respondJson("""{"data":[{"slug":"naruto","myanimelist":20}]}""")
                req.url.encodedPath.endsWith("/rate") ->
                    respondJson("""{}""")
                else ->
                    respondJson("""{"data":[],"next":null}""")
            }
        }
        val repo = ShowRepositoryImpl(client)

        assertIs<Result.Success<*>>(repo.getShows())
        assertIs<Result.Success<*>>(repo.getShows(listOf("1", "2"), listOf("genres")))
        assertIs<Result.Success<*>>(repo.getShow("1", listOf("seasons")))
        assertIs<Result.Success<*>>(repo.getUpcomingShows())
        assertIs<Result.Success<*>>(repo.getShowTrailers())
        assertIs<Result.Success<*>>(repo.getShowMuseum())
        assertIs<Result.Success<*>>(repo.getShowMuseumYear(2024))
        assertIs<Result.Success<*>>(repo.getShowMapping(malId = 20))
        assertIs<Result.Success<*>>(repo.getShowCast("1", next = "/v1/anime/1/cast?cursor=c2&limit=20"))
        assertIs<Result.Success<*>>(repo.getShowStaff("1", next = "/v1/anime/1/staff?cursor=s2&limit=20"))
        assertIs<Result.Success<*>>(repo.getShowCharacters("1"))
        assertIs<Result.Success<*>>(repo.getShowPeople("1"))
        assertIs<Result.Success<*>>(repo.getShowVideos("1"))
        assertIs<Result.Success<*>>(repo.getShowLanguages("1"))
        assertIs<Result.Success<*>>(repo.getShowEditorial("1"))
        assertIs<Result.Success<*>>(repo.getShowReviews("1"))
        assertIs<Result.Success<*>>(repo.getShowSeasons("1"))
        assertIs<Result.Success<*>>(repo.getShowSongs("1"))
        assertIs<Result.Success<*>>(repo.getShowStudios("1"))
        assertIs<Result.Success<*>>(repo.getMoreByStudio("1"))
        assertIs<Result.Success<*>>(repo.getRelatedShows("1"))
        assertIs<Result.Success<*>>(repo.getRelatedLiteratures("1"))
        assertIs<Result.Success<*>>(repo.getRelatedGames("1"))
        assertIs<Result.Success<*>>(repo.rateShow("1", 4.5, "Great!", ReviewRecommendation.recommended, isSpoiler = false))
        assertIs<Result.Success<*>>(repo.deleteShowRating("1"))

        val castNext = requests.first { it.path == "/v1/anime/1/cast" }
        assertEquals("https://api.kurozora.app/v1/anime/1/cast?cursor=c2&limit=20", castNext.fullUrl)
        val staffNext = requests.first { it.path == "/v1/anime/1/staff" }
        assertEquals("https://api.kurozora.app/v1/anime/1/staff?cursor=s2&limit=20", staffNext.fullUrl)

        val rateReq = requests.first { it.path == "/v1/anime/1/rate" && it.method == "POST" }
        assertEquals(listOf("4.5"), rateReq.formParameters["rating"])
        assertEquals(listOf("Great!"), rateReq.formParameters["description"])
        assertEquals(listOf("2"), rateReq.formParameters["recommendation"])
        assertEquals(listOf("0"), rateReq.formParameters["isSpoiler"])

        val delRate = requests.first { it.path == "/v1/anime/1/rate" && it.method == "DELETE" }
        assertEquals("DELETE", delRate.method)
    }

    // =========================================================================
    // 5. LiteratureRepository
    // =========================================================================

    @Test
    fun `LiteratureRepository covers all manga endpoints and v1_15 additions`() = runTest {
        val requests = mutableListOf<RecordedRequest>()
        val client = createClient(requests) { req ->
            if (req.url.encodedPath == "/v1/manga" && req.url.parameters["ids"] == null) {
                respondJson("""{"data":{"literatures":{"data":[],"next":null}}}""")
            } else {
                respondJson("""{"data":[],"next":null}""")
            }
        }
        val repo = LiteratureRepositoryImpl(client)

        assertIs<Result.Success<*>>(repo.getLiteratures())
        assertIs<Result.Success<*>>(repo.getLiteratures(listOf("1"), listOf("genres")))
        assertIs<Result.Success<*>>(repo.getLiterature("1"))
        assertIs<Result.Success<*>>(repo.getUpcomingLiteratures())
        assertIs<Result.Success<*>>(repo.getAdaptedLiteratures())
        assertIs<Result.Success<*>>(repo.getLiteratureMuseum())
        assertIs<Result.Success<*>>(repo.getLiteratureMuseumYear(2024))
        assertIs<Result.Success<*>>(repo.getLiteratureGallery("1"))
        assertIs<Result.Success<*>>(repo.getLiteratureLanguages("1"))
        assertIs<Result.Success<*>>(repo.getLiteratureEditorial("1"))
        assertIs<Result.Success<*>>(repo.getLiteratureCast("1"))
        assertIs<Result.Success<*>>(repo.getLiteratureStaff("1"))
        assertIs<Result.Success<*>>(repo.getLiteratureCharacters("1"))
        assertIs<Result.Success<*>>(repo.getLiteraturePeople("1"))
        assertIs<Result.Success<*>>(repo.getLiteratureReviews("1"))
        assertIs<Result.Success<*>>(repo.getLiteratureStudios("1"))
        assertIs<Result.Success<*>>(repo.getMoreByStudio("1"))
        assertIs<Result.Success<*>>(repo.getRelatedShows("1"))
        assertIs<Result.Success<*>>(repo.getRelatedLiteratures("1"))
        assertIs<Result.Success<*>>(repo.getRelatedGames("1"))
        assertIs<Result.Success<*>>(repo.rateLiterature("1", 5.0, "Masterpiece", ReviewRecommendation.recommended))
        assertIs<Result.Success<*>>(repo.deleteLiteratureRating("1"))

        assertTrue(requests.any { it.path == "/v1/manga/adapted" && it.method == "GET" })
        assertTrue(requests.any { it.path == "/v1/manga/1/gallery" && it.method == "GET" })
        assertTrue(requests.any { it.path == "/v1/manga/1/languages" && it.method == "GET" })
        assertTrue(requests.any { it.path == "/v1/manga/1/editorial" && it.method == "GET" })
        assertTrue(requests.any { it.path == "/v1/manga/1/rate" && it.method == "DELETE" })
    }

    // =========================================================================
    // 6. GameRepository
    // =========================================================================

    @Test
    fun `GameRepository parses CastIdentityResponse on game cast and covers v1_15 endpoints`() = runTest {
        val requests = mutableListOf<RecordedRequest>()
        val client = createClient(requests) { req ->
            when {
                req.url.encodedPath == "/v1/games" && req.url.parameters["ids"] == null ->
                    respondJson("""{"data":{"games":{"data":[],"next":null}}}""")
                req.url.encodedPath == "/v1/games/5/cast" ->
                    respondJson("""{"data":[{"id":"101","type":"game-cast","href":"/v1/game-cast/101"}],"next":null}""")
                else ->
                    respondJson("""{"data":[],"next":null}""")
            }
        }
        val repo = GameRepositoryImpl(client)

        assertIs<Result.Success<*>>(repo.getGames())
        assertIs<Result.Success<*>>(repo.getGames(listOf("5")))
        assertIs<Result.Success<*>>(repo.getGame("5"))
        assertIs<Result.Success<*>>(repo.getUpcomingGames())
        assertIs<Result.Success<*>>(repo.getAdaptedGames())
        assertIs<Result.Success<*>>(repo.getGameTrailers())
        assertIs<Result.Success<*>>(repo.getGameMuseum())
        assertIs<Result.Success<*>>(repo.getGameMuseumYear(2024))
        val castResult = assertIs<Result.Success<*>>(repo.getGameCast("5"))
        assertNotNull(castResult.data)
        assertIs<Result.Success<*>>(repo.getGameStaff("5"))
        assertIs<Result.Success<*>>(repo.getGameCharacters("5"))
        assertIs<Result.Success<*>>(repo.getGameSongs("5"))
        assertIs<Result.Success<*>>(repo.getGameLanguages("5"))
        assertIs<Result.Success<*>>(repo.getGameEditorial("5"))
        assertIs<Result.Success<*>>(repo.getGameReviews("5"))
        assertIs<Result.Success<*>>(repo.getGameStudios("5"))
        assertIs<Result.Success<*>>(repo.getMoreByStudio("5"))
        assertIs<Result.Success<*>>(repo.getRelatedShows("5"))
        assertIs<Result.Success<*>>(repo.getRelatedLiteratures("5"))
        assertIs<Result.Success<*>>(repo.getRelatedGames("5"))
        assertIs<Result.Success<*>>(repo.rateGame("5", 4.0))

        assertTrue(requests.any { it.path == "/v1/games/adapted" })
        assertTrue(requests.any { it.path == "/v1/games/5/songs" })
        assertTrue(requests.any { it.path == "/v1/games/5/languages" })
        assertTrue(requests.any { it.path == "/v1/games/5/editorial" })
    }

    // =========================================================================
    // 7-11. Character, Person, Studio, Song, Episode Repositories
    // =========================================================================

    @Test
    fun `Character, Person, Studio, Song, and Episode repositories match routes and form encodings`() = runTest {
        val requests = mutableListOf<RecordedRequest>()
        val client = createClient(requests) { req ->
            when {
                req.url.encodedPath == "/v1/characters" && req.url.parameters["ids"] == null ->
                    respondJson("""{"data":{"characters":{"data":[],"next":null}}}""")
                req.url.encodedPath == "/v1/people" && req.url.parameters["ids"] == null ->
                    respondJson("""{"data":{"people":{"data":[],"next":null}}}""")
                req.url.encodedPath == "/v1/studios" && req.url.parameters["ids"] == null ->
                    respondJson("""{"data":{"studios":{"data":[],"next":null}}}""")
                req.url.encodedPath == "/v1/songs" && req.url.parameters["ids"] == null ->
                    respondJson("""{"data":{"songs":{"data":[],"next":null}}}""")
                req.url.encodedPath == "/v1/episodes/7/watched" ->
                    respondJson("""{"data":{"isWatched":${req.method.value == "POST"}}}""")
                req.url.encodedPath == "/v1/songs/8/lyrics" ->
                    respondJson("""{"data":[{"id":"8","type":"song-lyrics","attributes":{"lyrics":"la la la","isSynced":false}}]}""")
                else ->
                    respondJson("""{"data":[],"next":null}""")
            }
        }
        val charRepo = CharacterRepositoryImpl(client)
        assertIs<Result.Success<*>>(charRepo.getCharacters())
        assertIs<Result.Success<*>>(charRepo.getCharacters(listOf("1")))
        assertIs<Result.Success<*>>(charRepo.getCharacter("1"))
        assertIs<Result.Success<*>>(charRepo.getCharacterGallery("1"))
        assertIs<Result.Success<*>>(charRepo.getCharacterPeople("1"))
        assertIs<Result.Success<*>>(charRepo.getCharacterShows("1"))
        assertIs<Result.Success<*>>(charRepo.getCharacterLiteratures("1"))
        assertIs<Result.Success<*>>(charRepo.getCharacterGames("1"))
        assertIs<Result.Success<*>>(charRepo.getCharacterReviews("1"))
        assertIs<Result.Success<*>>(charRepo.rateCharacter("1", 4.5))
        assertIs<Result.Success<*>>(charRepo.deleteCharacterRating("1"))

        val personRepo = PersonRepositoryImpl(client)
        assertIs<Result.Success<*>>(personRepo.getPeople())
        assertIs<Result.Success<*>>(personRepo.getPeople(listOf("2")))
        assertIs<Result.Success<*>>(personRepo.getPerson("2"))
        assertIs<Result.Success<*>>(personRepo.getPersonGallery("2"))
        assertIs<Result.Success<*>>(personRepo.getPersonRelationships("2"))
        assertIs<Result.Success<*>>(personRepo.getPersonShows("2"))
        assertIs<Result.Success<*>>(personRepo.getPersonGames("2"))
        assertIs<Result.Success<*>>(personRepo.getPersonLiteratures("2"))
        assertIs<Result.Success<*>>(personRepo.getPersonCharacters("2"))
        assertIs<Result.Success<*>>(personRepo.getPersonReviews("2"))
        assertIs<Result.Success<*>>(personRepo.ratePerson("2", 5.0))
        assertIs<Result.Success<*>>(personRepo.deletePersonRating("2"))

        val studioRepo = StudioRepositoryImpl(client)
        assertIs<Result.Success<*>>(studioRepo.getStudios())
        assertIs<Result.Success<*>>(studioRepo.getStudios(listOf("3")))
        assertIs<Result.Success<*>>(studioRepo.getStudio("3"))
        assertIs<Result.Success<*>>(studioRepo.getStudioPredecessors("3"))
        assertIs<Result.Success<*>>(studioRepo.getStudioSuccessors("3"))
        assertIs<Result.Success<*>>(studioRepo.getStudioGames("3"))
        assertIs<Result.Success<*>>(studioRepo.getStudioLiteratures("3"))
        assertIs<Result.Success<*>>(studioRepo.getStudioShows("3"))
        assertIs<Result.Success<*>>(studioRepo.getStudioReviews("3"))
        assertIs<Result.Success<*>>(studioRepo.rateStudio("3", 4.0))
        assertIs<Result.Success<*>>(studioRepo.deleteStudioRating("3"))

        val songRepo = SongRepositoryImpl(client)
        assertIs<Result.Success<*>>(songRepo.getSongs())
        assertIs<Result.Success<*>>(songRepo.getSongs(listOf("8")))
        assertIs<Result.Success<*>>(songRepo.getSong("8"))
        assertIs<Result.Success<*>>(songRepo.getSongLyrics("8"))
        assertIs<Result.Success<*>>(songRepo.getSongShows("8"))
        assertIs<Result.Success<*>>(songRepo.getSongGames("8"))
        assertIs<Result.Success<*>>(songRepo.rateSong("8", 4.5))
        assertIs<Result.Success<*>>(songRepo.deleteSongRating("8"))
        assertIs<Result.Success<*>>(songRepo.getSongReviews("8"))

        val epRepo = EpisodeRepositoryImpl(client)
        assertIs<Result.Success<*>>(epRepo.getEpisodes(listOf("7")))
        assertIs<Result.Success<*>>(epRepo.getEpisode("7", listOf("show", "videos")))
        assertIs<Result.Success<*>>(epRepo.getEpisodeSuggestions("7"))
        assertIs<Result.Success<*>>(epRepo.updateEpisodeWatchStatus("7"))
        assertIs<Result.Success<*>>(epRepo.clearEpisodeWatchStatus("7"))
        assertIs<Result.Success<*>>(epRepo.rateEpisode("7", 4.5, "Great ep", ReviewRecommendation.recommended, isSpoiler = true))
        assertIs<Result.Success<*>>(epRepo.deleteEpisodeRating("7"))
        assertIs<Result.Success<*>>(epRepo.getEpisodeReviews("7"))

        val epRate = requests.first { it.path == "/v1/episodes/7/rate" && it.method == "POST" }
        assertEquals(listOf("4.5"), epRate.formParameters["rating"])
        assertEquals(listOf("Great ep"), epRate.formParameters["description"])
        assertEquals(listOf("2"), epRate.formParameters["recommendation"])
        assertEquals(listOf("1"), epRate.formParameters["isSpoiler"])
        assertTrue(requests.any { it.path == "/v1/episodes/7/watched" && it.method == "DELETE" })
        assertTrue(requests.any { it.path == "/v1/episodes/7/rate" && it.method == "DELETE" })
    }

    // =========================================================================
    // 12-26. Cast, Season, Explore, Feed, Review, ParentalGuide, Genre, Theme,
    //        ThemeStore, Image, Legal, Misc, Schedule, Search, Store
    // =========================================================================

    @Test
    fun `Cast, Season, Explore, Feed, Review, and ParentalGuide repositories match v1_15 contracts`() = runTest {
        val requests = mutableListOf<RecordedRequest>()
        val client = createClient(requests) { req ->
            when {
                req.url.encodedPath == "/v1/seasons/12/watched" ->
                    respondJson("""{"data":{"isWatched":true}}""")
                req.url.encodedPath.endsWith("/heart") ->
                    respondJson("""{"data":{"isHearted":true}}""")
                req.url.encodedPath.endsWith("/pin") ->
                    respondJson("""{"data":{"isPinned":true}}""")
                req.url.encodedPath == "/v1/feed/messages/55/update" ->
                    respondJson("""{"data":{"content":"edited","isNSFW":false,"isSpoiler":true}}""")
                req.url.encodedPath == "/v1/reviews/77/vote" ->
                    respondJson("""{"data":{"isHelpful":true}}""")
                req.url.encodedPath == "/v1/reviews/categories" ->
                    respondJson("""{"data":[{"id":"1","type":"rating-categories","attributes":{"slug":"story","name":"Story","weight":1.0,"displayOrder":1}}]}""")
                req.url.encodedPath.endsWith("/parentalguide") && req.method.value == "GET" ->
                    respondJson("""{"data":{"stats":{"categories":[]},"entries":[]}}""")
                req.url.encodedPath == "/v1/parentalguide/entries/88/vote" ->
                    respondJson("""{"data":{"isHelpful":true}}""")
                else ->
                    respondJson("""{"data":[],"next":null}""")
            }
        }
        val castRepo = CastRepositoryImpl(client)
        assertIs<Result.Success<*>>(castRepo.getCast("1"))
        assertIs<Result.Success<*>>(castRepo.getShowCastDetails("1"))
        assertIs<Result.Success<*>>(castRepo.getShowCastByIds(listOf("1", "2")))
        assertIs<Result.Success<*>>(castRepo.getLiteratureCastDetails("3"))
        assertIs<Result.Success<*>>(castRepo.getLiteratureCastByIds(listOf("3")))
        assertIs<Result.Success<*>>(castRepo.getGameCastDetails("4"))
        assertIs<Result.Success<*>>(castRepo.getGameCastByIds(listOf("4")))
        assertIs<Result.Success<*>>(castRepo.getShowCast("10"))
        assertIs<Result.Success<*>>(castRepo.getLiteratureCast("20"))
        assertIs<Result.Success<*>>(castRepo.getGameCast("30"))

        val seasonRepo = SeasonRepositoryImpl(client)
        assertIs<Result.Success<*>>(seasonRepo.getSeasons(listOf("12", "13")))
        assertIs<Result.Success<*>>(seasonRepo.getDetails("12"))
        assertIs<Result.Success<*>>(seasonRepo.getEpisodes("12", hideFillers = true))
        assertIs<Result.Success<*>>(seasonRepo.updateWatchStatus("12"))

        val exploreRepo = ExploreRepositoryImpl(client)
        assertIs<Result.Success<*>>(exploreRepo.getExplore(genreId = "1", themeId = "2"))
        assertIs<Result.Success<*>>(exploreRepo.getExplore("trending", limit = 10))

        val chartsRepo = ChartsRepositoryImpl(client)
        assertIs<Result.Success<*>>(chartsRepo.getChartsAnime())
        assertIs<Result.Success<*>>(chartsRepo.getChartsManga())
        assertIs<Result.Success<*>>(chartsRepo.getChartsGames())
        assertIs<Result.Success<*>>(chartsRepo.getChartsEpisodes())
        assertIs<Result.Success<*>>(chartsRepo.getChartsSongs())
        assertIs<Result.Success<*>>(chartsRepo.getChartsCharacters())
        assertIs<Result.Success<*>>(chartsRepo.getChartsPeople())
        assertIs<Result.Success<*>>(chartsRepo.getChartsStudios())

        val feedRepo = FeedRepositoryImpl(client)
        assertIs<Result.Success<*>>(feedRepo.getExploreFeed())
        assertIs<Result.Success<*>>(feedRepo.getHomeFeed())
        assertIs<Result.Success<*>>(feedRepo.postFeedMessage(FeedMessageRequest(content = "Hello!")))
        assertIs<Result.Success<*>>(feedRepo.getFeedMessage("55"))
        assertIs<Result.Success<*>>(feedRepo.getFeedMessageReplies("55"))
        assertIs<Result.Success<*>>(feedRepo.getFeedMessageQuotes("55"))
        assertIs<Result.Success<*>>(feedRepo.getFeedMessageReShares("55"))
        assertIs<Result.Success<*>>(feedRepo.heartFeedMessage("55"))
        assertIs<Result.Success<*>>(feedRepo.pinFeedMessage("55"))
        assertIs<Result.Success<*>>(feedRepo.updateFeedMessage("55", FeedMessageRequest(content = "edited", isSpoiler = true)))
        assertIs<Result.Success<*>>(feedRepo.reportFeedMessage("55", ReportReason.spam))
        assertIs<Result.Success<*>>(feedRepo.deleteFeedMessage("55"))

        val reviewRepo = ReviewRepositoryImpl(client)
        assertIs<Result.Success<*>>(reviewRepo.getReview("77"))
        assertIs<Result.Success<*>>(reviewRepo.getReviewRevisions("77"))
        assertIs<Result.Success<*>>(reviewRepo.getRatingCategories(ReviewKind.anime, modelId = "1"))
        val voteRes = assertIs<Result.Success<*>>(reviewRepo.voteReview("77", isHelpful = true))
        assertNotNull(voteRes.data)
        assertIs<Result.Success<*>>(reviewRepo.reportReview("77", ReportReason.spoiler, "Spoils ending"))
        assertIs<Result.Success<*>>(reviewRepo.elevateReview("77"))
        assertIs<Result.Success<*>>(reviewRepo.deleteReview("77"))

        val pgRepo = ParentalGuideRepositoryImpl(client)
        val pgRequest = ParentalGuideEntryRequest(category = 0, rating = 1, frequency = 1, isSpoiler = false, reason = "Mild")
        assertIs<Result.Success<*>>(pgRepo.getShowParentalGuide("1"))
        assertIs<Result.Success<*>>(pgRepo.getLiteratureParentalGuide("2"))
        assertIs<Result.Success<*>>(pgRepo.getGameParentalGuide("3"))
        assertIs<Result.Success<*>>(pgRepo.submitShowEntry("1", pgRequest))
        assertIs<Result.Success<*>>(pgRepo.submitLiteratureEntry("2", pgRequest))
        assertIs<Result.Success<*>>(pgRepo.submitGameEntry("3", pgRequest))
        assertIs<Result.Success<*>>(pgRepo.updateEntry("88", pgRequest))
        assertIs<Result.Success<*>>(pgRepo.voteEntry("88", true))
        assertIs<Result.Success<*>>(pgRepo.reportEntry("88", ParentalGuideReportReason.spam))
        assertIs<Result.Success<*>>(pgRepo.deleteEntry("88"))

        val reportReviewReq = requests.first { it.path == "/v1/reviews/77/report" }
        assertEquals(listOf("spoiler"), reportReviewReq.formParameters["reason_key"])
        assertEquals(listOf("Spoils ending"), reportReviewReq.formParameters["details"])

        val feedReportReq = requests.first { it.path == "/v1/feed/messages/55/report" }
        assertEquals(listOf("spam"), feedReportReq.formParameters["reason_key"])
    }

    @Test
    fun `Genre, Theme, ThemeStore, Image, Legal, Misc, Schedule, Search, and Store repositories match contracts`() = runTest {
        val requests = mutableListOf<RecordedRequest>()
        val client = createClient(requests) { req ->
            when (req.url.encodedPath) {
                "/v1/legal/privacy-policy", "/v1/legal/terms-of-use" ->
                    respondJson("""{"data":{"type":"legal","href":"/v1/legal/privacy-policy","attributes":{"text":"Policy"}}}""")
                "/v1/info" ->
                    respondJson("""{"meta":{"version":"1.15.0","minimumAppVersion":"1.0.0","isMaintenanceModeEnabled":false,"isUserAuthenticated":false}}""")
                "/v1/settings" ->
                    respondJson("""{"data":{"appleMusicDeveloperToken":"dev_tok","appleMusicDeveloperTokenExpiresAt":1800000000,"youtubeAPIKey":"yt_key"}}""")
                "/v1/theme-store/4/download" ->
                    respondBytes(byteArrayOf(1, 2, 3, 4))
                "/v1/search" ->
                    respondJson("""{"data":{}}""")
                "/v1/search/suggestions" ->
                    respondJson("""{"data":["Naruto"]}""")
                "/v1/store/verify", "/v1/store/restore-order" ->
                    respondJson("""{"data":[{"type":"receipts","attributes":{"isValid":true,"needsRefresh":false}}]}""")
                "/v1/store/transactions" ->
                    respondJson("""{"data":[{"id":"tx1","type":"store-transactions","attributes":{"transactionID":"tx1","productType":1,"productID":"pro_monthly"}}]}""")
                else ->
                    respondJson("""{"data":[]}""")
            }
        }

        val genreRepo = GenreRepositoryImpl(client)
        assertIs<Result.Success<*>>(genreRepo.getGenres())
        assertIs<Result.Success<*>>(genreRepo.getGenres(listOf("1", "2")))
        assertIs<Result.Success<*>>(genreRepo.getGenre("1"))

        val themeRepo = ThemeRepositoryImpl(client)
        assertIs<Result.Success<*>>(themeRepo.getThemes())
        assertIs<Result.Success<*>>(themeRepo.getThemes(listOf("3", "4")))
        assertIs<Result.Success<*>>(themeRepo.getTheme("3"))

        val themeStoreRepo = ThemeStoreRepositoryImpl(client)
        assertIs<Result.Success<*>>(themeStoreRepo.getThemeStore())
        assertIs<Result.Success<*>>(themeStoreRepo.getThemeStoreItem("4"))
        val themeBytes = assertIs<Result.Success<ByteArray>>(themeStoreRepo.downloadTheme("4"))
        assertContentEquals(byteArrayOf(1, 2, 3, 4), themeBytes.data)

        val imageRepo = ImageRepositoryImpl(client)
        assertIs<Result.Success<*>>(imageRepo.getRandomImages(MediaKind.shows, MediaCollection.poster, limit = 3))

        val legalRepo = LegalRepositoryImpl(client)
        assertIs<Result.Success<*>>(legalRepo.getPrivacyPolicy())
        assertIs<Result.Success<*>>(legalRepo.getTermsOfUse())

        val miscRepo = MiscRepositoryImpl(client)
        assertIs<Result.Success<*>>(miscRepo.getAppInfo())
        val settingsRes = assertIs<Result.Success<*>>(miscRepo.getSettings())
        assertNotNull(settingsRes.data)
        assertIs<Result.Success<*>>(miscRepo.getPlayers())
        assertIs<Result.Success<*>>(miscRepo.getProviders())
        assertIs<Result.Success<*>>(miscRepo.getLanguages())
        assertIs<Result.Success<*>>(miscRepo.getLanguage("en"))

        val scheduleRepo = ScheduleRepositoryImpl(client)
        assertIs<Result.Success<*>>(scheduleRepo.getSchedule(KKScheduleType.shows, "2025-11-21"))

        val searchRepo = SearchRepositoryImpl(client)
        assertIs<Result.Success<*>>(searchRepo.search(KKSearchScope.kurozora, listOf(KKSearchType.shows), "naruto"))
        assertIs<Result.Success<*>>(searchRepo.getSearchSuggestions(KKSearchScope.kurozora, listOf(KKSearchType.shows), "nar"))

        val storeRepo = StoreRepositoryImpl(client)
        assertIs<Result.Success<*>>(storeRepo.verifyReceipt("YmFzZTY0", password = "abcd"))
        assertIs<Result.Success<*>>(storeRepo.restoreOrder("ORD-123"))
        assertIs<Result.Success<*>>(storeRepo.getTransactions())

        val verifyReq = requests.first { it.path == "/v1/store/verify" }
        assertEquals(listOf("YmFzZTY0"), verifyReq.formParameters["receipt"])
        assertEquals(listOf("abcd"), verifyReq.formParameters["password"])

        val restoreReq = requests.first { it.path == "/v1/store/restore-order" }
        assertEquals(listOf("ORD-123"), restoreReq.formParameters["orderID"])

        val schedReq = requests.first { it.path == "/v1/schedule" }
        assertEquals("2025-11-21", schedReq.queryParameters["date"])
        assertEquals("0", schedReq.queryParameters["type"])

        val randImgReq = requests.first { it.path == "/v1/images/random" }
        assertEquals("3", randImgReq.queryParameters["limit"])
        assertEquals("poster", randImgReq.queryParameters["collection"])
        assertEquals("shows", randImgReq.queryParameters["type"])
        assertNull(randImgReq.textBody)
    }
}
