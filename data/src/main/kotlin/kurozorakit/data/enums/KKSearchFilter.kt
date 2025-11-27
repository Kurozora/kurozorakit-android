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

sealed interface KKSearchFilter {
    data class AppTheme(val filter: AppThemeFilter) : KKSearchFilter
    data class Character(val filter: CharacterFilter) : KKSearchFilter
    data class Episode(val filter: EpisodeFilter) : KKSearchFilter
    data class Game(val filter: GameFilter) : KKSearchFilter
    data class Literature(val filter: LiteratureFilter) : KKSearchFilter
    data class Person(val filter: PersonFilter) : KKSearchFilter
    data class Show(val filter: ShowFilter) : KKSearchFilter
    data class Song(val filter: SongFilter) : KKSearchFilter
    data class Studio(val filter: StudioFilter) : KKSearchFilter
    data class User(val filter: UserFilter) : KKSearchFilter
}

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