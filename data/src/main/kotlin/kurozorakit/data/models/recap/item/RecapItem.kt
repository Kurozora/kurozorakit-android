package kurozorakit.data.models.recap.item

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.RecapItemType
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.genre.GenreIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.recap.entry.RecapEntryResponse
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.show.song.ShowSongResponse
import kurozorakit.data.models.studio.StudioIdentityResponse
import kurozorakit.data.models.theme.ThemeIdentityResponse

/**
 * Represents a specific data segment within a user's periodic Recap.
 *
 * A [RecapItem] breaks down the user's consumption into categories like "Top Genres",
 * "Most Watched Shows", or "Favorite Characters". It provides quantitative metrics
 * (counts, duration) and links to the specific resources that defined the user's
 * activity during that period.
 *
 * @property id The unique identifier for this specific recap item.
 * @property type The resource type designation, typically "recapItems".
 * @property href The canonical API link to fetch detailed information about this item.
 * @property attributes Quantitative data and classification of the recap segment.
 * @property relationships Pointers to the actual media resources associated with this summary.
 * @see IdentityResource
 */
@Serializable
data class RecapItem(
    override val id: String,
    override val type: String = "recapItems",
    override val href: String = "",
    val attributes: Attributes,
    val relationships: Relationships? = null
) : IdentityResource {

    /**
     * Statistical metadata for a [RecapItem].
     *
     * @property year The calendar year associated with this statistical data.
     * @property type The raw string representation of the item's category (e.g., "SHOWS", "GENRES").
     * @property totalSeriesCount Total number of unique series or main entities interacted with.
     * @property totalPartsCount Total number of sub-units consumed (e.g., episodes watched, chapters read).
     * @property totalPartsDuration Total time spent consuming this category, usually in minutes.
     * @property topPercentile The user's standing compared to the global community (e.g., "Top 5% of listeners").
     */
    @Serializable
    data class Attributes(
        val year: Int = 0,
        val month: Int? = null,
        val type: String = "",
        val totalSeriesCount: Int = 0,
        val totalPartsCount: Int = 0,
        val totalPartsDuration: Int = 0,
        val topPercentile: Double = 0.0
    ) {
        /**
         * A helper property that maps the raw [type] string to a type-safe [RecapItemType] enum.
         * Defaults to [RecapItemType.SHOWS] if the type is unknown.
         */
        val recapItemType: RecapItemType
            get() = RecapItemType.entries.firstOrNull { it.name.equals(type, true) } ?: RecapItemType.SHOWS
    }

    /**
     * Contextual links to the resources that constitute this [RecapItem].
     *
     * Depending on the [RecapItemType], only specific properties will be populated.
     *
     * @property shows List of shows that contributed to the recap stats.
     * @property games Games played during the period.
     * @property literatures Manga or light novels read during the period.
     * @property episodes Specific episodes that were highlights of the recap.
     * @property showSongs Most listened to or relevant theme songs.
     * @property genres Top genres based on the user's consumption.
     * @property themes Top narrative themes encountered.
     * @property studios Top animation or production studios encountered.
     * @property characters Most engaged or favorite characters.
     * @property people Notable staff or creators (e.g., most watched director).
     * @property items Ranked [kurozorakit.data.models.recap.entry.RecapEntry] list with per-item stats.
     */
    @Serializable
    data class Relationships(
        val shows: ShowIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null,
        val episodes: EpisodeIdentityResponse? = null,
        val showSongs: ShowSongResponse? = null,
        val genres: GenreIdentityResponse? = null,
        val themes: ThemeIdentityResponse? = null,
        val studios: StudioIdentityResponse? = null,
        val characters: CharacterIdentityResponse? = null,
        val people: PersonIdentityResponse? = null,
        val items: RecapEntryResponse? = null,
    )
}