package kurozora.kit.data.models.media

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.episode.EpisodeIdentityResponse
import kurozora.kit.data.models.game.GameIdentityResponse
import kurozora.kit.data.models.literature.LiteratureIdentityResponse
import kurozora.kit.data.models.show.ShowIdentityResponse

@Serializable
data class Media(
    val url: String,
    val height: Int? = null,
    val width: Int? = null,
    val backgroundColor: String? = null,
    val textColor1: String? = null,
    val textColor2: String? = null,
    val textColor3: String? = null,
    val textColor4: String? = null,
    val relationships: Relationships? = null,
) {
    @Serializable
    data class Relationships(
        val episodes: EpisodeIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null,
        val shows: ShowIdentityResponse? = null,
    )
}
