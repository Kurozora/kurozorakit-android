package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

@Serializable
data class StudioFilter(
    val type: String? = null,
    val tvRating: String? = null,
    val address: String? = null,
    val foundedAt: Long? = null,
    val defunctAt: Long? = null,
    val isNSFW: Boolean? = null
) : Filterable {
    override fun toFilterMap() = mapOf(
        "type" to type,
        "tv_rating_id" to tvRating,
        "address" to address,
        "founded_at" to foundedAt,
        "defunct_at" to defunctAt,
        "is_nsfw" to isNSFW
    ).filterValues { it != null }
}