package kurozorakit.data.models.explore

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.genre.GenreIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.recap.RecapResponse
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.show.song.ShowSongResponse
import kurozorakit.data.models.theme.ThemeIdentityResponse

/**
 * The core building block of the Kurozora Discovery engine.
 *
 * [ExploreCategory] implements a **Server-Driven UI (SDUI)** pattern. Instead of hardcoding
 * the Explore tab, the application receives a list of these categories which dictate
 * the visual hierarchy, layout constraints, and the specific type of media to be rendered.
 *
 * This enables real-time updates to the "Home" or "Explore" screens (e.g., promotional
 * banners or holiday-themed sections) without requiring a store update.
 *
 * @property id Unique identifier for the specific category section.
 * @property type Resource type identifier, typically "explore-categories".
 * @property href The direct API link to fetch this specific category's full details.
 * @property attributes Configuration metadata determining the visual style and UI behavior.
 * @property relationships The collection of polymorphic media pointers that populate this section.
 */
@Serializable
data class ExploreCategory(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null,
) : IdentityResource {

    /**
     * Visual and behavioral configuration for a Discovery section.
     *
     * These properties function as instructions for the client-side rendering engine,
     * defining how the items within this category should be displayed.
     *
     * @property slug A URL-safe unique identifier used for deep-linking and routing within the app.
     * @property title The primary header text displayed to the user (e.g., "Trending Now", "Summer 2024").
     * @property description Optional sub-header or explanatory text providing context for the content.
     * @property position The vertical sort order determining where this section appears in the discovery feed.
     * @property type The domain of the content (e.g., "shows", "people"). This tells the UI which
     * specific "See More" navigation path to use.
     * @property size The layout template identifier (e.g., "small_grid", "hero_carousel").
     * Maps to specific UI component styles or aspect ratios.
     */
    @Serializable
    data class Attributes(
        val slug: String,
        val title: String,
        val description: String?,
        val position: Int,
        val type: String,
        val size: String,
    )

    /**
     * The Resource Connections that populate the section.
     *
     * In line with the Kurozora polymorphic pattern, while multiple fields are available,
     * only the one corresponding to [Attributes.type] is typically populated for a
     * specific category instance.
     *
     * @property characters References to character profiles for "Popular Characters" rows.
     * @property episodes References to specific episodes for "New Releases" or "Recently Aired".
     * @property games References to video games for cross-media discovery sections.
     * @property literatures References to manga, light novels, and other written works.
     * @property people References to voice actors (Seiyuu), directors, or industry staff.
     * @property showSongs Theme songs (OPs/EDs) associated with the featured media.
     * @property shows The primary show (Anime/Live Action) resources for the main feed.
     * @property genres Metadata tags for genre-based discovery grids (e.g., "Action", "Slice of Life").
     * @property themes Specialized aesthetic tags (e.g., "Cyberpunk", "Isekai").
     * @property recaps Short-form visual updates or "Story-style" content summaries.
     */
    @Serializable
    data class Relationships(
        val characters: CharacterIdentityResponse? = null,
        val episodes: EpisodeIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null,
        val people: PersonIdentityResponse? = null,
        val showSongs: ShowSongResponse? = null,
        val shows: ShowIdentityResponse? = null,
        val genres: GenreIdentityResponse? = null,
        val themes: ThemeIdentityResponse? = null,
        val recaps: RecapResponse? = null,
    )
}