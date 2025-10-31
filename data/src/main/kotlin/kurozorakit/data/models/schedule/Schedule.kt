package kurozorakit.data.models.schedule

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozorakit.data.models.LocalDateSerializer
import kurozorakit.data.models.game.GameResponse
import kurozorakit.data.models.literature.LiteratureResponse
import kurozorakit.data.models.show.ShowResponse

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