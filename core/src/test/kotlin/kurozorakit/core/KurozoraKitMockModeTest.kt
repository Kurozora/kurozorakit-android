package kurozorakit.core

import kotlinx.coroutines.test.runTest
import kurozorakit.api.AccountUser
import kurozorakit.api.TokenProvider
import kurozorakit.core.mock.MockRouteResponse
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
import kurozorakit.data.enums.WeeklyDigestSectionType
import kurozorakit.data.models.feed.message.FeedMessageRequest
import kurozorakit.data.models.misc.SignInResult
import kurozorakit.data.models.parentalguide.request.ParentalGuideEntryRequest
import kurozorakit.data.models.user.update.UserUpdate
import kurozorakit.shared.KurozoraError
import kurozorakit.shared.Result
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KurozoraKitMockModeTest {

    @Test
    fun `KurozoraKit mock works out-of-the-box across all 26 repositories`() = runTest {
        val kit = KurozoraKit.mock()
        assertNotNull(kit.mockState)

        // Show
        val shows = assertIs<Result.Success<*>>(kit.show().getShows()).data
        assertNotNull(shows)
        val show1 = assertIs<Result.Success<kurozorakit.data.models.show.ShowResponse>>(kit.show().getShow("1")).data
        assertEquals("One Piece", show1.data.first().attributes.title)
        assertIs<Result.Success<*>>(kit.show().getShowCast("1"))
        assertIs<Result.Success<*>>(kit.show().getShowStaff("1"))
        assertIs<Result.Success<*>>(kit.show().getShowCharacters("1"))
        assertIs<Result.Success<*>>(kit.show().getShowVideos("1"))
        assertIs<Result.Success<*>>(kit.show().getShowLanguages("1"))
        assertIs<Result.Success<*>>(kit.show().getShowEditorial("1"))
        assertIs<Result.Success<*>>(kit.show().getShowMapping(malId = 21))
        assertIs<Result.Success<*>>(kit.show().getShowSongs("1"))
        assertIs<Result.Success<*>>(kit.show().getShowStudios("1"))
        assertIs<Result.Success<*>>(kit.show().getShowSeasons("1"))
        assertIs<Result.Success<*>>(kit.show().getUpcomingShows())
        assertIs<Result.Success<*>>(kit.show().getShowTrailers())
        assertIs<Result.Success<*>>(kit.show().getShowMuseum())
        assertIs<Result.Success<*>>(kit.show().getShowMuseumYear(2024))

        // Charts
        assertIs<Result.Success<*>>(kit.charts().getChartsAnime())
        assertIs<Result.Success<*>>(kit.charts().getChartsManga())
        assertIs<Result.Success<*>>(kit.charts().getChartsGames())
        assertIs<Result.Success<*>>(kit.charts().getChartsEpisodes())
        assertIs<Result.Success<*>>(kit.charts().getChartsSongs())
        assertIs<Result.Success<*>>(kit.charts().getChartsCharacters())
        assertIs<Result.Success<*>>(kit.charts().getChartsPeople())
        assertIs<Result.Success<*>>(kit.charts().getChartsStudios())

        // Literature, Game, Character, Person, Studio, Song
        assertEquals("Berserk", assertIs<Result.Success<kurozorakit.data.models.literature.LiteratureResponse>>(kit.literature().getLiterature("101")).data.data.first().attributes.title)
        assertIs<Result.Success<*>>(kit.literature().getLiteratureGallery("101"))
        assertIs<Result.Success<*>>(kit.literature().getLiteratureLanguages("101"))
        assertIs<Result.Success<*>>(kit.literature().getLiteratureEditorial("101"))
        assertIs<Result.Success<*>>(kit.literature().getAdaptedLiteratures())

        assertEquals("Persona 5 Royal", assertIs<Result.Success<kurozorakit.data.models.game.GameResponse>>(kit.game().getGame("201")).data.data.first().attributes.title)
        assertIs<Result.Success<*>>(kit.game().getGameCast("201"))
        assertIs<Result.Success<*>>(kit.game().getGameSongs("201"))
        assertIs<Result.Success<*>>(kit.game().getAdaptedGames())

        assertEquals("Monkey D. Luffy", assertIs<Result.Success<kurozorakit.data.models.character.CharacterResponse>>(kit.character().getCharacter("301")).data.data.first().attributes.name)
        assertIs<Result.Success<*>>(kit.character().getCharacterGallery("301"))

        assertEquals("Mayumi Tanaka", assertIs<Result.Success<kurozorakit.data.models.person.PersonResponse>>(kit.people().getPerson("401")).data.data.first().attributes.fullName)
        assertIs<Result.Success<*>>(kit.people().getPersonRelationships("401"))

        assertEquals("Toei Animation", assertIs<Result.Success<kurozorakit.data.models.studio.StudioResponse>>(kit.studio().getStudio("501")).data.data.first().attributes.name)
        assertIs<Result.Success<*>>(kit.studio().getStudioPredecessors("501"))
        assertIs<Result.Success<*>>(kit.studio().getStudioSuccessors("501"))

        assertEquals("We Are!", assertIs<Result.Success<kurozorakit.data.models.song.SongResponse>>(kit.song().getSong("601")).data.data.first().attributes.title)
        assertIs<Result.Success<*>>(kit.song().getSongLyrics("601"))

        // Cast, Season, Episode, Explore, AirSeason, Schedule, Image
        assertIs<Result.Success<*>>(kit.cast().getShowCast("1"))
        assertIs<Result.Success<*>>(kit.cast().getShowCastDetails("701"))
        assertIs<Result.Success<*>>(kit.season().getSeasons(listOf("10")))
        assertIs<Result.Success<*>>(kit.season().getDetails("10"))
        assertIs<Result.Success<*>>(kit.season().getEpisodes("10"))
        assertIs<Result.Success<*>>(kit.episode().getEpisode("100"))
        assertIs<Result.Success<*>>(kit.explore().getExplore())
        assertIs<Result.Success<*>>(kit.explore().getExplore("exp-shows"))
        assertIs<Result.Success<*>>(kit.airSeason().getAnimeSeason("2025", SeasonOfYear.WINTER))
        assertIs<Result.Success<*>>(kit.schedule().getSchedule(KKScheduleType.shows, "2025-01-16"))
        assertIs<Result.Success<*>>(kit.image().getRandomImages(MediaKind.shows, MediaCollection.poster, 2))

        // Genre, Theme, ThemeStore, Legal, Misc, Store, Review, ParentalGuide
        assertIs<Result.Success<*>>(kit.genre().getGenres())
        assertIs<Result.Success<*>>(kit.theme().getThemes())
        assertIs<Result.Success<*>>(kit.themeStore().getThemeStore())
        val themeBytes = assertIs<Result.Success<ByteArray>>(kit.themeStore().downloadTheme("1")).data
        assertTrue(themeBytes.decodeToString().contains("KUROZORA_MOCK_THEME_1"))
        assertIs<Result.Success<*>>(kit.legal().getPrivacyPolicy())
        assertIs<Result.Success<*>>(kit.legal().getTermsOfUse())
        assertIs<Result.Success<*>>(kit.misc().getAppInfo())
        assertIs<Result.Success<*>>(kit.misc().getSettings())
        assertIs<Result.Success<*>>(kit.misc().getPlayers())
        assertIs<Result.Success<*>>(kit.misc().getProviders())
        assertIs<Result.Success<*>>(kit.misc().getLanguages())
        assertIs<Result.Success<*>>(kit.kurozoraStore().getTransactions())
        assertIs<Result.Success<*>>(kit.kurozoraStore().verifyReceipt("YmFzZTY0"))
        assertIs<Result.Success<*>>(kit.review().getRatingCategories(ReviewKind.anime))
        assertIs<Result.Success<*>>(kit.parentalGuide().getShowParentalGuide("1"))

        // User & Auth
        assertIs<Result.Success<*>>(kit.user().getMyProfile())
        assertIs<Result.Success<*>>(kit.user().getMyDigest())
        assertIs<Result.Success<*>>(kit.user().getMyDigestSection(WeeklyDigestSectionType.upNext))
        assertIs<Result.Success<*>>(kit.user().getMyRecaps())
        assertIs<Result.Success<*>>(kit.user().getMyRecapDetails("2025", "0"))
        assertIs<Result.Success<*>>(kit.user().getMyRecapStats("2025", "0"))
        assertIs<Result.Success<*>>(kit.user().getMyNotifications())
        assertIs<Result.Success<*>>(kit.user().getMySessions())
        assertIs<Result.Success<*>>(kit.user().getAccessTokens())
        assertIs<Result.Success<*>>(kit.auth().getUserProfile("1002"))
        assertIs<Result.Success<*>>(kit.auth().getUsers(listOf("1002")))
        assertIs<Result.Success<*>>(kit.user().getUsers(listOf("1002")))
    }

    @Test
    fun `stateful library, favorites, reminders, episodes, reviews, and feed mutations persist in mockState`() = runTest {
        val kit = KurozoraKit.mock()

        // 1. Library mutation + Show reflection + Sync
        assertIs<Result.Success<*>>(kit.user().addToLibrary(KKLibrary.Kind.SHOWS, KKLibrary.Status.PLANNING, listOf("3")))
        val planning = assertIs<Result.Success<kurozorakit.data.models.library.LibraryResponse>>(
            kit.user().getMyLibrary(KKLibrary.Kind.SHOWS, KKLibrary.Status.PLANNING)
        ).data
        assertTrue(planning.data.shows?.any { it.id == "3" } == true)

        assertIs<Result.Success<*>>(kit.user().updateMyLibrary(KKLibrary.Kind.SHOWS, listOf("3"), rewatchCount = 4, isHidden = true))
        val show3 = assertIs<Result.Success<kurozorakit.data.models.show.ShowResponse>>(kit.show().getShow("3")).data.data.first()
        assertEquals(4, show3.attributes.library?.rewatchCount)
        assertEquals(true, show3.attributes.library?.isHidden)

        val sync = assertIs<Result.Success<kurozorakit.data.models.library.LibrarySyncResponse>>(kit.user().syncMyLibrary()).data
        assertTrue(sync.data.relationships.entries.any { it.trackableID == "3" && it.rewatchCount == 4 })

        assertIs<Result.Success<*>>(kit.user().removeFromMyLibrary(KKLibrary.Kind.SHOWS, listOf("3")))
        val planningAfter = assertIs<Result.Success<kurozorakit.data.models.library.LibraryResponse>>(
            kit.user().getMyLibrary(KKLibrary.Kind.SHOWS, KKLibrary.Status.PLANNING)
        ).data
        assertFalse(planningAfter.data.shows?.any { it.id == "3" } == true)

        // 2. Favorites & Reminders (.ics export)
        assertIs<Result.Success<*>>(kit.user().updateMyFavorites(KKLibrary.Kind.SHOWS, listOf("2"), isFavorited = true))
        val favs = assertIs<Result.Success<kurozorakit.data.models.favorite.library.FavoriteLibraryResponse>>(
            kit.user().getMyFavorites(KKLibrary.Kind.SHOWS)
        ).data
        assertTrue(favs.data.shows?.any { it.id == "2" } == true)

        assertIs<Result.Success<*>>(kit.user().updateReminderStatus(KKLibrary.Kind.SHOWS, listOf("2"), isReminded = true))
        val ics = assertIs<Result.Success<ByteArray>>(kit.user().downloadMyReminders()).data.decodeToString()
        assertTrue(ics.contains("BEGIN:VCALENDAR"))
        assertTrue(ics.contains("(0:2)"))

        // 3. Episode watched state
        assertIs<Result.Success<*>>(kit.episode().updateEpisodeWatchStatus("101"))
        val watched = assertIs<Result.Success<kurozorakit.data.models.episode.EpisodeIdentityResponse>>(kit.user().getMyWatchedEpisodes()).data
        assertTrue(watched.data.any { it.id == "101" })
        assertIs<Result.Success<*>>(kit.episode().clearEpisodeWatchStatus("101"))
        val watchedAfter = assertIs<Result.Success<kurozorakit.data.models.episode.EpisodeIdentityResponse>>(kit.user().getMyWatchedEpisodes()).data
        assertFalse(watchedAfter.data.any { it.id == "101" })

        // 4. Reviews & voting
        val initialReviews = assertIs<Result.Success<kurozorakit.data.models.review.ReviewResponse>>(kit.show().getShowReviews("2")).data.data.size
        assertIs<Result.Success<*>>(kit.show().rateShow("2", 5.0, "Emotional masterpiece!", ReviewRecommendation.recommended, false))
        val afterReviews = assertIs<Result.Success<kurozorakit.data.models.review.ReviewResponse>>(kit.show().getShowReviews("2")).data.data
        assertEquals(initialReviews + 1, afterReviews.size)
        val createdReviewId = afterReviews.first().id
        val voteRes = assertIs<Result.Success<kurozorakit.data.models.review.ReviewVoteResponse>>(kit.review().voteReview(createdReviewId, isHelpful = true)).data
        assertEquals(true, voteRes.data.isHelpful)
        assertIs<Result.Success<*>>(kit.review().deleteReview(createdReviewId))

        // 5. Feed timeline mutations
        val posted = assertIs<Result.Success<kurozorakit.data.models.feed.message.FeedMessageResponse>>(
            kit.feed().postFeedMessage(FeedMessageRequest(content = "Hello from mock simulator!"))
        ).data.data.first()
        assertEquals("Hello from mock simulator!", posted.attributes.content)
        assertIs<Result.Success<*>>(kit.feed().heartFeedMessage(posted.id))
        assertIs<Result.Success<*>>(kit.feed().pinFeedMessage(posted.id))
        assertIs<Result.Success<*>>(kit.feed().updateFeedMessage(posted.id, FeedMessageRequest(content = "Updated mock post!")))
        assertIs<Result.Success<*>>(kit.feed().reportFeedMessage(posted.id, ReportReason.spam))
        assertIs<Result.Success<*>>(kit.feed().deleteFeedMessage(posted.id))

        // 6. Settings, Profile, Notifications, Timeout Appeal, Parental Guide
        val updatedSettings = assertIs<Result.Success<kurozorakit.data.models.user.settings.UserSettingResponse>>(
            kit.user().updateMySettings(scrobbleThreshold = 95, ratingStyle = RatingStyle.detailed)
        ).data.data.first()
        assertEquals(95, updatedSettings.attributes.scrobbleThreshold)
        assertEquals(RatingStyle.detailed, updatedSettings.attributes.ratingStyle)

        assertIs<Result.Success<*>>(kit.user().updateMyProfile(UserUpdate(nickname = "MockHero")))
        assertIs<Result.Success<*>>(kit.user().updateMyNotification("4001", ReadStatus.read))
        val appeal = assertIs<Result.Success<kurozorakit.data.models.user.timeout.TimeoutResponse>>(
            kit.user().submitTimeoutAppeal("Please lift my mock timeout")
        ).data.data.first()
        assertEquals("Please lift my mock timeout", appeal.relationships?.appeal?.data?.first()?.attributes?.message)

        val pgEntry = assertIs<Result.Success<kurozorakit.data.models.parentalguide.ParentalGuideEntryResponse>>(
            kit.parentalGuide().submitShowEntry("1", ParentalGuideEntryRequest(category = 1, rating = 2, frequency = 1, isSpoiler = false, reason = "Mock PG"))
        ).data.data.first()
        assertIs<Result.Success<*>>(kit.parentalGuide().reportEntry(pgEntry.id, ParentalGuideReportReason.inaccurate))
        assertIs<Result.Success<*>>(kit.parentalGuide().deleteEntry(pgEntry.id))

        // 7. Search filtering & reset
        val search = assertIs<Result.Success<kurozorakit.data.models.search.SearchResponse>>(
            kit.search().search(KKSearchScope.kurozora, listOf(KKSearchType.shows), "frieren")
        ).data
        assertEquals(listOf("2"), search.data.shows?.data?.map { it.id })

        val suggestions = assertIs<Result.Success<kurozorakit.data.models.search.SearchSuggesitonsResponse>>(
            kit.search().getSearchSuggestions(KKSearchScope.kurozora, listOf(KKSearchType.shows), "one")
        ).data
        assertTrue(suggestions.data.any { it.contains("One Piece", ignoreCase = true) })

        kit.mockState!!.reset()
        assertTrue(kit.mockState!!.recordedRequests.isEmpty())
    }

    @Test
    fun `mockMode supports auth gate, 2FA sign-in flow, status overrides, and custom route handlers`() = runTest {
        var expiredTriggered = false
        var savedToken: String? = null
        val inMemoryTokenProvider = object : TokenProvider {
            override suspend fun saveToken(user: AccountUser) { savedToken = user.token }
            override suspend fun getToken(): String? = savedToken
        }

        val kit = KurozoraKit.Builder()
            .consoleLogging(false)
            .tokenProvider(inMemoryTokenProvider)
            .onSessionExpired { expiredTriggered = true }
            .mockMode {
                requireAuthForMeEndpoints = true
                requireTwoFactorOnSignIn = true
                overrideStatus("anime/999", 404)
                onRoute("legal/terms-of-use") { _, _ ->
                    MockRouteResponse(
                        statusCode = 200,
                        body = """{"data":{"type":"legal","href":"/v1/legal/terms-of-use","attributes":{"text":"Custom Mock ToS"}}}""",
                    )
                }
            }
            .build()

        // Unauthenticated /me fails with 401 and triggers onSessionExpired
        val unauth = kit.user().getMyProfile()
        assertIs<KurozoraError.AuthenticationError>(assertIs<Result.Error>(unauth).error)
        assertTrue(expiredTriggered)

        // Sign-in returns 2FA challenge -> complete 2FA -> /me now succeeds
        val signIn = assertIs<Result.Success<SignInResult>>(kit.auth().signIn("mock@kurozora.app", "pw")).data
        val challenge = assertIs<SignInResult.RequiresTwoFactor>(signIn)
        assertIs<Result.Success<*>>(kit.auth().submitTwoFactorChallenge(challenge.challengeToken, otp = "123456"))
        assertNotNull(savedToken)
        assertIs<Result.Success<*>>(kit.user().getMyProfile())

        // Status override returns 404 NotFoundError
        val missing = kit.show().getShow("999")
        assertIs<KurozoraError.NotFoundError>(assertIs<Result.Error>(missing).error)

        // Custom route override returns custom body
        val customTos = assertIs<Result.Success<kurozorakit.data.models.legal.LegalResponse>>(kit.legal().getTermsOfUse()).data
        assertEquals("Custom Mock ToS", customTos.data.attributes.text)
    }
}
