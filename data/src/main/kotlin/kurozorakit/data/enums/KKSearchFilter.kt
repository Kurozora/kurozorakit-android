package kurozorakit.data.enums

import kurozorakit.data.models.search.filters.AppThemeFilter
import kurozorakit.data.models.search.filters.CharacterFilter
import kurozorakit.data.models.search.filters.EpisodeFilter
import kurozorakit.data.models.search.filters.GameFilter
import kurozorakit.data.models.search.filters.LiteratureFilter
import kurozorakit.data.models.search.filters.PersonFilter
import kurozorakit.data.models.search.filters.ShowFilter
import kurozorakit.data.models.search.filters.SongFilter
import kurozorakit.data.models.search.filters.StudioFilter
import kurozorakit.data.models.search.filters.UserFilter

/**
 * A type-safe container for all possible search filters within the Kurozora ecosystem.
 *
 * This sealed interface ensures that exhaustive checking is performed when processing
 * search requests. Each entry wraps a specific domain-model filter (e.g., [ShowFilter])
 * and provides a unified way to convert these complex filters into API-ready maps.
 */
sealed interface KKSearchFilter {
    /** Filters related to application themes and visual styles. */
    data class AppTheme(val filter: AppThemeFilter) : KKSearchFilter
    /** Filters for finding fictional characters (e.g., by role or gender). */
    data class Character(val filter: CharacterFilter) : KKSearchFilter
    /** Filters for individual episodes (e.g., by air date or episode type). */
    data class Episode(val filter: EpisodeFilter) : KKSearchFilter
    /** Filters for video games (e.g., by platform or game status). */
    data class Game(val filter: GameFilter) : KKSearchFilter
    /** Filters for manga and light novels (e.g., by serialization status). */
    data class Literature(val filter: LiteratureFilter) : KKSearchFilter
    /** Filters for real-life people like voice actors or authors. */
    data class Person(val filter: PersonFilter) : KKSearchFilter
    /** Filters for anime and animated series (e.g., by season or rating). */
    data class Show(val filter: ShowFilter) : KKSearchFilter
    /** Filters for music tracks, OPs, and EDs. */
    data class Song(val filter: SongFilter) : KKSearchFilter
    /** Filters for production studios and animation houses. */
    data class Studio(val filter: StudioFilter) : KKSearchFilter
    /** Filters for finding other users within the community. */
    data class User(val filter: UserFilter) : KKSearchFilter
}

/**
 * Converts the specific [KKSearchFilter] into a map of query parameters for network requests.
 *
 * @param forLibrary If `true`, the resulting map will include additional parameters
 * specific to user library filtering (like personal status or private notes).
 * @return A [Map] containing the key-value pairs representing the active filters.
 */
fun KKSearchFilter.toFilterMap(forLibrary: Boolean = false): Map<String, Any?> {
    return when (this) {
        is KKSearchFilter.AppTheme -> this.filter.toFilterMap(forLibrary)
        is KKSearchFilter.Character -> this.filter.toFilterMap(forLibrary)
        is KKSearchFilter.Episode -> this.filter.toFilterMap(forLibrary)
        is KKSearchFilter.Game -> this.filter.toFilterMap(forLibrary)
        is KKSearchFilter.Literature -> this.filter.toFilterMap(forLibrary)
        is KKSearchFilter.Person -> this.filter.toFilterMap(forLibrary)
        is KKSearchFilter.Show -> this.filter.toFilterMap(forLibrary)
        is KKSearchFilter.Song -> this.filter.toFilterMap(forLibrary)
        is KKSearchFilter.Studio -> this.filter.toFilterMap(forLibrary)
        is KKSearchFilter.User -> this.filter.toFilterMap(forLibrary)
    }
}