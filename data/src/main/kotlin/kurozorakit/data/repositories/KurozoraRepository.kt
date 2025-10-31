package kurozorakit.data.repositories

import kurozorakit.api.KurozoraApiClient
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
import kurozorakit.data.repositories.user.UserRepository
import kurozorakit.data.repositories.user.UserRepositoryImpl
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

/**
 * Main repository interface that combines all Kurozora API functionality
 */
//interface KurozoraRepository :
//    ExploreRepository,
//    SearchRepository,
//    ShowRepository,
//    LiteratureRepository,
//    GameRepository,
//    CharacterRepository,
//    PersonRepository,
//    StudioRepository,
//    EpisodeRepository,
//    SongRepository,
//    CastRepository,
//    GenreRepository,
//    ReviewRepository,
//    ScheduleRepository,
//    FeedRepository,
//    ImageRepository,
//    MiscRepository,
//    AuthRepository,
//    MeRepository,
//    ThemeRepository,
//    ThemeStoreRepository,
//    StoreRepository,
//    LegalRepository
//
///**
// * Main repository implementation
// */
//class KurozoraRepositoryImpl(
//    private val apiClient: KurozoraApiClient
//) : KurozoraRepository,
//    ExploreRepositoryImpl(apiClient),
//    SearchRepositoryImpl(apiClient),
//    ShowRepositoryImpl(apiClient),
//    LiteratureRepositoryImpl(apiClient),
//    GameRepositoryImpl(apiClient),
//    CharacterRepositoryImpl(apiClient),
//    PersonRepositoryImpl(apiClient),
//    StudioRepositoryImpl(apiClient),
//    EpisodeRepositoryImpl(apiClient),
//    SongRepositoryImpl(apiClient),
//    CastRepositoryImpl(apiClient),
//    GenreRepositoryImpl(apiClient),
//    ReviewRepositoryImpl(apiClient),
//    ScheduleRepositoryImpl(apiClient),
//    FeedRepositoryImpl(apiClient),
//    ImageRepositoryImpl(apiClient),
//    MiscRepositoryImpl(apiClient),
//    AuthRepositoryImpl(apiClient),
//    MeRepositoryImpl(apiClient),
//    ThemeRepositoryImpl(apiClient),
//    ThemeStoreRepositoryImpl(apiClient),
//    StoreRepositoryImpl(apiClient),
//    LegalRepositoryImpl(apiClient)
