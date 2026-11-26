package kurozorakit.data.models.recap

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.media.Media

/**
 * Represents a periodic summary or "Recap" of a user's activity within the Kurozora ecosystem.
 *
 * Recaps are generated to give users insights into their media consumption (shows, games, literature)
 * over a specific timeframe, typically a month or a year. This model provides both the temporal
 * data and the necessary assets to render a visually appealing summary screen.
 *
 * @property id The unique identifier for the recap resource.
 * @property type The resource type designation, typically "recaps".
 * @property href The canonical API link to access this specific recap's detailed data.
 * @property attributes The descriptive and aesthetic properties of the recap.
 * @see IdentityResource
 */
@Serializable
data class Recap(
    override val id: String,
    override val type: String = "recaps",
    override val href: String = "",
    val attributes: Attributes
) : IdentityResource {

    /**
     * Contains the temporal metadata and visual styling properties for a [Recap].
     *
     * @property year The calendar year the recap belongs to (e.g., 2024).
     * @property month The calendar month of the recap (1 for January, 12 for December).
     * @property description A localized summary or highlight text describing the user's achievements.
     * @property backgroundColor1 The primary hex color code for the recap UI background.
     * @property backgroundColor2 The secondary hex color code, intended to be used with [backgroundColor1]
     * to create a gradient effect in the UI.
     * @property artwork A [Media] object (image or illustration) that serves as the visual centerpiece
     * for this recap period.
     */
    @Serializable
    data class Attributes(
        val year: Int = 0,
        val month: Int = 0,
        val description: String? = null,
        val backgroundColor1: String = "",
        val backgroundColor2: String = "",
        val artwork: Media? = null
    )
}