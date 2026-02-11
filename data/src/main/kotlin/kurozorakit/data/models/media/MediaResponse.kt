package kurozorakit.data.models.media

import kotlinx.serialization.Serializable

/**
 * A root-level data wrapper for API responses containing a collection of [Media] resources.
 *
 * This model follows the standard JSON:API or similar resource-collection patterns,
 * ensuring that multiple media assets (such as a gallery of screenshots or a collection
 * of promotional banners) are delivered in a structured [data] array.
 *
 * @property data A list of [Media] objects, each representing an individual asset
 * with its own metadata, dimensions, and styling attributes.
 */
@Serializable
data class MediaResponse(
    val data: List<Media>,
)