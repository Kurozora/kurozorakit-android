package kurozorakit.core

import kurozorakit.api.KurozoraApiClient
import kurozorakit.api.Platform
import kurozorakit.api.TokenProvider
import kurozorakit.shared.UserAgent
import kurozorakit.cache.CacheManager
import kurozorakit.data.DefaultTokenProvider
import kurozorakit.data.models.user.User
import kurozorakit.data.repositories.airseason.AirSeasonRepository
import kurozorakit.data.repositories.airseason.AirSeasonRepositoryImpl
import kurozorakit.shared.logging.CompositeLogger
import kurozorakit.shared.logging.ConsoleLogger
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.shared.logging.LogLevel
import kurozorakit.data.repositories.auth.AuthRepository
import kurozorakit.data.repositories.auth.AuthRepositoryImpl
import kurozorakit.data.repositories.cast.CastRepository
import kurozorakit.data.repositories.cast.CastRepositoryImpl
import kurozorakit.data.repositories.character.CharacterRepository
import kurozorakit.data.repositories.character.CharacterRepositoryImpl
import kurozorakit.data.repositories.episode.EpisodeRepository
import kurozorakit.data.repositories.episode.EpisodeRepositoryImpl
import kurozorakit.data.repositories.explore.ExploreRepository
import kurozorakit.data.repositories.explore.ExploreRepositoryImpl
import kurozorakit.data.repositories.feed.FeedRepository
import kurozorakit.data.repositories.feed.FeedRepositoryImpl
import kurozorakit.data.repositories.game.GameRepository
import kurozorakit.data.repositories.game.GameRepositoryImpl
import kurozorakit.data.repositories.genre.GenreRepository
import kurozorakit.data.repositories.genre.GenreRepositoryImpl
import kurozorakit.data.repositories.image.ImageRepository
import kurozorakit.data.repositories.image.ImageRepositoryImpl
import kurozorakit.data.repositories.legal.LegalRepository
import kurozorakit.data.repositories.legal.LegalRepositoryImpl
import kurozorakit.data.repositories.literature.LiteratureRepository
import kurozorakit.data.repositories.literature.LiteratureRepositoryImpl
import kurozorakit.data.repositories.misc.MiscRepository
import kurozorakit.data.repositories.misc.MiscRepositoryImpl
import kurozorakit.data.repositories.person.PersonRepository
import kurozorakit.data.repositories.person.PersonRepositoryImpl
import kurozorakit.data.repositories.review.ReviewRepository
import kurozorakit.data.repositories.review.ReviewRepositoryImpl
import kurozorakit.data.repositories.schedule.ScheduleRepository
import kurozorakit.data.repositories.schedule.ScheduleRepositoryImpl
import kurozorakit.data.repositories.search.SearchRepository
import kurozorakit.data.repositories.search.SearchRepositoryImpl
import kurozorakit.data.repositories.season.SeasonRepository
import kurozorakit.data.repositories.season.SeasonRepositoryImpl
import kurozorakit.data.repositories.show.ShowRepository
import kurozorakit.data.repositories.show.ShowRepositoryImpl
import kurozorakit.data.repositories.song.SongRepository
import kurozorakit.data.repositories.song.SongRepositoryImpl
import kurozorakit.data.repositories.store.StoreRepository
import kurozorakit.data.repositories.store.StoreRepositoryImpl
import kurozorakit.data.repositories.studio.StudioRepository
import kurozorakit.data.repositories.studio.StudioRepositoryImpl
import kurozorakit.data.repositories.theme.ThemeRepository
import kurozorakit.data.repositories.theme.ThemeRepositoryImpl
import kurozorakit.data.repositories.themestore.ThemeStoreRepository
import kurozorakit.data.repositories.themestore.ThemeStoreRepositoryImpl
import kurozorakit.data.repositories.user.UserRepository
import kurozorakit.data.repositories.user.UserRepositoryImpl

/**
 * The main entry point for the KurozoraKit library.
 *
 * This class acts as a facade, providing a unified access point to all repositories and features
 * within the Kurozora ecosystem. It should be initialized using the [Builder] class to configure
 * API keys, platform details, and caching mechanisms.
 */
class KurozoraKit private constructor(
    private val apiClient: KurozoraApiClient,
    private val showRepository: ShowRepository,
    private val literatureRepository: LiteratureRepository,
    private val gameRepository: GameRepository,
    private val characterRepository: CharacterRepository,
    private val personRepository: PersonRepository,
    private val studioRepository: StudioRepository,
    private val genreRepository: GenreRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val searchRepository: SearchRepository,
    private val castRepository: CastRepository,
    private val episodeRepository: EpisodeRepository,
    private val exploreRepository: ExploreRepository,
    private val feedRepository: FeedRepository,
    private val imageRepository: ImageRepository,
    private val legalRepository: LegalRepository,
    private val miscRepository: MiscRepository,
    private val reviewRepository: ReviewRepository,
    private val scheduleRepository: ScheduleRepository,
    private val seasonRepository: SeasonRepository,
    private val songRepository: SongRepository,
    private val storeRepository: StoreRepository,
    private val themeRepository: ThemeRepository,
    private val themeStoreRepository: ThemeStoreRepository,
    private val airSeasonRepository: AirSeasonRepository,
) {

    /** Provides access to Anime (Show) related data and operations. */
    fun show(): ShowRepository = showRepository

    /** Provides access to Literature (Manga/Novels) related data and operations. */
    fun literature(): LiteratureRepository = literatureRepository

    /** Provides access to Game related data and operations. */
    fun game(): GameRepository = gameRepository

    /** Provides access to fictional Character data and biographies. */
    fun character(): CharacterRepository = characterRepository

    /** Provides access to People (Voice Actors, Staff) related data. */
    fun people(): PersonRepository = personRepository

    /** Provides access to production Studios and Publisher information. */
    fun studio(): StudioRepository = studioRepository

    /** Provides access to Genre and category listings. */
    fun genre(): GenreRepository = genreRepository

    /** Handles authentication tasks such as Sign In, Sign Up, and account recovery. */
    fun auth(): AuthRepository = authRepository

    /** Manages User profiles, libraries, and social interactions. */
    fun user(): UserRepository = userRepository

    /** Provides global search functionality across all media types. */
    fun search(): SearchRepository = searchRepository

    /** Manages Cast and staff listings for specific media items. */
    fun cast(): CastRepository = castRepository

    /** Provides access to Episode-specific details and tracking. */
    fun episode(): EpisodeRepository = episodeRepository

    /** Accesses curated content and discovery feeds. */
    fun explore(): ExploreRepository = exploreRepository

    /** Manages the social activity feed and user posts. */
    fun feed(): FeedRepository = feedRepository

    /** Provides access to image resources and random media. */
    fun image(): ImageRepository = imageRepository

    /** Accesses legal documents like Privacy Policy and Terms of Use. */
    fun legal(): LegalRepository = legalRepository

    /** Accesses general system information and application settings. */
    fun misc(): MiscRepository = miscRepository

    /** Manages user Reviews and ratings for various media. */
    fun review(): ReviewRepository = reviewRepository

    /** Provides access to airing schedules and release dates. */
    fun schedule(): ScheduleRepository = scheduleRepository

    /** Accesses media organized by calendar seasons (e.g., Summer 2024). */
    fun season(): SeasonRepository = seasonRepository

    /** Accesses Song data, including OSTs, Openings, and Endings. */
    fun song(): SongRepository = songRepository

    /** Manages Store-related operations and purchase verification. */
    fun store(): StoreRepository = storeRepository

    /** Accesses UI themes and styling data. */
    fun theme(): ThemeRepository = themeRepository

    /** Accesses the theme marketplace for browsing and acquiring new themes. */
    fun themeStore(): ThemeStoreRepository = themeStoreRepository

    /** Provides information about seasonal airing status. */
    fun airSeason(): AirSeasonRepository = airSeasonRepository

    companion object {
        /**
         * The current version of the KurozoraKit library.
         */
        const val version = "1.0.0"
    }

    /**
     * Builder class for constructing a [KurozoraKit] instance with custom configuration.
     *
     * Use this class to define the environment, credentials, and performance settings
     * before initializing the library.
     */
    class Builder {
        private var apiEndpoint: String = KurozoraApi.V1.baseUrl
        private var apiKey: String = ""
        private var tokenProvider: TokenProvider = DefaultTokenProvider
        private var logLevel: LogLevel = LogLevel.INFO
        private var maxRetries: Int = 3
        private var initialBackoffDelayMs: Long = 1000
        private var maxBackoffDelayMs: Long = 30000
        private var backoffFactor: Double = 2.0
        private var userAgent: UserAgent = UserAgent()
        private var cacheManager: CacheManager? = null
        private lateinit var platform: Platform

        /**
         * Sets the base API endpoint URL. Defaults to [KurozoraApi.V1.baseUrl].
         */
        fun apiEndpoint(apiEndpoint: String) = apply { this.apiEndpoint = apiEndpoint }

        /**
         * Sets the API Key required for authenticating requests.
         */
        fun apiKey(apiKey: String) = apply { this.apiKey = apiKey }

        /**
         * Sets the [Platform] implementation providing device-specific info.
         */
        fun platform(platform: Platform) = apply { this.platform = platform }

        /**
         * Sets a custom [TokenProvider] for managing authentication tokens.
         */
        fun tokenProvider(tokenProvider: TokenProvider) = apply { this.tokenProvider = tokenProvider }

        /**
         * Configures the library's internal logging level.
         */
        fun logLevel(level: LogLevel) = apply { this.logLevel = level }

        /**
         * Configures the maximum number of retries for failed network requests.
         */
        fun maxRetries(retries: Int) = apply { this.maxRetries = retries }

        /**
         * Sets the starting delay for exponential backoff during retries.
         */
        fun initialBackoffDelayMs(delay: Long) = apply { this.initialBackoffDelayMs = delay }

        /**
         * Sets the maximum upper limit for backoff delay during retries.
         */
        fun maxBackoffDelayMs(delay: Long) = apply { this.maxBackoffDelayMs = delay }

        /**
         * Sets the multiplier for the exponential backoff logic.
         */
        fun backoffFactor(factor: Double) = apply { this.backoffFactor = factor }

        /**
         * Configures a [CacheManager] to handle local data caching.
         */
        fun cacheManager(cacheManager: CacheManager) = apply { this.cacheManager = cacheManager }

        /**
         * Sets a custom [UserAgent] to be sent with every request.
         */
        fun userAgent(userAgent: UserAgent) = apply { this.userAgent = userAgent }

        /**
         * Finalizes the configuration and returns a fully initialized [KurozoraKit] instance.
         *
         * This method sets up the logging system, initializes the HTTP client,
         * and wires up all internal repositories.
         * * @throws UninitializedPropertyAccessException if [platform] is not set.
         */
        fun build(): KurozoraKit {
            // Initialize logging infrastructure
            val logger = CompositeLogger(
                listOf(ConsoleLogger(logLevel)),
                logLevel
            )
            KurozoraLogger.setLogger(logger)

            // Initialize the shared API client
            val apiClient = KurozoraApiClient(
                baseUrl = apiEndpoint,
                apiKey = apiKey,
                tokenProvider = tokenProvider,
                cacheManager = cacheManager,
                platform = platform,
                userAgent = userAgent,
            )

            // Instantiate repository implementations
            val showRepository = ShowRepositoryImpl(apiClient)
            val literatureRepository = LiteratureRepositoryImpl(apiClient)
            val gameRepository = GameRepositoryImpl(apiClient)
            val characterRepository = CharacterRepositoryImpl(apiClient)
            val personRepository = PersonRepositoryImpl(apiClient)
            val studioRepository = StudioRepositoryImpl(apiClient)
            val genreRepository = GenreRepositoryImpl(apiClient)
            val authRepository = AuthRepositoryImpl(apiClient)
            val userRepository = UserRepositoryImpl(apiClient)
            val searchRepository = SearchRepositoryImpl(apiClient)
            val castRepository = CastRepositoryImpl(apiClient)
            val episodeRepository = EpisodeRepositoryImpl(apiClient)
            val exploreRepository = ExploreRepositoryImpl(apiClient)
            val feedRepository = FeedRepositoryImpl(apiClient)
            val imageRepository = ImageRepositoryImpl(apiClient)
            val legalRepository = LegalRepositoryImpl(apiClient)
            val miscRepository = MiscRepositoryImpl(apiClient)
            val reviewRepository = ReviewRepositoryImpl(apiClient)
            val scheduleRepository = ScheduleRepositoryImpl(apiClient)
            val seasonRepository = SeasonRepositoryImpl(apiClient)
            val songRepository = SongRepositoryImpl(apiClient)
            val storeRepository = StoreRepositoryImpl(apiClient)
            val themeRepository = ThemeRepositoryImpl(apiClient)
            val themeStoreRepository = ThemeStoreRepositoryImpl(apiClient)
            val airSeasonRepository = AirSeasonRepositoryImpl(apiClient)

            return KurozoraKit(
                apiClient = apiClient,
                showRepository = showRepository,
                literatureRepository = literatureRepository,
                gameRepository = gameRepository,
                characterRepository = characterRepository,
                personRepository = personRepository,
                studioRepository = studioRepository,
                genreRepository = genreRepository,
                authRepository = authRepository,
                userRepository = userRepository,
                searchRepository = searchRepository,
                castRepository = castRepository,
                episodeRepository = episodeRepository,
                exploreRepository = exploreRepository,
                feedRepository = feedRepository,
                imageRepository = imageRepository,
                legalRepository = legalRepository,
                miscRepository = miscRepository,
                reviewRepository = reviewRepository,
                scheduleRepository = scheduleRepository,
                seasonRepository = seasonRepository,
                songRepository = songRepository,
                storeRepository = storeRepository,
                themeRepository = themeRepository,
                themeStoreRepository = themeStoreRepository,
                airSeasonRepository = airSeasonRepository,
            )
        }
    }
}