package kurozorakit.data.models.recap.entry

import kotlinx.serialization.Serializable
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.genre.GenreIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.studio.StudioIdentityResponse
import kurozorakit.data.models.theme.ThemeIdentityResponse

/**
 * Represents an individual ranked entry inside a [kurozorakit.data.models.recap.item.RecapItem].
 *
 * @property id Unique identifier for the recap entry.
 * @property type Resource type, typically "recap-entries".
 * @property attributes Ranking position, consumed parts count/duration, and optional staff role.
 * @property relationships Identity link to the underlying media, genre, theme, studio, character, or person.
 */
@Serializable
data class RecapEntry(
    val id: String,
    val type: String,
    val attributes: Attributes,
    val relationships: Relationships? = null,
) {
    @Serializable
    data class Attributes(
        val position: Int,
        val partsCount: Int = 0,
        val partsDuration: Int = 0,
        val role: String? = null,
    )

    @Serializable
    data class Relationships(
        val shows: ShowIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val genres: GenreIdentityResponse? = null,
        val themes: ThemeIdentityResponse? = null,
        val studios: StudioIdentityResponse? = null,
        val characters: CharacterIdentityResponse? = null,
        val people: PersonIdentityResponse? = null,
    )
}

@Serializable
data class RecapEntryResponse(
    val data: List<RecapEntry> = emptyList(),
)
