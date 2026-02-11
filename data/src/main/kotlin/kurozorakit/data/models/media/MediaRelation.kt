package kurozorakit.data.models.media

import kotlinx.serialization.Serializable

/**
 * Represents the semantic nature of a relationship between two media resources.
 *
 * This model defines how one resource relates to another within the Kurozora database,
 * providing context for the connection (e.g., whether a show is a "Sequel" to a game
 * or a "Side Story" to a literature work).
 *
 * @property name The short, machine-readable or display-friendly name of the relation.
 * Examples: "Sequel", "Prequel", "Alternative Version", "Adaptation", "Spin-off".
 * @property description A localized, human-readable explanation of the relationship.
 * This provides more context, such as "This series is a direct sequel to the 2018 adaptation."
 */
@Serializable
data class MediaRelation(
    val name: String,
    val description: String,
)