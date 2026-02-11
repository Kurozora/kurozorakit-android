package kurozorakit.data.enums

/**
 * Defines the direction of social relationships for a specific user profile.
 *
 * This enum is used to filter and display lists of users associated with a profile,
 * determining whether to show those who look up to the user or those the user
 * is interested in.
 */
enum class UsersListType {
    /** Users who have subscribed to the current user's updates. */
    followers,

    /** Users whom the current user has subscribed to. */
    following;

    /**
     * The human-readable label used for tab titles and headers in the UI.
     */
    val stringValue: String
        get() = when (this) {
            followers -> "Followers"
            following -> "Following"
        }
}