package kurozorakit.data.models.airseason

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozorakit.data.models.game.GameResponse
import kurozorakit.data.models.literature.LiteratureResponse
import kurozorakit.data.models.show.ShowResponse
import kurozorakit.data.models.show.attributes.MediaType

/**
 * Represents a collection of media released or updated within a specific broadcast or release season.
 *
 * This model follows a structured relationship pattern where the [Attributes] define the
 * category of content, and [Relationships] contain the actual data payload. It is primarily
 * used to populate "Seasonal" discovery feeds, grouping content by their release window.
 *
 * @property type The resource type identifier, typically "air-season".
 * @property attributes Metadata describing the nature and classification of this season entry.
 * @property relationships The concrete media entities (Shows, Games, or Literature)
 * associated with this seasonal release.
 */
@Serializable
data class AirSeason(
    val type: String,
    val attributes: Attributes,
    val relationships: Relationships
) {
    /**
     * Categorical metadata for the [AirSeason].
     *
     * @property type A [MediaType] enum that classifies the entry (e.g., ANIME, MANGA, GAME).
     * This acts as a discriminator for the UI to determine which field in [Relationships]
     * contains the relevant data.
     */
    @Serializable
    data class Attributes(
        val type: MediaType,
    )

    /**
     * The media content linked to this specific [AirSeason] entry.
     *
     * In the kurozorakit data architecture, these relationships are typically mutually
     * exclusive—meaning only the field corresponding to [Attributes.type] will be
     * non-null in a single object instance.
     *
     * @property shows The collection of animated series or films, populated when [Attributes.type] is a show/anime.
     * @property games The collection of video games, populated when [Attributes.type] is a game.
     * @property literatures The collection of written works (manga/novels), populated when [Attributes.type] is literature.
     */
    @Serializable
    data class Relationships(
        val shows: ShowResponse? = null,
        val games: GameResponse? = null,
        val literatures: LiteratureResponse? = null
    )
}