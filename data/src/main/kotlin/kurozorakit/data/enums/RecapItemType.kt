package kurozorakit.data.enums

/**
 * Defines the categories of data displayed within a user's activity recap.
 *
 * This enum is used to aggregate statistics for seasonal or annual summaries,
 * helping users visualize their consumption habits across different media types
 * and their most-interacted-with metadata (Genres and Themes).
 */
enum class RecapItemType {
    /** Summary data for animated series and movies. */
    SHOWS,

    /** Summary data for interactive titles and gaming sessions. */
    GAMES,

    /** Summary data for reading habits, including manga and novels. */
    LITERATURES,

    /** * The most frequent categories associated with the media consumed
     * (e.g., Action, Romance, Sci-Fi).
     */
    GENRES,

    /** * The most frequent narrative elements or motifs found in the media
     * consumed (e.g., High School, Time Travel, Gore).
     */
    THEMES
}