package kurozorakit.data.models.airseason

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozorakit.data.models.game.GameResponse
import kurozorakit.data.models.literature.LiteratureResponse
import kurozorakit.data.models.show.ShowResponse
import kurozorakit.data.models.show.attributes.MediaType

@Serializable
data class AirSeason(
    val type: String,
    val attributes: Attributes,
    val relationships: Relationships
) {
    @Serializable
    data class Attributes(
        val type: MediaType,
    )

    @Serializable
    data class Relationships(
        val shows: ShowResponse? = null,
        val games: GameResponse? = null,
        val literatures: LiteratureResponse? = null
    )
}