package kurozorakit.data.models.user.achievement

import kotlinx.serialization.Serializable
import kurozorakit.data.models.media.Media

/**
 * Represents a platform-wide achievement earned by a user.
 *
 * Achievements serve as badges of honor for user milestones, such as
 * community contributions, collection milestones, or event participations.
 * They are designed to be visually distinct using custom color palettes.
 *
 * @property id The unique identifier for the achievement.
 * @property type The resource type, typically "achievements".
 * @property attributes The visual and descriptive data associated with the achievement.
 */
@Serializable
data class Achievement(
    val id: String,
    val type: String,
    var attributes: Attributes
) {
    /**
     * Visual and textual metadata for an [Achievement].
     *
     * @property name The display title of the achievement (e.g., "Manga Guru").
     * @property description A brief explanation of how the achievement was earned.
     * @property textColor The hex color code for the title text to ensure contrast.
     * @property backgroundColor The hex color code used for the badge background or border.
     * @property symbol A [Media] object (icon/image) representing the achievement's badge.
     */
    @Serializable
    data class Attributes(
        val name: String,
        val description: String,
        val textColor: String,
        val backgroundColor: String,
        val symbol: Media? = null
    )
}