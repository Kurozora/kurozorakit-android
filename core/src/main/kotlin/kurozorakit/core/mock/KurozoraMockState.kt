package kurozorakit.core.mock

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kurozorakit.data.enums.DiscordActivityName
import kurozorakit.data.enums.DiscordPresenceImage
import kurozorakit.data.enums.EpisodeFillerKind
import kurozorakit.data.enums.KKLibrary
import kurozorakit.data.enums.LanguageSupportType
import kurozorakit.data.enums.ParentalGuideCategory
import kurozorakit.data.enums.ParentalGuideDepiction
import kurozorakit.data.enums.ParentalGuideFrequency
import kurozorakit.data.enums.ParentalGuideRating
import kurozorakit.data.enums.RatingStyle
import kurozorakit.data.enums.RecapStatType
import kurozorakit.data.enums.ReviewRecommendation
import kurozorakit.data.enums.SongType
import kurozorakit.data.enums.StoreProductType
import kurozorakit.data.enums.TimeoutReason
import kurozorakit.data.enums.UserNotificationType
import kurozorakit.data.models.airseason.AirSeason
import kurozorakit.data.models.character.Character
import kurozorakit.data.models.digest.WeeklyDigestSection
import kurozorakit.data.models.editorial.Editorial
import kurozorakit.data.models.episode.Episode
import kurozorakit.data.models.episode.EpisodeIdentity
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.explore.ExploreCategory
import kurozorakit.data.models.feed.message.FeedMessage
import kurozorakit.data.models.game.Game
import kurozorakit.data.models.game.GameIdentity
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.genre.Genre
import kurozorakit.data.models.language.Language
import kurozorakit.data.models.language.MediaLanguage
import kurozorakit.data.models.legal.Legal
import kurozorakit.data.models.library.LibraryAttributes
import kurozorakit.data.models.literature.Literature
import kurozorakit.data.models.literature.LiteratureIdentity
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.game.GameResponse
import kurozorakit.data.models.literature.LiteratureResponse
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaRelation
import kurozorakit.data.models.media.MediaStat
import kurozorakit.data.models.show.ShowResponse
import kurozorakit.data.models.misc.Settings
import kurozorakit.data.models.parentalguide.ParentalGuideEntry
import kurozorakit.data.models.person.Person
import kurozorakit.data.models.person.PersonRelationship
import kurozorakit.data.models.person.PersonResponse
import kurozorakit.data.models.player.Player
import kurozorakit.data.models.provider.Provider
import kurozorakit.data.models.recap.Recap
import kurozorakit.data.models.recap.item.RecapItem
import kurozorakit.data.models.recap.stat.RecapStat
import kurozorakit.data.models.review.MediaRatingRevision
import kurozorakit.data.models.review.RatingCategory
import kurozorakit.data.models.review.Review
import kurozorakit.data.models.schedule.Schedule
import kurozorakit.data.models.season.Season
import kurozorakit.data.models.show.Show
import kurozorakit.data.models.show.ShowIdentity
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.show.attributes.AdaptationSource
import kurozorakit.data.models.show.attributes.AiringStatus
import kurozorakit.data.models.show.attributes.MediaType
import kurozorakit.data.models.show.attributes.TVRating
import kurozorakit.data.models.show.cast.Cast
import kurozorakit.data.models.show.cast.attributes.CastRole
import kurozorakit.data.models.show.mapping.AnimeMapping
import kurozorakit.data.models.show.song.ShowSong
import kurozorakit.data.models.song.Song
import kurozorakit.data.models.song.lyric.SongLyric
import kurozorakit.data.models.staff.Staff
import kurozorakit.data.models.staff.StaffRole
import kurozorakit.data.models.store.StoreTransaction
import kurozorakit.data.models.studio.Studio
import kurozorakit.data.models.theme.Theme
import kurozorakit.data.models.theme.app.AppTheme
import kurozorakit.data.models.user.User
import kurozorakit.data.models.user.UserResponse
import kurozorakit.data.models.user.achievement.Achievement
import kurozorakit.data.models.user.notification.UserNotification
import kurozorakit.data.models.user.session.Session
import kurozorakit.data.models.user.session.location.Location
import kurozorakit.data.models.user.session.location.LocationResponse
import kurozorakit.data.models.user.session.platform.Platform
import kurozorakit.data.models.user.session.platform.PlatformResponse
import kurozorakit.data.models.user.settings.UserSetting
import kurozorakit.data.models.user.timeout.Timeout
import kurozorakit.data.models.user.timeout.TimeoutAppeal
import kurozorakit.data.models.user.timeout.TimeoutAppealResponse
import kurozorakit.data.models.user.tokens.AccessToken
import kurozorakit.data.models.video.Video
import kurozorakit.data.models.character.CharacterResponse

/**
 * Mutable per-item library entry tracked inside [KurozoraMockState].
 */
data class MockLibraryEntry(
    val kind: Int,
    val modelId: String,
    var status: KKLibrary.Status,
    var rewatchCount: Int = 0,
    var isHidden: Boolean = false,
)

/**
 * In-memory stateful database backing [KurozoraMockEngine].
 *
 * Pre-seeded with realistic Anime, Manga, Games, Characters, People, Studios, Songs,
 * Episodes, Seasons, Explore Categories, Reviews, Feed Messages, Parental Guide entries,
 * Notifications, Sessions, Settings, and Catalogs so every single repository in `KurozoraKit`
 * works end-to-end offline with real stateful mutations.
 *
 * Call [reset] in test teardown (`@BeforeTest` / `@AfterTest`) to restore the clean initial seed.
 */
class KurozoraMockState {
    private val lock = Any()

    // --- Read-mostly seeded catalogs (mutable so tests can add/inspect items freely) ---
    val shows: MutableList<Show> = mutableListOf()
    val literatures: MutableList<Literature> = mutableListOf()
    val games: MutableList<Game> = mutableListOf()
    val characters: MutableList<Character> = mutableListOf()
    val people: MutableList<Person> = mutableListOf()
    val studios: MutableList<Studio> = mutableListOf()
    val songs: MutableList<Song> = mutableListOf()
    val seasons: MutableList<Season> = mutableListOf()
    val episodes: MutableList<Episode> = mutableListOf()
    val genres: MutableList<Genre> = mutableListOf()
    val themes: MutableList<Theme> = mutableListOf()
    val appThemes: MutableList<AppTheme> = mutableListOf()
    val users: MutableList<User> = mutableListOf()
    val players: MutableList<Player> = mutableListOf()
    val providers: MutableList<Provider> = mutableListOf()
    val languages: MutableList<Language> = mutableListOf()
    val exploreCategories: MutableList<ExploreCategory> = mutableListOf()
    val achievements: MutableList<Achievement> = mutableListOf()
    val recaps: MutableList<Recap> = mutableListOf()
    val recapItems: MutableList<RecapItem> = mutableListOf()
    val recapStats: MutableList<RecapStat> = mutableListOf()
    val storeTransactions: MutableList<StoreTransaction> = mutableListOf()

    // --- Mutable user-session & interaction state ---
    var currentUserId: String = "1001"
    var activeAuthToken: String = "mock-token-1001|kurozora-mock-secret"
    var pendingTwoFactorToken: String = "mock-2fa-challenge-token"

    val library: MutableMap<Pair<Int, String>, MockLibraryEntry> = linkedMapOf()
    val favorites: MutableSet<Pair<Int, String>> = linkedSetOf()
    val reminders: MutableSet<Pair<Int, String>> = linkedSetOf()
    val watchedEpisodeIds: MutableSet<String> = linkedSetOf()
    val followingUserIds: MutableSet<String> = linkedSetOf()
    val blockedUserIds: MutableSet<String> = linkedSetOf()
    val reviews: MutableList<Review> = mutableListOf()
    val reviewVotes: MutableMap<String, Boolean?> = linkedMapOf()
    val feedMessages: MutableList<FeedMessage> = mutableListOf()
    val heartedFeedIds: MutableSet<String> = linkedSetOf()
    val parentalGuideEntries: MutableList<ParentalGuideEntry> = mutableListOf()
    val parentalGuideVotes: MutableMap<String, Boolean?> = linkedMapOf()
    val notifications: MutableList<UserNotification> = mutableListOf()
    val notificationReadOverrides: MutableMap<String, Boolean> = linkedMapOf()
    val accessTokens: MutableList<AccessToken> = mutableListOf()
    val sessions: MutableList<Session> = mutableListOf()
    var userSettings: UserSetting = defaultUserSetting()
    var activeTimeoutAppealMessage: String? = null
    val recordedRequests: MutableList<MockRequestContext> = mutableListOf()

    private var idSequence: Int = 5000

    init {
        reset()
    }

    /** Generates a unique monotonic mock entity ID. */
    fun nextId(): String = synchronized(lock) { (++idSequence).toString() }

    /** Executes [block] under the internal state synchronization lock. */
    fun <T> withLock(block: KurozoraMockState.() -> T): T = synchronized(lock) { block() }

    /** Resets all collections and mutable user state back to the initial realistic seed. */
    fun reset() = synchronized(lock) {
        idSequence = 5000
        shows.clear(); literatures.clear(); games.clear(); characters.clear(); people.clear()
        studios.clear(); songs.clear(); seasons.clear(); episodes.clear(); genres.clear()
        themes.clear(); appThemes.clear(); users.clear(); players.clear(); providers.clear()
        languages.clear(); exploreCategories.clear(); achievements.clear(); recaps.clear()
        recapItems.clear(); recapStats.clear(); storeTransactions.clear()

        library.clear(); favorites.clear(); reminders.clear(); watchedEpisodeIds.clear()
        followingUserIds.clear(); blockedUserIds.clear(); reviews.clear(); reviewVotes.clear()
        feedMessages.clear(); heartedFeedIds.clear(); parentalGuideEntries.clear()
        parentalGuideVotes.clear(); notifications.clear(); notificationReadOverrides.clear()
        accessTokens.clear(); sessions.clear(); recordedRequests.clear()

        currentUserId = "1001"
        activeAuthToken = "mock-token-1001|kurozora-mock-secret"
        activeTimeoutAppealMessage = null
        userSettings = defaultUserSetting()

        seedCatalog()
        seedUserState()
    }

    fun currentUser(): User = users.first { it.id == currentUserId }

    fun showWithUserLibrary(show: Show): Show {
        val libEntry = library[KKLibrary.Kind.SHOWS.value to show.id]
        val isFav = (KKLibrary.Kind.SHOWS.value to show.id) in favorites
        val isRem = (KKLibrary.Kind.SHOWS.value to show.id) in reminders
        val updatedLib = LibraryAttributes(
            status = libEntry?.status,
            isFavorited = isFav,
            isReminded = isRem,
            isHidden = libEntry?.isHidden,
            rewatchCount = libEntry?.rewatchCount,
        )
        return show.copy(attributes = show.attributes.copy(library = updatedLib))
    }

    fun literatureWithUserLibrary(lit: Literature): Literature {
        val libEntry = library[KKLibrary.Kind.LITERATURES.value to lit.id]
        val isFav = (KKLibrary.Kind.LITERATURES.value to lit.id) in favorites
        val isRem = (KKLibrary.Kind.LITERATURES.value to lit.id) in reminders
        val updatedLib = LibraryAttributes(
            status = libEntry?.status,
            isFavorited = isFav,
            isReminded = isRem,
            isHidden = libEntry?.isHidden,
            rewatchCount = libEntry?.rewatchCount,
        )
        return lit.copy(attributes = lit.attributes.copy(library = updatedLib))
    }

    fun gameWithUserLibrary(game: Game): Game {
        val libEntry = library[KKLibrary.Kind.GAMES.value to game.id]
        val isFav = (KKLibrary.Kind.GAMES.value to game.id) in favorites
        val isRem = (KKLibrary.Kind.GAMES.value to game.id) in reminders
        val updatedLib = LibraryAttributes(
            status = libEntry?.status,
            isFavorited = isFav,
            isReminded = isRem,
            isHidden = libEntry?.isHidden,
            rewatchCount = libEntry?.rewatchCount,
        )
        return game.copy(attributes = game.attributes.copy(library = updatedLib))
    }

    fun buildCastList(): List<Cast> {
        val char1 = characters.first()
        val person1 = people.first()
        return listOf(
            Cast(
                id = "701",
                type = "show-cast",
                href = "/v1/show-cast/701",
                attributes = Cast.Attributes(
                    role = CastRole(name = "Main", description = "Main character"),
                    language = "Japanese",
                ),
                relationships = Cast.Relationships(
                    people = PersonResponse(data = listOf(person1), next = null),
                    characters = CharacterResponse(data = listOf(char1), next = null),
                ),
            )
        )
    }

    fun buildStaffList(): List<Staff> {
        val person1 = people.first()
        return listOf(
            Staff(
                id = "751",
                type = "staff",
                href = "/v1/staff/751",
                attributes = Staff.Attributes(
                    role = StaffRole(name = "Director", description = "Series Director")
                ),
                relationships = Staff.Relationships(
                    person = PersonResponse(data = listOf(person1), next = null)
                ),
            )
        )
    }

    fun buildShowSongs(): List<ShowSong> {
        val song = songs.first()
        return listOf(
            ShowSong(
                id = "801",
                type = "show-songs",
                href = "/v1/show-songs/801",
                song = song,
                attributes = ShowSong.Attributes(
                    type = SongType.opening,
                    position = 1,
                    episodes = "1-47",
                ),
            )
        )
    }

    fun buildVideos(showId: String): List<Video> = listOf(
        Video(
            id = "v-$showId",
            type = "video",
            attributes = Video.Attributes(
                code = "dQw4w9WgXcQ",
                source = "YouTube",
                url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                isSub = true,
                isDub = false,
                type = "PV",
                publishedAtTimestamp = 1700000000L,
                viewCount = 12500,
            ),
        )
    )

    fun buildMediaLanguages(): List<MediaLanguage> = listOf(
        MediaLanguage(
            name = "Japanese",
            code = "ja",
            iso6393 = "jpn",
            type = LanguageSupportType.audio,
            isOriginal = true,
        ),
        MediaLanguage(
            name = "English",
            code = "en",
            iso6393 = "eng",
            type = LanguageSupportType.subtitles,
            isOriginal = false,
        ),
    )

    fun buildEditorial(modelId: String): List<Editorial> = listOf(
        Editorial(
            id = "ed-$modelId",
            type = "editorials",
            attributes = Editorial.Attributes(
                body = "Mock editorial highlight for item $modelId.",
                byline = "Kurozora Editorial",
                publishedAtTimestamp = 1700000000L,
            ),
        )
    )

    fun buildAnimeMappings(): List<AnimeMapping> = listOf(
        AnimeMapping(slug = "one-piece", myanimelist = 21, anilist = 21, kitsu = 12, anidb = 69, thetvdb = 81797, imdb = "tt0388629"),
        AnimeMapping(slug = "frieren", myanimelist = 52991, anilist = 154587, kitsu = 46474),
    )

    fun buildAirSeasons(): List<AirSeason> = listOf(
        AirSeason(
            type = "browse-seasons",
            attributes = AirSeason.Attributes(type = MediaType(name = "TV", description = "Television series")),
            relationships = AirSeason.Relationships(
                shows = ShowResponse(shows.map { showWithUserLibrary(it) }, next = null),
                literatures = LiteratureResponse(literatures.map { literatureWithUserLibrary(it) }, next = null),
                games = GameResponse(games.map { gameWithUserLibrary(it) }, next = null),
            ),
        )
    )

    fun buildSchedules(): List<Schedule> = listOf(
        Schedule(
            type = "schedules",
            attributes = Schedule.Attributes(date = LocalDate(2025, 1, 16)),
            relationships = Schedule.Relationships(
                shows = ShowResponse(shows.take(2).map { showWithUserLibrary(it) }, next = null),
                literatures = LiteratureResponse(literatures.take(1).map { literatureWithUserLibrary(it) }, next = null),
                games = GameResponse(games.take(1).map { gameWithUserLibrary(it) }, next = null),
            ),
        )
    )

    fun buildWeeklyDigest(): List<WeeklyDigestSection> = listOf(
        WeeklyDigestSection(
            id = "up-next",
            type = "weekly-digest-sections",
            href = "/v1/me/digest/up-next",
            attributes = WeeklyDigestSection.Attributes(
                kind = "up-next",
                title = "Up Next",
                subtitle = "Continue where you left off",
                position = 0,
            ),
            relationships = WeeklyDigestSection.Relationships(
                episodes = EpisodeIdentityResponse(
                    data = episodes.filter { it.id !in watchedEpisodeIds }.map { EpisodeIdentity(it.id, it.type, it.href) },
                    next = null,
                )
            ),
        ),
        WeeklyDigestSection(
            id = "trending",
            type = "weekly-digest-sections",
            href = "/v1/me/digest/trending",
            attributes = WeeklyDigestSection.Attributes(
                kind = "trending",
                title = "Trending This Week",
                subtitle = "Popular shows across Kurozora",
                position = 1,
            ),
            relationships = WeeklyDigestSection.Relationships(
                shows = ShowIdentityResponse(shows.take(3).map { ShowIdentity(it.id, it.type, it.href) }, next = null)
            ),
        ),
    )

    fun buildTimeout(): Timeout = Timeout(
        id = "to-1",
        type = "timeouts",
        attributes = Timeout.Attributes(
            reasonKey = TimeoutReason.spam,
            reasonLabel = "Spam",
            note = "Simulated mock timeout",
            isPermanent = false,
            expiresAtTimestamp = 1893456000L,
            issuedAtTimestamp = 1700000000L,
        ),
        relationships = activeTimeoutAppealMessage?.let { msg ->
            Timeout.Relationships(
                appeal = TimeoutAppealResponse(
                    data = listOf(
                        TimeoutAppeal(
                            id = "app-1",
                            type = "timeout-appeals",
                            attributes = TimeoutAppeal.Attributes(
                                message = msg,
                                createdAtTimestamp = 1700001000L,
                                updatedAtTimestamp = 1700002000L,
                            ),
                        )
                    )
                )
            )
        },
    )

    fun buildSongLyric(songId: String): SongLyric = SongLyric(
        id = "lyr-$songId",
        type = "song-lyrics",
        attributes = SongLyric.Attributes(
            source = "Kurozora",
            language = "ja",
            timing = "line",
            lines = listOf(
                SongLyric.Line(position = 0, beginMs = 0, endMs = 4000, text = "Arittake no yume wo kakiatsume"),
                SongLyric.Line(position = 1, beginMs = 4000, endMs = 8000, text = "Sagashi mono wo sagashi ni yuku no sa"),
            ),
        ),
    )

    fun buildPersonRelationships(): List<PersonRelationship> = listOf(
        PersonRelationship(
            person = people.last(),
            attributes = PersonRelationship.Attributes(
                relation = MediaRelation(name = "Colleague", description = "Co-starred together"),
            ),
        )
    )

    fun buildRatingCategories(): List<RatingCategory> = listOf(
        RatingCategory(
            id = "rc-1",
            type = "rating-categories",
            href = "/v1/reviews/categories",
            attributes = RatingCategory.Attributes(slug = "story", name = "Story", weight = 0.35, displayOrder = 1),
        ),
        RatingCategory(
            id = "rc-2",
            type = "rating-categories",
            href = "/v1/reviews/categories",
            attributes = RatingCategory.Attributes(slug = "animation", name = "Animation", weight = 0.35, displayOrder = 2),
        ),
        RatingCategory(
            id = "rc-3",
            type = "rating-categories",
            href = "/v1/reviews/categories",
            attributes = RatingCategory.Attributes(slug = "sound", name = "Sound", weight = 0.30, displayOrder = 3),
        ),
    )

    fun buildReviewRevisions(reviewId: String): List<MediaRatingRevision> = listOf(
        MediaRatingRevision(
            id = "rev-$reviewId-1",
            type = "media-rating-revisions",
            attributes = MediaRatingRevision.Attributes(
                score = 4.5,
                description = "Initial review text.",
                isSpoiler = false,
                recommendation = ReviewRecommendation.recommended,
                writtenAtTimestamp = 1700000000L,
            ),
        )
    )

    fun buildAppSettings(): Settings = Settings(
        appleMusicDeveloperToken = "mock-apple-music-dev-token",
        appleMusicDeveloperTokenExpiresAt = 1893456000L,
        youtubeAPIKey = "mock-youtube-api-key",
    )

    fun buildLegal(title: String): Legal = Legal(
        type = "legal",
        href = "/v1/legal/${title.lowercase().replace(" ", "-")}",
        attributes = Legal.Attributes(text = "Kurozora Mock $title — simulated legal content."),
    )

    private fun defaultUserSetting(): UserSetting = UserSetting(
        id = "us-1001",
        type = "user-settings",
        href = "/v1/me/settings",
        attributes = UserSetting.Attributes(
            scrobbleThreshold = 85,
            discordRichPresenceEnabled = true,
            discordPresenceImage = DiscordPresenceImage.poster,
            discordActivityName = DiscordActivityName.title,
            ratingStyle = RatingStyle.standard,
        ),
    )

    private fun media(url: String, bg: String = "#1B263B") = Media(
        url = url,
        width = 1000,
        height = 1500,
        backgroundColor = bg,
        textColor1 = "#FFFFFF",
    )

    private fun seedCatalog() {
        val tv = MediaType("TV", "Television anime series")
        val mangaType = MediaType("Manga", "Japanese comic series")
        val gameType = MediaType("RPG", "Role-playing video game")
        val pg13 = TVRating("PG-13", "Teens 13 or older")
        val r17 = TVRating("R-17+", "Violence & profanity")
        val mangaSrc = AdaptationSource("Manga", "Adapted from manga")
        val origSrc = AdaptationSource("Original", "Original work")
        val airing = AiringStatus("Currently Airing", "Airing weekly", "#34C759")
        val finished = AiringStatus("Finished Airing", "Completed broadcast", "#007AFF")

        shows += listOf(
            Show(
                id = "1", type = "shows", href = "/v1/anime/1",
                attributes = Show.Attributes(
                    slug = "one-piece", title = "One Piece", originalTitle = "ワンピース",
                    synopsis = "Monkey D. Luffy sets sail with his crew to find the legendary treasure One Piece.",
                    tagline = "Set sail for the Grand Line!",
                    poster = media("https://cdn.kurozora.app/mock/shows/1/poster.jpg", "#0D3B66"),
                    banner = media("https://cdn.kurozora.app/mock/shows/1/banner.jpg", "#0D3B66"),
                    genres = listOf("Action", "Adventure", "Fantasy"),
                    themes = listOf("Pirates", "Friendship"),
                    studio = "Toei Animation",
                    tvRating = pg13, type = tv, source = mangaSrc, status = airing,
                    stats = MediaStat(ratingCountList = listOf(10, 20, 40, 200, 900), ratingAverage = 4.7, ratingCount = 1170, rankGlobal = 1, rankTotal = 10000, positivePercentage = 95.0, sentiment = "Overwhelmingly Positive"),
                    episodeCount = 1120, seasonCount = 21, duration = "24m", durationCount = 24,
                    durationTotal = "448h", durationTotalCount = 26880, isNSFW = false,
                ),
            ),
            Show(
                id = "2", type = "shows", href = "/v1/anime/2",
                attributes = Show.Attributes(
                    slug = "frieren", title = "Frieren: Beyond Journey's End", originalTitle = "葬送のフリーレン",
                    synopsis = "After a ten-year quest to defeat the Demon King, elven mage Frieren begins a quiet journey to understand humanity.",
                    poster = media("https://cdn.kurozora.app/mock/shows/2/poster.jpg", "#2B2D42"),
                    genres = listOf("Adventure", "Drama", "Fantasy"),
                    studio = "Madhouse",
                    tvRating = pg13, type = tv, source = mangaSrc, status = finished,
                    stats = MediaStat(ratingCountList = listOf(5, 10, 20, 150, 980), ratingAverage = 4.9, ratingCount = 1165, rankGlobal = 2, rankTotal = 10000, positivePercentage = 98.0, sentiment = "Overwhelmingly Positive"),
                    episodeCount = 28, seasonCount = 1, duration = "24m", durationCount = 24,
                    durationTotal = "11h 12m", durationTotalCount = 672, isNSFW = false,
                ),
            ),
            Show(
                id = "3", type = "shows", href = "/v1/anime/3",
                attributes = Show.Attributes(
                    slug = "attack-on-titan", title = "Attack on Titan", originalTitle = "進撃の巨人",
                    synopsis = "Humanity fights for survival behind giant walls against man-eating Titans.",
                    poster = media("https://cdn.kurozora.app/mock/shows/3/poster.jpg", "#3D1E1E"),
                    genres = listOf("Action", "Drama"),
                    studio = "MAPPA",
                    tvRating = r17, type = tv, source = mangaSrc, status = finished,
                    stats = MediaStat(ratingCountList = listOf(15, 25, 50, 250, 860), ratingAverage = 4.6, ratingCount = 1200, rankGlobal = 3, rankTotal = 10000, positivePercentage = 93.0, sentiment = "Very Positive"),
                    episodeCount = 89, seasonCount = 4, duration = "24m", durationCount = 24,
                    durationTotal = "35h 36m", durationTotalCount = 2136, isNSFW = false,
                ),
            ),
            Show(
                id = "4", type = "shows", href = "/v1/anime/4",
                attributes = Show.Attributes(
                    slug = "steins-gate", title = "Steins;Gate", originalTitle = "シュタインズ・ゲート",
                    synopsis = "Self-proclaimed mad scientist Rintaro Okabe accidentally discovers how to send text messages into the past.",
                    poster = media("https://cdn.kurozora.app/mock/shows/4/poster.jpg", "#1E2A38"),
                    genres = listOf("Sci-Fi", "Drama"),
                    themes = listOf("Time Travel", "Psychological"),
                    studio = "White Fox",
                    tvRating = pg13, type = tv, source = origSrc, status = finished,
                    episodeCount = 24, seasonCount = 1, duration = "24m", durationCount = 24,
                    durationTotal = "9h 36m", durationTotalCount = 576, isNSFW = false,
                ),
            ),
            Show(
                id = "5", type = "shows", href = "/v1/anime/5",
                attributes = Show.Attributes(
                    slug = "fmab", title = "Fullmetal Alchemist: Brotherhood", originalTitle = "鋼の錬金術師",
                    synopsis = "Two brothers search for the Philosopher's Stone after a forbidden alchemy ritual goes wrong.",
                    poster = media("https://cdn.kurozora.app/mock/shows/5/poster.jpg", "#4A1C1C"),
                    genres = listOf("Action", "Adventure", "Fantasy"),
                    studio = "Bones",
                    tvRating = r17, type = tv, source = mangaSrc, status = finished,
                    episodeCount = 64, seasonCount = 1, duration = "24m", durationCount = 24,
                    durationTotal = "25h 36m", durationTotalCount = 1536, isNSFW = false,
                ),
            ),
        )

        literatures += listOf(
            Literature(
                id = "101", type = "literatures", href = "/v1/manga/101",
                attributes = Literature.Attributes(
                    slug = "berserk", title = "Berserk", originalTitle = "ベルセルク",
                    synopsis = "Guts, the Black Swordsman, battles fate and demons in a dark medieval world.",
                    poster = media("https://cdn.kurozora.app/mock/manga/101/poster.jpg", "#1C1C1C"),
                    genres = listOf("Action", "Fantasy", "Drama"),
                    tvRating = r17, type = mangaType, source = origSrc, status = airing,
                    volumeCount = 42, chapterCount = 376, pageCount = 8200,
                    duration = "6m", durationCount = 6, durationTotal = "37h", durationTotalCount = 2256, isNSFW = false,
                ),
            ),
            Literature(
                id = "102", type = "literatures", href = "/v1/manga/102",
                attributes = Literature.Attributes(
                    slug = "vagabond", title = "Vagabond", originalTitle = "バガボンド",
                    synopsis = "A retelling of the life of legendary Japanese swordsman Miyamoto Musashi.",
                    poster = media("https://cdn.kurozora.app/mock/manga/102/poster.jpg", "#2C2C2C"),
                    genres = listOf("Action", "Drama"),
                    tvRating = r17, type = mangaType, source = origSrc, status = finished,
                    volumeCount = 37, chapterCount = 327, pageCount = 7200,
                    duration = "6m", durationCount = 6, durationTotal = "32h", durationTotalCount = 1962, isNSFW = false,
                ),
            ),
        )

        games += listOf(
            Game(
                id = "201", type = "games", href = "/v1/games/201",
                attributes = Game.Attributes(
                    slug = "persona-5-royal", title = "Persona 5 Royal",
                    synopsis = "Don the mask of Joker and lead the Phantom Thieves of Hearts across Tokyo.",
                    poster = media("https://cdn.kurozora.app/mock/games/201/poster.jpg", "#8B0000"),
                    genres = listOf("Action", "Fantasy"),
                    tvRating = r17, type = gameType, source = origSrc, status = finished,
                    editionCount = 3, duration = "100h", durationCount = 6000,
                    durationTotal = "120h", durationTotalCount = 7200, isNSFW = false,
                ),
            ),
            Game(
                id = "202", type = "games", href = "/v1/games/202",
                attributes = Game.Attributes(
                    slug = "elden-ring", title = "Elden Ring",
                    synopsis = "Rise, Tarnished, and be guided by grace to brandish the power of the Elden Ring.",
                    poster = media("https://cdn.kurozora.app/mock/games/202/poster.jpg", "#2E261B"),
                    genres = listOf("Action", "Adventure", "Fantasy"),
                    tvRating = r17, type = gameType, source = origSrc, status = finished,
                    editionCount = 2, duration = "80h", durationCount = 4800,
                    durationTotal = "100h", durationTotalCount = 6000, isNSFW = false,
                ),
            ),
        )

        characters += listOf(
            Character(
                id = "301", type = "characters", href = "/v1/characters/301",
                attributes = Character.Attributes(
                    slug = "monkey-d-luffy", name = "Monkey D. Luffy",
                    about = "Captain of the Straw Hat Pirates who ate the Gum-Gum Fruit.",
                    profile = media("https://cdn.kurozora.app/mock/characters/301.jpg"),
                ),
            ),
            Character(
                id = "302", type = "characters", href = "/v1/characters/302",
                attributes = Character.Attributes(
                    slug = "frieren", name = "Frieren",
                    about = "Elven mage who traveled with Himmel's Hero Party.",
                    profile = media("https://cdn.kurozora.app/mock/characters/302.jpg"),
                ),
            ),
            Character(
                id = "303", type = "characters", href = "/v1/characters/303",
                attributes = Character.Attributes(
                    slug = "guts", name = "Guts",
                    about = "The Black Swordsman wielding the massive Dragonslayer.",
                    profile = media("https://cdn.kurozora.app/mock/characters/303.jpg"),
                ),
            ),
        )

        people += listOf(
            Person(
                id = "401", type = "people", href = "/v1/people/401",
                attributes = Person.Attributes(
                    slug = "mayumi-tanaka", fullName = "Mayumi Tanaka",
                    about = "Legendary Japanese voice actress best known as Monkey D. Luffy.",
                    profile = media("https://cdn.kurozora.app/mock/people/401.jpg"),
                ),
            ),
            Person(
                id = "402", type = "people", href = "/v1/people/402",
                attributes = Person.Attributes(
                    slug = "atsumi-tanezaki", fullName = "Atsumi Tanezaki",
                    about = "Acclaimed Japanese voice actress known as Frieren and Anya Forger.",
                    profile = media("https://cdn.kurozora.app/mock/people/402.jpg"),
                ),
            ),
        )

        studios += listOf(
            Studio(
                id = "501", type = "studios", href = "/v1/studios/501",
                attributes = Studio.Attributes(
                    slug = "toei-animation", name = "Toei Animation",
                    about = "One of Japan's oldest and largest animation studios.",
                    tvRating = pg13, isNSFW = false,
                ),
            ),
            Studio(
                id = "502", type = "studios", href = "/v1/studios/502",
                attributes = Studio.Attributes(
                    slug = "madhouse", name = "Madhouse",
                    about = "Renowned Japanese animation studio founded in 1972.",
                    tvRating = pg13, isNSFW = false,
                ),
            ),
            Studio(
                id = "503", type = "studios", href = "/v1/studios/503",
                attributes = Studio.Attributes(
                    slug = "mappa", name = "MAPPA",
                    about = "Japanese animation studio behind Attack on Titan Final Season.",
                    tvRating = r17, isNSFW = false,
                ),
            ),
        )

        songs += listOf(
            Song(
                id = "601", type = "songs", href = "/v1/songs/601",
                attributes = Song.Attributes(title = "We Are!", artist = "Hiroshi Kitadani"),
            ),
            Song(
                id = "602", type = "songs", href = "/v1/songs/602",
                attributes = Song.Attributes(title = "Yuusha", artist = "YOASOBI"),
            ),
        )

        seasons += listOf(
            Season(
                id = "10", type = "seasons", href = "/v1/seasons/10",
                attributes = Season.Attributes(
                    number = 1, title = "East Blue", episodeCount = 3, ratingAverage = 4.6,
                    startedAtTimestamp = 940377600L,
                ),
            ),
            Season(
                id = "11", type = "seasons", href = "/v1/seasons/11",
                attributes = Season.Attributes(
                    number = 1, title = "Season 1", episodeCount = 2, ratingAverage = 4.9,
                    startedAtTimestamp = 1695945600L,
                ),
            ),
        )

        episodes += listOf(
            Episode(
                id = "100", type = "episodes", href = "/v1/episodes/100",
                attributes = Episode.Attributes(
                    number = 1, numberTotal = 1, seasonNumber = 1,
                    title = "I'm Luffy! The Man Who's Gonna Be King of the Pirates!",
                    synopsis = "Luffy emerges from a barrel aboard Alvida's ship and meets Koby.",
                    showTitle = "One Piece", duration = "24m",
                    startedAtTimestamp = 940377600L, viewCount = 500000,
                    isFiller = false, fillerKind = EpisodeFillerKind.mangaCanon,
                    isVerified = true, isPremiere = true,
                ),
            ),
            Episode(
                id = "101", type = "episodes", href = "/v1/episodes/101",
                attributes = Episode.Attributes(
                    number = 2, numberTotal = 2, seasonNumber = 1,
                    title = "Enter the Great Swordsman! Pirate Hunter Roronoa Zoro!",
                    synopsis = "Luffy arrives at Shells Town and recruits Roronoa Zoro.",
                    showTitle = "One Piece", duration = "24m",
                    startedAtTimestamp = 940982400L, viewCount = 460000,
                    isFiller = false, fillerKind = EpisodeFillerKind.mangaCanon, isVerified = true,
                ),
            ),
            Episode(
                id = "102", type = "episodes", href = "/v1/episodes/102",
                attributes = Episode.Attributes(
                    number = 1, numberTotal = 1, seasonNumber = 1,
                    title = "The Journey's End",
                    synopsis = "Himmel's party returns victorious to the capital after ten years.",
                    showTitle = "Frieren: Beyond Journey's End", duration = "28m",
                    startedAtTimestamp = 1695945600L, viewCount = 720000,
                    isFiller = false, fillerKind = EpisodeFillerKind.mangaCanon,
                    isVerified = true, isPremiere = true,
                ),
            ),
        )

        genres += listOf(
            Genre("1", "genres", "/v1/genres/1", Genre.Attributes(slug = "action", name = "Action", description = "Fast-paced action.", color = "#FF3B30", backgroundColor1 = "#FF3B30", backgroundColor2 = "#C70039", textColor1 = "#FFFFFF", textColor2 = "#F2F2F7", isNSFW = false)),
            Genre("2", "genres", "/v1/genres/2", Genre.Attributes(slug = "adventure", name = "Adventure", description = "Epic journeys.", color = "#FF9500", backgroundColor1 = "#FF9500", backgroundColor2 = "#FF5E3A", textColor1 = "#FFFFFF", textColor2 = "#F2F2F7", isNSFW = false)),
            Genre("3", "genres", "/v1/genres/3", Genre.Attributes(slug = "fantasy", name = "Fantasy", description = "Magical worlds.", color = "#5856D6", backgroundColor1 = "#5856D6", backgroundColor2 = "#3634A3", textColor1 = "#FFFFFF", textColor2 = "#F2F2F7", isNSFW = false)),
            Genre("4", "genres", "/v1/genres/4", Genre.Attributes(slug = "sci-fi", name = "Sci-Fi", description = "Futuristic concepts.", color = "#007AFF", backgroundColor1 = "#007AFF", backgroundColor2 = "#0040DD", textColor1 = "#FFFFFF", textColor2 = "#F2F2F7", isNSFW = false)),
        )

        themes += listOf(
            Theme("1", "themes", "/v1/themes/1", Theme.Attributes(slug = "time-travel", name = "Time Travel", description = "Manipulating time.", color = "#0A84FF", backgroundColor1 = "#0A84FF", backgroundColor2 = "#0040DD", textColor1 = "#FFFFFF", textColor2 = "#F2F2F7", symbol = null, isNSFW = false)),
            Theme("2", "themes", "/v1/themes/2", Theme.Attributes(slug = "psychological", name = "Psychological", description = "Mind games.", color = "#AF52DE", backgroundColor1 = "#AF52DE", backgroundColor2 = "#8944AB", textColor1 = "#FFFFFF", textColor2 = "#F2F2F7", symbol = null, isNSFW = false)),
        )

        appThemes += listOf(
            AppTheme("1", "app-themes", "/v1/theme-store/1", AppTheme.Attributes(emptyList(), "Kurozora Dark", "1.0.0", 12400, "https://cdn.kurozora.app/mock/themes/1.ktheme")),
            AppTheme("2", "app-themes", "/v1/theme-store/2", AppTheme.Attributes(emptyList(), "Sakura", "1.2.0", 8300, "https://cdn.kurozora.app/mock/themes/2.ktheme")),
        )

        users += listOf(
            User(
                id = "1001", uuid = "00000000-0000-0000-0000-000000001001",
                type = "users", href = "/v1/users/1001/profile",
                attributes = User.Attributes(
                    slug = "kurozora_mock", username = "Kurozora Mock",
                    biography = "Simulated mock account for offline development and testing.",
                    activityStatus = "Online",
                    isDeveloper = true, isStaff = false, isEarlySupporter = true,
                    isPro = true, isSubscribed = true, isVerified = true,
                    joinedAt = LocalDate(2023, 1, 15),
                    followerCount = 42, followingCount = 12, reputationCount = 150, ratingsCount = 24,
                ),
            ),
            User(
                id = "1002", uuid = "00000000-0000-0000-0000-000000001002",
                type = "users", href = "/v1/users/1002/profile",
                attributes = User.Attributes(
                    slug = "luffy", username = "Monkey D. Luffy",
                    biography = "I'm gonna be King of the Pirates!",
                    activityStatus = "Online",
                    isDeveloper = false, isStaff = false, isEarlySupporter = false,
                    isPro = false, isSubscribed = false, isVerified = true,
                    joinedAt = LocalDate(2022, 5, 5),
                    followerCount = 999, followingCount = 9, reputationCount = 500, ratingsCount = 10,
                ),
            ),
        )

        exploreCategories += listOf(
            ExploreCategory(
                id = "exp-shows", type = "explore", href = "/v1/explore/exp-shows",
                attributes = ExploreCategory.Attributes(
                    slug = "trending-shows", title = "Trending Shows",
                    description = "Most watched shows right now", position = 1,
                    type = "shows", size = "large",
                ),
                relationships = ExploreCategory.Relationships(
                    shows = ShowIdentityResponse(shows.map { ShowIdentity(it.id, it.type, it.href) }, next = null)
                ),
            ),
            ExploreCategory(
                id = "exp-manga", type = "explore", href = "/v1/explore/exp-manga",
                attributes = ExploreCategory.Attributes(
                    slug = "popular-manga", title = "Popular Manga",
                    description = "Top rated literature", position = 2,
                    type = "literatures", size = "medium",
                ),
                relationships = ExploreCategory.Relationships(
                    literatures = LiteratureIdentityResponse(literatures.map { LiteratureIdentity(it.id, it.type, it.href) }, next = null)
                ),
            ),
            ExploreCategory(
                id = "exp-games", type = "explore", href = "/v1/explore/exp-games",
                attributes = ExploreCategory.Attributes(
                    slug = "top-games", title = "Top Games",
                    description = "Critically acclaimed games", position = 3,
                    type = "games", size = "medium",
                ),
                relationships = ExploreCategory.Relationships(
                    games = GameIdentityResponse(games.map { GameIdentity(it.id, it.type, it.href) }, next = null)
                ),
            ),
        )

        players += listOf(
            Player("p-1", "players", attributes = Player.Attributes("mpv", "mpv")),
            Player("p-2", "players", attributes = Player.Attributes("vlc", "VLC Media Player")),
        )

        providers += listOf(
            Provider("pr-1", "providers", attributes = Provider.Attributes("crunchyroll", "Crunchyroll")),
            Provider("pr-2", "providers", attributes = Provider.Attributes("netflix", "Netflix")),
        )

        languages += listOf(
            Language("Japanese", "ja", "jpn"),
            Language("English", "en", "eng"),
            Language("Turkish", "tr", "tur"),
        )

        achievements += listOf(
            Achievement("ach-1", "achievements", Achievement.Attributes("First Steps", "Added your first anime to your library.", achievedAt = Instant.fromEpochSeconds(1700000000L))),
            Achievement("ach-2", "achievements", Achievement.Attributes("Binge Watcher", "Watched 100 episodes.", achievedAt = null)),
        )

        recaps += listOf(
            Recap("rec-2025", "recaps", "/v1/me/recap/2025/0", Recap.Attributes(2025, 0, "#FF9500", "#FF2D55", "Your 2025 Year in Review")),
        )
        recapItems += listOf(
            RecapItem("ri-1", "recap-items", "/v1/me/recap/2025/0", RecapItem.Attributes(2025, 0, "shows", 42, 620, 14880, 5.0)),
        )
        recapStats += listOf(
            RecapStat("rs-1", "recap-stats", RecapStat.Attributes(2025, 0, RecapStatType.longestStreak, 18, 1735689600L)),
        )

        storeTransactions += listOf(
            StoreTransaction("tx-1", "store-transactions", StoreTransaction.Attributes("tx-1", StoreProductType.autoRenewingSubscription, "kurozora.pro.monthly", 4990, "USD", 1700000000L, 1799000000L)),
        )
    }

    private fun seedUserState() {
        library[KKLibrary.Kind.SHOWS.value to "1"] = MockLibraryEntry(KKLibrary.Kind.SHOWS.value, "1", KKLibrary.Status.INPROGRESS, rewatchCount = 1)
        library[KKLibrary.Kind.SHOWS.value to "2"] = MockLibraryEntry(KKLibrary.Kind.SHOWS.value, "2", KKLibrary.Status.COMPLETED)
        library[KKLibrary.Kind.LITERATURES.value to "101"] = MockLibraryEntry(KKLibrary.Kind.LITERATURES.value, "101", KKLibrary.Status.INPROGRESS)
        library[KKLibrary.Kind.GAMES.value to "201"] = MockLibraryEntry(KKLibrary.Kind.GAMES.value, "201", KKLibrary.Status.PLANNING)

        favorites += (KKLibrary.Kind.SHOWS.value to "1")
        reminders += (KKLibrary.Kind.SHOWS.value to "1")
        watchedEpisodeIds += "100"
        followingUserIds += "1002"

        val me = currentUser()
        reviews += Review(
            id = "901",
            type = "reviews",
            href = "/v1/reviews/901",
            attributes = Review.Attributes(
                score = 5.0,
                description = "An unforgettable adventure that gets better every arc.",
                isSpoiler = false,
                recommendation = ReviewRecommendation.recommended,
                helpfulCount = 12,
                unhelpfulCount = 1,
                createdAt = LocalDate(2025, 1, 10),
            ),
            relationships = Review.Relationships(
                shows = ShowIdentityResponse(listOf(ShowIdentity("1", "shows", "/v1/anime/1")), next = null),
                users = UserResponse(listOf(me), next = null),
            ),
        )

        feedMessages += FeedMessage(
            id = "2001",
            type = "feed-messages",
            href = "/v1/feed/messages/2001",
            attributes = FeedMessage.Attributes(
                content = "Just started watching Frieren in Kurozora Mock Mode!",
                contentHTML = "<p>Just started watching Frieren in Kurozora Mock Mode!</p>",
                contentMarkdown = "Just started watching Frieren in Kurozora Mock Mode!",
                metrics = FeedMessage.Attributes.Metrics(heartCount = 5, replyCount = 1, reShareCount = 0),
                isNSFW = false, isPinned = false, isReply = false, isReShare = false,
                isReShared = false, isSpoiler = false, createdAtTimestamp = 1737000000L,
            ),
            relationships = FeedMessage.Relationships(users = UserResponse(listOf(me), next = null)),
        )

        parentalGuideEntries += ParentalGuideEntry(
            id = "3001",
            type = "parental-guide-entries",
            href = "/v1/parentalguide/entries/3001",
            attributes = ParentalGuideEntry.Attributes(
                category = ParentalGuideCategory.violenceAndGore,
                rating = ParentalGuideRating.mild,
                frequency = ParentalGuideFrequency.occasional,
                depiction = ParentalGuideDepiction.shown,
                reason = "Stylized shonen battle scenes.",
                isSpoiler = false,
                userID = me.id,
                helpfulCount = 4,
                unhelpfulCount = 0,
            ),
        )

        notifications += UserNotification(
            id = "4001",
            type = "user-notifications",
            href = "/v1/me/notifications/4001",
            attributes = UserNotification.Attributes(
                type = UserNotificationType.NewFollower,
                isRead = false,
                payload = UserNotification.Payload(userID = "1002", username = "luffy"),
                description = "luffy started following you.",
                createdAt = LocalDate(2025, 2, 1),
            ),
        )

        val mockPlatformInfo = PlatformResponse(
            data = listOf(Platform("platforms", Platform.Attributes("Android 15", "Android", "15", "Google", "Pixel")))
        )
        val mockLocationInfo = LocationResponse(
            data = listOf(Location("locations", Location.Attributes("Istanbul", "Marmara", "Turkey", 41.0082, 28.9784)))
        )

        accessTokens += AccessToken(
            id = "mock-token-1001",
            type = "access-tokens",
            href = "/v1/me/access-tokens/mock-token-1001",
            attributes = AccessToken.Attributes(ipAddress = "127.0.0.1", lastValidatedAtTimestamp = 1737000000L, appSource = "Kurozora Android"),
            relationships = AccessToken.Relationships(platform = mockPlatformInfo, location = mockLocationInfo),
        )

        sessions += Session(
            id = "sess-1001",
            type = "sessions",
            href = "/v1/me/sessions/sess-1001",
            attributes = Session.Attributes(ipAddress = "127.0.0.1", lastValidatedAt = LocalDate(2025, 2, 1), appSource = "Kurozora Android"),
            relationships = Session.Relationships(platform = mockPlatformInfo, location = mockLocationInfo),
        )
    }
}
