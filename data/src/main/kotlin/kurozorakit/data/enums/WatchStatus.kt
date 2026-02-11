package kurozorakit.data.enums

/**
 * Represents the consumption state of a specific piece of media content (e.g., an Episode).
 *
 * This enum is the core of the progress tracking system. It allows the UI to
 * visually distinguish between content the user has finished, content remaining
 * in their queue, and content that cannot be interacted with yet.
 *
 * @property value The unique integer identifier for the watch state.
 */
enum class WatchStatus(val value: Int) {
    /** The user has access to the content but has not yet marked it as seen. */
    notWatched(-1),

    /** * The tracking feature is unavailable for this item.
     * Often used for unreleased episodes or content restricted by age ratings.
     */
    disabled(0),

    /** The user has successfully completed viewing the content. */
    watched(1);

    /**
     * The human-readable label for the consumption state.
     */
    val stringValue: String
        get() = when (this) {
            notWatched -> "Not Watched"
            disabled -> "Disabled"
            watched -> "Watched"
        }

    companion object {
        /**
         * Maps a nullable [Boolean] flag (often from a database or API) to a [WatchStatus].
         *
         * - `true`  -> [watched]
         * - `false` -> [notWatched]
         * - `null`  -> [disabled]
         */
        fun fromBoolean(bool: Boolean?): WatchStatus {
            return when (bool) {
                true -> watched
                false -> notWatched
                null -> disabled
            }
        }
    }
}