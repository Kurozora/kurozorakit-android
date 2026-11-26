package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Identifies the target entity kind for favorites (expanding beyond library-trackable items
 * to also cover characters, people, studios, and songs).
 */
@Serializable(with = FavoriteKindSerializer::class)
enum class FavoriteKind(val rawValue: Int) {
    anime(0),
    manga(1),
    game(2),
    character(3),
    person(4),
    studio(5),
    song(6);

    /** Whether this kind requires an existing library entry before favoriting. */
    val isLibraryTrackable: Boolean
        get() = this == anime || this == manga || this == game

    companion object {
        fun fromInt(value: Int): FavoriteKind? = entries.find { it.rawValue == value }
    }
}

object FavoriteKindSerializer : IntValueEnumSerializer<FavoriteKind>(
    serialName = "FavoriteKind",
    entries = FavoriteKind.entries,
    valueSelector = { it.rawValue },
    fallback = FavoriteKind.anime,
)
