package kurozora.kit.data.models.schedule

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozora.kit.data.models.LocalDateSerializer
import kurozora.kit.data.models.game.GameResponse
import kurozora.kit.data.models.literature.LiteratureResponse
import kurozora.kit.data.models.show.ShowResponse

@Serializable
data class Schedule(
    val type: String,
    val attributes: Attributes,
    val relationships: Relationships
) {
    @Serializable
    data class Attributes(
        @Serializable(with = LocalDateSerializer::class)
        val date: LocalDate? = null,
    )

    @Serializable
    data class Relationships(
        val shows: ShowResponse? = null,
        val games: GameResponse? = null,
        val literatures: LiteratureResponse? = null
    )
}