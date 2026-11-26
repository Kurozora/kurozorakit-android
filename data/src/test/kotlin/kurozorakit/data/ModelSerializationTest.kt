package kurozorakit.data

import kotlinx.serialization.json.Json
import kurozorakit.data.enums.DiscordActivityName
import kurozorakit.data.enums.DiscordPresenceImage
import kurozorakit.data.enums.EpisodeFillerKind
import kurozorakit.data.enums.ExploreCategoryType
import kurozorakit.data.enums.FavoriteKind
import kurozorakit.data.enums.KKLibrary
import kurozorakit.data.enums.LanguageSupportType
import kurozorakit.data.enums.ParentalGuideCategory
import kurozorakit.data.enums.ParentalGuideDepiction
import kurozorakit.data.enums.ParentalGuideFrequency
import kurozorakit.data.enums.ParentalGuideRating
import kurozorakit.data.enums.RatingStyle
import kurozorakit.data.enums.RecapStatType
import kurozorakit.data.enums.ReportReason
import kurozorakit.data.enums.ReviewKind
import kurozorakit.data.enums.ReviewRecommendation
import kurozorakit.data.enums.SongType
import kurozorakit.data.enums.StoreProductType
import kurozorakit.data.enums.TimeoutReason
import kurozorakit.data.enums.UserNotificationType
import kurozorakit.data.enums.UserRole
import kurozorakit.data.enums.WeeklyDigestSectionType
import kurozorakit.data.models.character.Character
import kurozorakit.data.models.digest.WeeklyDigestSectionResponse
import kurozorakit.data.models.episode.Episode
import kurozorakit.data.models.game.Game
import kurozorakit.data.models.library.LibraryUpdateResponse
import kurozorakit.data.models.literature.Literature
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaStat
import kurozorakit.data.models.parentalguide.ParentalGuideEntry
import kurozorakit.data.models.person.Person
import kurozorakit.data.models.review.Review
import kurozorakit.data.models.show.Show
import kurozorakit.data.models.song.lyric.SongLyric
import kurozorakit.data.models.song.lyric.SongLyricResponse
import kurozorakit.data.models.store.StoreTransactionResponse
import kurozorakit.data.models.studio.Studio
import kurozorakit.data.models.user.User
import kurozorakit.data.models.user.notification.UserNotification
import kurozorakit.data.models.user.settings.UserSettingResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ModelSerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun `MediaStat deserializes v1_15_0 sentiment and favorite fields`() {
        val raw = """
            {
              "ratingCountList": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
              "ratingAverage": 4.4,
              "ratingCount": 55,
              "rankGlobal": 12,
              "rankTotal": 25000,
              "sentiment": "Rated Very Positive",
              "positivePercentage": 88.5,
              "highestRatingPercentage": 88.5,
              "favoriteSentiment": "Fan Favorite",
              "favoriteCount": 140,
              "favoriteShare": 0.32,
              "sentimentGap": 0.14
            }
        """.trimIndent()

        val stat = json.decodeFromString<MediaStat>(raw)
        assertEquals(88.5, stat.positivePercentage)
        @Suppress("DEPRECATION")
        assertEquals(88.5, stat.highestRatingPercentage)
        assertEquals("Fan Favorite", stat.favoriteSentiment)
        assertEquals(140, stat.favoriteCount)
        assertEquals(0.32, stat.favoriteShare)
        assertEquals(0.14, stat.sentimentGap)
    }

    @Test
    fun `Media deserializes focalX and focalY coordinates`() {
        val raw = """
            {
              "url": "https://cdn.kurozora.app/poster.jpg",
              "height": 900,
              "width": 600,
              "focalX": 0.48,
              "focalY": 0.22
            }
        """.trimIndent()

        val media = json.decodeFromString<Media>(raw)
        assertEquals(0.48, media.focalX)
        assertEquals(0.22, media.focalY)
    }

    @Test
    fun `Episode deserializes fillerKind, tvRating, videos, and premiere flags`() {
        val raw = """
            {
              "id": "ep-1",
              "type": "episodes",
              "href": "/v1/episodes/ep-1",
              "attributes": {
                "number": 1,
                "numberTotal": 1,
                "seasonNumber": 1,
                "showTitle": "Frieren",
                "title": "The Journey's End",
                "tvRating": { "name": "PG-13", "description": "Teens 13 or older" },
                "duration": "28m",
                "videos": [
                  {
                    "id": "99",
                    "type": "video",
                    "attributes": {
                      "code": "abc123",
                      "source": "App\\Embeds\\YouTubeEmbed",
                      "url": "https://youtu.be/abc123",
                      "isSub": true,
                      "isDub": false,
                      "type": "Trailer",
                      "publishedAt": 1700000000,
                      "viewCount": 1000
                    }
                  }
                ],
                "isFiller": false,
                "fillerKind": 0,
                "isNsfw": false,
                "isPremiere": true,
                "isFinale": false,
                "isSpecial": false,
                "isVerified": true,
                "viewCount": 5000,
                "startedAt": 1695945600,
                "firstAired": 1695945600,
                "endedAt": 1695947280
              }
            }
        """.trimIndent()

        val episode = json.decodeFromString<Episode>(raw)
        assertEquals(EpisodeFillerKind.mangaCanon, episode.attributes.fillerKind)
        assertTrue(episode.attributes.isPremiere)
        assertFalse(episode.attributes.isFinale)
        assertEquals("PG-13", episode.attributes.tvRating?.name)
        assertEquals(1, episode.attributes.videos?.size)
        assertEquals("abc123", episode.attributes.videos?.first()?.attributes?.code)
        assertNotNull(episode.attributes.firstAired)
        assertNotNull(episode.attributes.endedAt)
    }

    @Test
    fun `Character deserializes bust, waist, hip and nicknames`() {
        val raw = """
            {
              "id": "10",
              "type": "characters",
              "href": "/v1/characters/10",
              "attributes": {
                "slug": "frieren",
                "name": "Frieren",
                "nicknames": ["Frieren the Slayer"],
                "bust": 78.5,
                "waist": 56.0,
                "hip": 81.0
              }
            }
        """.trimIndent()

        val character = json.decodeFromString<Character>(raw)
        assertEquals(listOf("Frieren the Slayer"), character.attributes.nicknames)
        assertEquals(78.5, character.attributes.bustSize)
        assertEquals(56.0, character.attributes.waistSize)
        assertEquals(81.0, character.attributes.hipSize)
    }

    @Test
    fun `Studio deserializes foundedAt and defunctAt timestamps`() {
        val raw = """
            {
              "id": "5",
              "type": "studios",
              "href": "/v1/studios/5",
              "attributes": {
                "slug": "madhouse",
                "name": "Madhouse",
                "tvRating": { "name": "NR", "description": "Not Rated" },
                "isNSFW": false,
                "foundedAt": 88041600,
                "defunctAt": null
              }
            }
        """.trimIndent()

        val studio = json.decodeFromString<Studio>(raw)
        assertNotNull(studio.attributes.foundedAtTimestamp)
        assertNull(studio.attributes.defunctAtTimestamp)
    }

    @Test
    fun `ParentalGuideEntry deserializes integer enums and camelCase isSpoiler`() {
        val raw = """
            {
              "id": "42",
              "type": "parental-guide-entries",
              "href": "/v1/parentalguide/entries/42",
              "attributes": {
                "category": 2,
                "rating": 3,
                "frequency": 2,
                "depiction": 3,
                "reason": "Graphic battle scenes",
                "isSpoiler": true,
                "helpfulCount": 10,
                "unhelpfulCount": 1,
                "isHelpful": true,
                "userID": "7",
                "createdAt": 1700000000,
                "updatedAt": 1700003600
              }
            }
        """.trimIndent()

        val entry = json.decodeFromString<ParentalGuideEntry>(raw)
        assertEquals(ParentalGuideCategory.violenceAndGore, entry.attributes.category)
        assertEquals(ParentalGuideRating.severe, entry.attributes.rating)
        assertEquals(ParentalGuideFrequency.occasional, entry.attributes.frequency)
        assertEquals(ParentalGuideDepiction.graphic, entry.attributes.depiction)
        assertTrue(entry.attributes.isSpoiler)
        assertNotNull(entry.attributes.createdAt)
        assertNotNull(entry.attributes.updatedAt)
    }

    @Test
    fun `Review deserializes recommendation, helpfulness, progress, and revisionCount`() {
        val raw = """
            {
              "id": "101",
              "type": "reviews",
              "href": "/v1/reviews/101",
              "attributes": {
                "score": 4.5,
                "description": "Great story",
                "isSpoiler": true,
                "recommendation": 2,
                "progress": 12,
                "progressTotal": 24,
                "isLowEffort": false,
                "isElevated": true,
                "helpfulCount": 15,
                "unhelpfulCount": 2,
                "isHelpful": true,
                "revisionCount": 3,
                "createdAt": 1700000000
              }
            }
        """.trimIndent()

        val review = json.decodeFromString<Review>(raw)
        assertEquals(ReviewRecommendation.recommended, review.attributes.recommendation)
        assertTrue(review.attributes.isSpoiler)
        assertTrue(review.attributes.isElevated)
        assertEquals(12, review.attributes.progress)
        assertEquals(24, review.attributes.progressTotal)
        assertEquals(3, review.attributes.revisionCount)
    }

    @Test
    fun `User deserializes integer UserRole including mod and timeout relationship`() {
        val raw = """
            {
              "id": "1",
              "uuid": "uuid-1",
              "type": "users",
              "href": "/v1/users/1/profile",
              "attributes": {
                "slug": "moduser",
                "username": "ModUser",
                "activityStatus": "Online",
                "isDeveloper": false,
                "isStaff": true,
                "isEarlySupporter": false,
                "isPro": true,
                "isSubscribed": true,
                "isVerified": true,
                "joinedAt": 1600000000,
                "followerCount": 10,
                "followingCount": 5,
                "reputationCount": 100,
                "ratingsCount": 20,
                "role": 3
              },
              "relationships": {
                "timeout": {
                  "data": [
                    {
                      "id": "9",
                      "type": "timeouts",
                      "attributes": {
                        "reasonKey": 1,
                        "reasonLabel": "Spam",
                        "isPermanent": false,
                        "expiresAt": 1800000000,
                        "issuedAt": 1790000000
                      }
                    }
                  ]
                }
              }
            }
        """.trimIndent()

        val user = json.decodeFromString<User>(raw)
        assertEquals(UserRole.mod, user.attributes.role)
        val timeout = user.relationships?.timeout?.data?.firstOrNull()
        assertNotNull(timeout)
        assertEquals(TimeoutReason.spam, timeout.attributes.reasonKey)
    }

    @Test
    fun `UserNotification handles new types, unknown fallback, and episode payload`() {
        val newEpisodesRaw = """
            {
              "id": "n-1",
              "type": "notifications",
              "href": "/v1/me/notifications/n-1",
              "attributes": {
                "type": "NewEpisodes",
                "description": "2 new episodes are out",
                "isRead": false,
                "createdAt": 1700000000,
                "payload": {
                  "animeSlug": "frieren",
                  "animeTitle": "Frieren",
                  "count": 2,
                  "episodeID": "ep-10",
                  "episodes": [
                    { "episodeID": "ep-10", "number": 10 },
                    { "episodeID": "ep-11", "number": 11 }
                  ]
                }
              }
            }
        """.trimIndent()

        val notif = json.decodeFromString<UserNotification>(newEpisodesRaw)
        assertEquals(UserNotificationType.NewEpisodes, notif.attributes.type)
        assertEquals(2, notif.attributes.payload.episodes?.size)

        val unknownTypeRaw = """
            {
              "id": "n-2",
              "type": "notifications",
              "href": "/v1/me/notifications/n-2",
              "attributes": {
                "type": "BrandNewFutureNotificationType",
                "description": "Future alert",
                "isRead": true,
                "createdAt": 1700000000,
                "payload": { "path": "digest" }
              }
            }
        """.trimIndent()

        val unknownNotif = json.decodeFromString<UserNotification>(unknownTypeRaw)
        assertEquals(UserNotificationType.other, unknownNotif.attributes.type)
        assertEquals("digest", unknownNotif.attributes.payload.path)
    }

    @Test
    fun `LibraryUpdateResponse deserializes nested attributes and libraries relationship`() {
        val raw = """
            {
              "data": {
                "attributes": {
                  "status": 0,
                  "isFavorited": false,
                  "isReminded": true,
                  "isHidden": false,
                  "rewatchCount": 1
                },
                "relationships": {
                  "libraries": [
                    {
                      "id": "500",
                      "kind": 0,
                      "trackableID": "123",
                      "status": 0,
                      "rewatchCount": 1,
                      "isHidden": false,
                      "isFavorited": false,
                      "isReminded": true
                    }
                  ]
                }
              }
            }
        """.trimIndent()

        val response = json.decodeFromString<LibraryUpdateResponse>(raw)
        assertEquals(KKLibrary.Status.INPROGRESS, response.data.status)
        assertEquals(1, response.data.rewatchCount)
        assertEquals(1, response.data.relationships?.libraries?.size)
        assertEquals("123", response.data.relationships?.libraries?.first()?.trackableID)
    }

    @Test
    fun `Show and Game deserialize languageSupport and publishedAt`() {
        val showRaw = """
            {
              "id": "1",
              "type": "shows",
              "href": "/v1/anime/1",
              "attributes": {
                "slug": "test",
                "title": "Test",
                "languageSupport": {
                  "name": "Japanese",
                  "code": "ja",
                  "iso6393": "jpn",
                  "types": [1, 2],
                  "count": 4
                },
                "tvRating": { "name": "PG-13", "description": "PG-13" },
                "type": { "name": "TV", "description": "TV" },
                "source": { "name": "Manga", "description": "Manga" },
                "status": { "name": "Currently Airing", "description": "Airing", "color": "#00ff00" },
                "episodeCount": 12,
                "seasonCount": 1,
                "firstAired": 1700000000,
                "duration": "24m",
                "durationCount": 24,
                "durationTotal": "4h",
                "durationTotalCount": 240,
                "isNSFW": false
              }
            }
        """.trimIndent()

        val show = json.decodeFromString<Show>(showRaw)
        assertEquals("ja", show.attributes.languageSupport?.code)
        assertEquals(
            listOf(LanguageSupportType.audio, LanguageSupportType.subtitles),
            show.attributes.languageSupport?.types
        )
        assertEquals(4, show.attributes.languageSupport?.count)
        assertNotNull(show.attributes.firstAired)

        val gameRaw = """
            {
              "id": "2",
              "type": "games",
              "href": "/v1/games/2",
              "attributes": {
                "slug": "persona-5",
                "videoUrl": "https://youtu.be/xyz",
                "title": "Persona 5",
                "tvRating": { "name": "M", "description": "Mature" },
                "type": { "name": "Main Game", "description": "Main Game" },
                "source": { "name": "Original", "description": "Original" },
                "status": { "name": "Released", "description": "Released", "color": "#00ff00" },
                "editionCount": 2,
                "publishedAt": 1473897600,
                "duration": "100h",
                "durationCount": 6000,
                "durationTotal": "120h",
                "durationTotalCount": 7200,
                "isNSFW": false
              }
            }
        """.trimIndent()

        val game = json.decodeFromString<Game>(gameRaw)
        assertEquals("https://youtu.be/xyz", game.attributes.videoUrl)
        assertNotNull(game.attributes.publishedAt)
    }

    @Test
    fun `New v1_15_0 resources deserialize cleanly`() {
        val settingsRaw = """
            {
              "data": [
                {
                  "id": "1",
                  "type": "settings",
                  "href": "/v1/me/settings",
                  "attributes": {
                    "scrobbleThreshold": 80,
                    "discordRichPresenceEnabled": true,
                    "discordPresenceImage": 1,
                    "discordActivityName": 0,
                    "ratingStyle": 2
                  }
                }
              ]
            }
        """.trimIndent()

        val settings = json.decodeFromString<UserSettingResponse>(settingsRaw).data.first()
        assertEquals(80, settings.attributes.scrobbleThreshold)
        assertEquals(DiscordPresenceImage.episodeBanner, settings.attributes.discordPresenceImage)
        assertEquals(DiscordActivityName.title, settings.attributes.discordActivityName)
        assertEquals(RatingStyle.detailed, settings.attributes.ratingStyle)

        val txRaw = """
            {
              "data": [
                {
                  "id": "tx-1",
                  "type": "store-transactions",
                  "attributes": {
                    "transactionID": "tx-1",
                    "productType": 3,
                    "productID": "kurozora.plus.monthly",
                    "priceMilliunits": 4990,
                    "currency": "USD",
                    "purchasedAt": 1700000000,
                    "isRefundable": false
                  }
                }
              ]
            }
        """.trimIndent()

        val tx = json.decodeFromString<StoreTransactionResponse>(txRaw).data.first()
        assertEquals(StoreProductType.autoRenewingSubscription, tx.attributes.productType)

        val digestRaw = """
            {
              "data": [
                {
                  "id": "momentum",
                  "type": "digestSection",
                  "href": "",
                  "attributes": {
                    "kind": "momentum",
                    "title": null,
                    "subtitle": null,
                    "position": 5,
                    "momentum": { "hasMomentum": true, "episodes": 12 }
                  }
                }
              ]
            }
        """.trimIndent()

        val digest = json.decodeFromString<WeeklyDigestSectionResponse>(digestRaw).data.first()
        assertEquals("momentum", digest.attributes.kind)
        assertNotNull(digest.attributes.momentum)
    }

    @Test
    fun `User deserializes cleanly when activityStatus and optional booleans or counts are omitted or null`() {
        val tolerantJson = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
            explicitNulls = false
        }
        val missingFieldsRaw = """
            {
              "id": "2",
              "type": "users",
              "href": "/v1/users/2/profile",
              "uuid": "9788aec4-712c-43ec-8848-056381355d17",
              "attributes": {
                "slug": "kirito",
                "username": "Kirito",
                "followerCount": null,
                "isVerified": null,
                "joinedAt": null
              }
            }
        """.trimIndent()

        val user = tolerantJson.decodeFromString<User>(missingFieldsRaw)
        assertEquals("2", user.id)
        assertEquals("Kirito", user.attributes.username)
        assertNull(user.attributes.activityStatus)
        assertEquals(0, user.attributes.followerCount)
        assertFalse(user.attributes.isVerified)
        assertNull(user.attributes.joinedAt)
    }

    @Test
    fun `SongLyric deserializes both object-array agents and legacy string-array agents`() {
        val objectAgentsRaw = """
            {
              "data": [
                {
                  "id": "21189",
                  "type": "lyrics",
                  "href": "/v1/songs/21189/lyrics",
                  "attributes": {
                    "source": "ttml",
                    "language": "ja",
                    "timing": "word",
                    "agents": [{"key": "v1", "type": "person"}],
                    "lines": [
                      {
                        "key": "L1",
                        "position": 1,
                        "songPart": "Intro",
                        "agent": "v1",
                        "beginMs": 565,
                        "endMs": 3368,
                        "text": "Dandadan"
                      }
                    ]
                  }
                }
              ]
            }
        """.trimIndent()

        val objResp = json.decodeFromString<SongLyricResponse>(objectAgentsRaw)
        assertEquals(1, objResp.data.size)
        assertEquals(listOf(SongLyric.Agent(key = "v1", type = "person")), objResp.data.first().attributes.agents)

        val stringAgentsRaw = """
            {
              "data": [
                {
                  "id": "21189",
                  "type": "lyrics",
                  "attributes": {
                    "agents": ["v1", "v2"],
                    "lines": [{"text": "Hello"}]
                  }
                }
              ]
            }
        """.trimIndent()

        val strResp = json.decodeFromString<SongLyricResponse>(stringAgentsRaw)
        assertEquals(
            listOf(SongLyric.Agent(key = "v1", type = null), SongLyric.Agent(key = "v2", type = null)),
            strResp.data.first().attributes.agents,
        )
    }

    @Test
    fun `Person and Studio deserialize websiteURLs and socialURLs from both arrays and maps`() {
        val personMapRaw = """
            {
              "id": "10",
              "type": "people",
              "href": "/v1/people/10",
              "attributes": {
                "slug": "oda",
                "fullName": "Eiichiro Oda",
                "websiteURLs": {"0": "https://example.com/a", "1": "https://example.com/b"}
              }
            }
        """.trimIndent()
        val personArrayRaw = """
            {
              "id": "11",
              "type": "people",
              "href": "/v1/people/11",
              "attributes": {
                "slug": "kubo",
                "fullName": "Tite Kubo",
                "websiteURLs": ["https://example.com/c"]
              }
            }
        """.trimIndent()

        val personFromMap = json.decodeFromString<Person>(personMapRaw)
        val personFromArray = json.decodeFromString<Person>(personArrayRaw)
        assertEquals(listOf("https://example.com/a", "https://example.com/b"), personFromMap.attributes.websiteURLs)
        assertEquals(listOf("https://example.com/c"), personFromArray.attributes.websiteURLs)

        val studioMapRaw = """
            {
              "id": "20",
              "type": "studios",
              "href": "/v1/studios/20",
              "attributes": {
                "slug": "toei",
                "name": "Toei Animation",
                "websiteURLs": {"0": "https://toei.example"},
                "socialURLs": {"twitter": "https://x.com/toei", "yt": "https://youtube.com/toei"}
              }
            }
        """.trimIndent()
        val studioArrayRaw = """
            {
              "id": "21",
              "type": "studios",
              "href": "/v1/studios/21",
              "attributes": {
                "slug": "mappa",
                "name": "MAPPA",
                "websiteURLs": ["https://mappa.example"],
                "socialURLs": ["https://x.com/mappa"]
              }
            }
        """.trimIndent()

        val studioFromMap = json.decodeFromString<Studio>(studioMapRaw)
        val studioFromArray = json.decodeFromString<Studio>(studioArrayRaw)
        assertEquals(listOf("https://toei.example"), studioFromMap.attributes.websiteURLs)
        assertEquals(listOf("https://x.com/toei", "https://youtube.com/toei"), studioFromMap.attributes.socialURLs)
        assertEquals(listOf("https://mappa.example"), studioFromArray.attributes.websiteURLs)
        assertEquals(listOf("https://x.com/mappa"), studioFromArray.attributes.socialURLs)
    }

    @Test
    fun `All custom enum serializers fall back cleanly on unknown backend values`() {
        assertEquals(DiscordActivityName.title, json.decodeFromString<DiscordActivityName>("999"))
        assertEquals(DiscordPresenceImage.poster, json.decodeFromString<DiscordPresenceImage>("999"))
        assertEquals(EpisodeFillerKind.mangaCanon, json.decodeFromString<EpisodeFillerKind>("999"))
        assertEquals(ExploreCategoryType.shows, json.decodeFromString<ExploreCategoryType>("\"future-explore-type\""))
        assertEquals(FavoriteKind.anime, json.decodeFromString<FavoriteKind>("999"))
        assertEquals(LanguageSupportType.audio, json.decodeFromString<LanguageSupportType>("999"))
        assertEquals(ParentalGuideCategory.sexAndNudity, json.decodeFromString<ParentalGuideCategory>("999"))
        assertEquals(ParentalGuideDepiction.implied, json.decodeFromString<ParentalGuideDepiction>("999"))
        assertEquals(ParentalGuideFrequency.brief, json.decodeFromString<ParentalGuideFrequency>("999"))
        assertEquals(ParentalGuideRating.none, json.decodeFromString<ParentalGuideRating>("999"))
        assertEquals(RatingStyle.standard, json.decodeFromString<RatingStyle>("999"))
        assertEquals(RecapStatType.busiestWeekday, json.decodeFromString<RecapStatType>("999"))
        assertEquals(ReportReason.other, json.decodeFromString<ReportReason>("\"future_reason\""))
        assertEquals(ReviewKind.anime, json.decodeFromString<ReviewKind>("999"))
        assertEquals(ReviewRecommendation.mixedFeelings, json.decodeFromString<ReviewRecommendation>("999"))
        assertEquals(SongType.background, json.decodeFromString<SongType>("999"))
        assertEquals(StoreProductType.consumable, json.decodeFromString<StoreProductType>("999"))
        assertEquals(TimeoutReason.other, json.decodeFromString<TimeoutReason>("999"))
        assertEquals(UserNotificationType.other, json.decodeFromString<UserNotificationType>("\"FutureNotification\""))
        assertEquals(UserRole.editor, json.decodeFromString<UserRole>("999"))
        assertEquals(WeeklyDigestSectionType.drops, json.decodeFromString<WeeklyDigestSectionType>("\"future-digest\""))
    }

    @Test
    fun `Literature and Game relationships deserialize full CharacterResponse and StaffResponse`() {
        val litRaw = """
            {
              "id": "1",
              "type": "literatures",
              "href": "/v1/manga/1",
              "attributes": { "slug": "one-piece", "title": "One Piece" },
              "relationships": {
                "characters": {
                  "data": [
                    { "id": "10", "type": "characters", "href": "/v1/characters/10", "attributes": { "slug": "luffy", "name": "Luffy" } }
                  ]
                },
                "staff": {
                  "data": [
                    {
                      "id": "20",
                      "type": "staff",
                      "href": "/v1/staff/20",
                      "attributes": { "role": { "name": "Author", "description": "Story & Art" } },
                      "relationships": {
                        "person": {
                          "data": [{ "id": "30", "type": "people", "href": "/v1/people/30", "attributes": { "slug": "oda", "fullName": "Eiichiro Oda" } }]
                        }
                      }
                    }
                  ]
                }
              }
            }
        """.trimIndent()

        val lit = json.decodeFromString<Literature>(litRaw)
        assertEquals("Luffy", lit.relationships?.characters?.data?.first()?.attributes?.name)
        assertEquals("Author", lit.relationships?.staff?.data?.first()?.attributes?.role?.name)
        assertEquals("Eiichiro Oda", lit.relationships?.staff?.data?.first()?.relationships?.person?.data?.first()?.attributes?.fullName)

        val gameRaw = litRaw.replace("\"literatures\"", "\"games\"").replace("/v1/manga/1", "/v1/games/1")
        val game = json.decodeFromString<Game>(gameRaw)
        assertEquals("Luffy", game.relationships?.characters?.data?.first()?.attributes?.name)
        assertEquals("Author", game.relationships?.staff?.data?.first()?.attributes?.role?.name)
    }
}
