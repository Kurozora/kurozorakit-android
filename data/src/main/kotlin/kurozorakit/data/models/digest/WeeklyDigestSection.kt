package kurozorakit.data.models.digest

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.show.ShowIdentityResponse

/**
 * Represents a single section of the user's weekly digest returned by
 * `GET /me/digest` or `GET /me/digest/{section}`.
 */
@Serializable
data class WeeklyDigestSection(
    override val id: String,
    override val type: String,
    override val href: String = "",
    val attributes: Attributes,
    val relationships: Relationships? = null,
) : IdentityResource {
    @Serializable
    data class Attributes(
        val kind: String,
        val title: String? = null,
        val subtitle: String? = null,
        val position: Int,
        val momentum: JsonObject? = null,
    )

    @Serializable
    data class Relationships(
        val shows: ShowIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val episodes: EpisodeIdentityResponse? = null,
        val people: PersonIdentityResponse? = null,
    )
}

@Serializable
data class WeeklyDigestSectionResponse(
    val data: List<WeeklyDigestSection> = emptyList(),
)
