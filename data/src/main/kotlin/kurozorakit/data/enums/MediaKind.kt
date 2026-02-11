package kurozorakit.data.enums

/**
 * Defines the fundamental categories of media entities within the Kurozora ecosystem.
 *
 * This enum acts as a high-level discriminator. It is used to determine which
 * primary database table to query and which specific model structure to
 * expect during deserialization.
 */
enum class MediaKind {
    /** Individual segments of a series, including specials and OVAs. */
    episodes,

    /** Interactive entertainment, including full games, DLCs, and mods. */
    games,

    /** Written works, including Manga, Light Novels, and Manhwa. */
    literatures,

    /** Animated series or movies. */
    shows;

    /**
     * The raw string identifier used for API routing and query parameters.
     */
    val stringValue: String
        get() = when (this) {
            episodes -> "episodes"
            games -> "games"
            literatures -> "literatures"
            shows -> "shows"
        }
}