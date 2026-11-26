package kurozorakit.data.models.museum

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.media.Media

/**
 * Represents a yearly aggregate entry returned by museum overview endpoints
 * (`GET /anime/museum`, `GET /manga/museum`, `GET /games/museum`).
 *
 * @property year Calendar release year.
 * @property count Number of catalog entries released in [year].
 */
@Serializable
data class MuseumYear(
    val year: Int = 0,
    val count: Int = 0,
)

/**
 * Response container for museum overview endpoints.
 *
 * @property data List of yearly catalog counts.
 * @property next Optional pagination cursor for subsequent pages.
 */
@Serializable
data class MuseumYearResponse(
    val data: List<MuseumYear> = emptyList(),
    val next: String? = null,
)

/**
 * Represents a single catalog item returned by year-scoped museum endpoints
 * (`GET /anime/museum/{year}`, `GET /manga/museum/{year}`, `GET /games/museum/{year}`).
 */
@Serializable
data class MuseumItem(
    override val id: String,
    override val type: String = "",
    override val href: String = "",
    val attributes: Attributes = Attributes(),
) : IdentityResource {
    @Serializable
    data class Attributes(
        val title: String = "",
        val poster: Media? = null,
    )
}

/**
 * Paginated response container for year-scoped museum items.
 */
@Serializable
data class MuseumItemResponse(
    val data: List<MuseumItem> = emptyList(),
    val next: String? = null,
)
