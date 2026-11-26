package kurozorakit.data.models.editorial

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.models.IdentityResource

/**
 * Represents a curated editorial blurb for an Anime, Manga, or Game.
 *
 * Returned by `GET /anime|manga|games/{id}/editorial`.
 */
@Serializable
data class Editorial(
    override val id: String,
    override val type: String,
    override val href: String = "",
    val attributes: Attributes,
) : IdentityResource {
    @Serializable
    data class Attributes(
        val body: String,
        val byline: String? = null,
        @SerialName("publishedAt")
        val publishedAtTimestamp: Long? = null,
    ) {
        @Transient
        val publishedAt: Instant? = publishedAtTimestamp?.let { Instant.fromEpochSeconds(it) }
    }
}

@Serializable
data class EditorialResponse(
    val data: List<Editorial> = emptyList(),
)
