package kurozorakit.data.models.explore

import kotlinx.serialization.Serializable

/**
 * A container for a collection of full [ExploreCategory] objects.
 *
 * This response model is typically used by the primary discovery endpoints to
 * deliver a complete set of UI sections. Each [ExploreCategory] within the
 * [data] list contains both the visual configuration (size, title, position)
 * and the media pointers (shows, games, etc.) needed to populate the screen.
 *
 * @property data A list of [ExploreCategory] resources that define the
 * content and layout of the current discovery view.
 */
@Serializable
data class ExploreCategoryResponse(
    val data: List<ExploreCategory>,
)