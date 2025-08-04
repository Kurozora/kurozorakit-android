package kurozora.kit.core

import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.api.TokenProvider
import kurozora.kit.data.DefaultTokenProvider
import kurozora.kit.shared.logging.CompositeLogger
import kurozora.kit.shared.logging.ConsoleLogger
import kurozora.kit.shared.logging.KurozoraLogger
import kurozora.kit.shared.logging.LogLevel
import kurozora.kit.data.repositories.auth.AuthRepository
import kurozora.kit.data.repositories.auth.AuthRepositoryImpl
import kurozora.kit.data.repositories.cast.CastRepository
import kurozora.kit.data.repositories.cast.CastRepositoryImpl
import kurozora.kit.data.repositories.character.CharacterRepository
import kurozora.kit.data.repositories.character.CharacterRepositoryImpl
import kurozora.kit.data.repositories.episode.EpisodeRepository
import kurozora.kit.data.repositories.episode.EpisodeRepositoryImpl
import kurozora.kit.data.repositories.explore.ExploreRepository
import kurozora.kit.data.repositories.explore.ExploreRepositoryImpl
import kurozora.kit.data.repositories.feed.FeedRepository
import kurozora.kit.data.repositories.feed.FeedRepositoryImpl
import kurozora.kit.data.repositories.game.GameRepository
import kurozora.kit.data.repositories.game.GameRepositoryImpl
import kurozora.kit.data.repositories.genre.GenreRepository
import kurozora.kit.data.repositories.genre.GenreRepositoryImpl
import kurozora.kit.data.repositories.image.ImageRepository
import kurozora.kit.data.repositories.image.ImageRepositoryImpl
import kurozora.kit.data.repositories.legal.LegalRepository
import kurozora.kit.data.repositories.legal.LegalRepositoryImpl
import kurozora.kit.data.repositories.literature.LiteratureRepository
import kurozora.kit.data.repositories.literature.LiteratureRepositoryImpl
import kurozora.kit.data.repositories.misc.MiscRepository
import kurozora.kit.data.repositories.misc.MiscRepositoryImpl
import kurozora.kit.data.repositories.person.PersonRepository
import kurozora.kit.data.repositories.person.PersonRepositoryImpl
import kurozora.kit.data.repositories.review.ReviewRepository
import kurozora.kit.data.repositories.review.ReviewRepositoryImpl
import kurozora.kit.data.repositories.schedule.ScheduleRepository
import kurozora.kit.data.repositories.schedule.ScheduleRepositoryImpl
import kurozora.kit.data.repositories.search.SearchRepository
import kurozora.kit.data.repositories.search.SearchRepositoryImpl
import kurozora.kit.data.repositories.season.SeasonRepository
import kurozora.kit.data.repositories.season.SeasonRepositoryImpl
import kurozora.kit.data.repositories.show.ShowRepository
import kurozora.kit.data.repositories.show.ShowRepositoryImpl
import kurozora.kit.data.repositories.song.SongRepository
import kurozora.kit.data.repositories.song.SongRepositoryImpl
import kurozora.kit.data.repositories.store.StoreRepository
import kurozora.kit.data.repositories.store.StoreRepositoryImpl
import kurozora.kit.data.repositories.studio.StudioRepository
import kurozora.kit.data.repositories.studio.StudioRepositoryImpl
import kurozora.kit.data.repositories.theme.ThemeRepository
import kurozora.kit.data.repositories.theme.ThemeRepositoryImpl
import kurozora.kit.data.repositories.themestore.ThemeStoreRepository
import kurozora.kit.data.repositories.themestore.ThemeStoreRepositoryImpl
import kurozora.kit.data.repositories.user.UserRepository
import kurozora.kit.data.repositories.user.UserRepositoryImpl

/**
 * Main entry point for the KurozoraKit library.
 * This class provides access to all the functionality of the library.
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
    private val themeStoreRepository: ThemeStoreRepository
) {

    /**
     * Access to the show repository.
     */
    fun show(): ShowRepository = showRepository

    /**
     * Access to the literature repository.
     */
    fun literature(): LiteratureRepository = literatureRepository

    /**
     * Access to the game repository.
     */
    fun game(): GameRepository = gameRepository

    /**
     * Access to the character repository.
     */
    fun character(): CharacterRepository = characterRepository

    /**
     * Access to the person repository.
     */
    fun people(): PersonRepository = personRepository

    /**
     * Access to the studio repository.
     */
    fun studio(): StudioRepository = studioRepository

    /**
     * Access to the genre repository.
     */
    fun genre(): GenreRepository = genreRepository

    /**
     * Access to the auth repository.
     */
    fun auth(): AuthRepository = authRepository

    /**
     * Access to the user repository.
     */
    fun user(): UserRepository = userRepository

    /**
     * Access to the search repository.
     */
    fun search(): SearchRepository = searchRepository

    /**
     * Access to the cast repository.
     */
    fun cast(): CastRepository = castRepository

    /**
     * Access to the episode repository.
     */
    fun episode(): EpisodeRepository = episodeRepository

    /**
     * Access to the explore repository.
     */
    fun explore(): ExploreRepository = exploreRepository

    /**
     * Access to the feed repository.
     */
    fun feed(): FeedRepository = feedRepository

    /**
     * Access to the image repository.
     */
    fun image(): ImageRepository = imageRepository

    /**
     * Access to the legal repository.
     */
    fun legal(): LegalRepository = legalRepository

    /**
     * Access to the misc repository.
     */
    fun misc(): MiscRepository = miscRepository

    /**
     * Access to the review repository.
     */
    fun review(): ReviewRepository = reviewRepository

    /**
     * Access to the schedule repository.
     */
    fun schedule(): ScheduleRepository = scheduleRepository

    /**
     * Access to the season repository.
     */
    fun season(): SeasonRepository = seasonRepository

    /**
     * Access to the song repository.
     */
    fun song(): SongRepository = songRepository

    /**
     * Access to the store repository.
     */
    fun store(): StoreRepository = storeRepository

    /**
     * Access to the theme repository.
     */
    fun theme(): ThemeRepository = themeRepository

    /**
     * Access to the theme store repository.
     */
    fun themeStore(): ThemeStoreRepository = themeStoreRepository

    companion object {
        /**
         * The current version of the KurozoraKit library.
         */
        const val version = "1.0.0"
    }

    /**
     * Builder class for creating a KurozoraKit instance.
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

        /**
         * Sets the API endpoint.
         */
        fun apiEndpoint(apiEndpoint: String) = apply { this.apiEndpoint = apiEndpoint }

        /**
         * Sets the API key.
         */
        fun apiKey(apiKey: String) = apply { this.apiKey = apiKey }

        /**
         * Sets the token provider.
         */
        fun tokenProvider(tokenProvider: TokenProvider) = apply { this.tokenProvider = tokenProvider }

        /**
         * Sets the log level.
         */
        fun logLevel(level: LogLevel) = apply { this.logLevel = level }

        /**
         * Sets the maximum number of retries for API requests.
         */
        fun maxRetries(retries: Int) = apply { this.maxRetries = retries }

        /**
         * Sets the initial backoff delay in milliseconds for retries.
         */
        fun initialBackoffDelayMs(delay: Long) = apply { this.initialBackoffDelayMs = delay }

        /**
         * Sets the maximum backoff delay in milliseconds for retries.
         */
        fun maxBackoffDelayMs(delay: Long) = apply { this.maxBackoffDelayMs = delay }

        /**
         * Sets the backoff factor for retries.
         */
        fun backoffFactor(factor: Double) = apply { this.backoffFactor = factor }

        /**
         * Builds a new KurozoraKit instance.
         */
        fun build(): KurozoraKit {
            // Set up logging
            val logger = CompositeLogger(
                listOf(ConsoleLogger(logLevel)),
                logLevel
            )
            KurozoraLogger.setLogger(logger)

            // Create the API client
            val apiClient = KurozoraApiClient(
                baseUrl = apiEndpoint,
                apiKey = apiKey,
                userAgent = "KurozoraApp/1.0.0 (com.seloreis.kurozora; Android 14) KtorClient/3.2.2",
                tokenProvider = tokenProvider
            )

            // Create all repositories
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
                themeStoreRepository = themeStoreRepository
            )
        }
    }
}