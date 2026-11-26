package kurozorakit.data.models.show.mapping

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * External service identifier mapping for an Anime returned by `GET /anime/mapping`.
 */
@Serializable
data class AnimeMapping(
    val slug: String,
    val myanimelist: Int? = null,
    val anilist: Int? = null,
    val kitsu: Int? = null,
    val anidb: Int? = null,
    @SerialName("anime_planet")
    val animePlanet: String? = null,
    val anisearch: Int? = null,
    val livechart: Int? = null,
    val notify: String? = null,
    val shoboi: Int? = null,
    val trakt: Int? = null,
    val thetvdb: Int? = null,
    val imdb: String? = null,
)

@Serializable
data class AnimeMappingResponse(
    val data: List<AnimeMapping> = emptyList(),
    val next: String? = null,
)
