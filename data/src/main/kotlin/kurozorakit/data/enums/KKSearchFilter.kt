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

