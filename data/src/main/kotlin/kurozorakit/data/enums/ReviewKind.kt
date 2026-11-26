package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Identifies the target entity kind when querying or filtering reviews and rating categories.
 */
@Serializable(with = ReviewKindSerializer::class)
enum class ReviewKind(val rawValue: Int) {
    anime(0),
    manga(1),
    game(2),
    character(3),
    person(4),
    studio(5),
    song(6),
    episode(7);

    companion object {
        fun fromInt(value: Int): ReviewKind? = entries.find { it.rawValue == value }
    }
}

object ReviewKindSerializer : IntValueEnumSerializer<ReviewKind>(
    serialName = "ReviewKind",
    entries = ReviewKind.entries,
    valueSelector = { it.rawValue },
    fallback = ReviewKind.anime,
)
