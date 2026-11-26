package kurozorakit.data.models.review

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource

/**
 * Represents a weighted category used in detailed multi-axis reviews (e.g., Story, Animation, Sound).
 *
 * Returned by `GET /reviews/categories`.
 */
@Serializable
data class RatingCategory(
    override val id: String,
    override val type: String,
    override val href: String = "",
    val attributes: Attributes,
) : IdentityResource {
    @Serializable
    data class Attributes(
        val slug: String,
        val name: String,
        val description: String? = null,
        val weight: Double,
        val displayOrder: Int,
        val score: Double? = null,
        val review: String? = null,
    )
}

@Serializable
data class RatingCategoryResponse(
    val data: List<RatingCategory> = emptyList(),
)
