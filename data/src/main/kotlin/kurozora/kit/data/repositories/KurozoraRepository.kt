package kurozora.kit.data.repositories

import kurozora.kit.api.KurozoraApiClient
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
import kurozora.kit.data.repositories.user.UserRepository
import kurozora.kit.data.repositories.user.UserRepositoryImpl
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
