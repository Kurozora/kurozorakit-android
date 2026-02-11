package kurozorakit.data.models.library

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.FavoriteStatus
import kurozorakit.data.enums.HiddenStatus
import kurozorakit.data.enums.KKLibrary
import kurozorakit.data.enums.ReminderStatus

/**
 * An alias representing a delta update for the library, streamlining naming conventions
 * for modification operations.
 */
typealias LibraryUpdate = LibraryAttributes

/**
 * Tracks a user's personal interaction, progress, and preferences for a media resource.
 *
 * This model is typically embedded within parent media objects (Shows, Games, Literatures).
 * It manages the transition of states—such as moving a title from "Planning" to "Completed"—and
 * persists user-specific metadata like personal ratings and private reviews.
 *
 * @property rating The user's personal score (Range: 0.0 to 10.0).
 * @property review The user's personal commentary or review text for the resource.
 * @property isFavorited Legacy boolean flag for the favorited state.
 * @property _favoriteStatus Private backing field for the explicit [FavoriteStatus] enum.
 * @property isReminded Legacy boolean flag for notification reminders.
 * @property _reminderStatus Private backing field for the explicit [ReminderStatus] enum.
 * @property isHidden Legacy boolean flag for visibility settings.
 * @property _hiddenStatus Private backing field for the explicit [HiddenStatus] enum.
 * @property status The user's current consumption status (e.g., CURRENT, COMPLETED, DROPPED).
 * @property rewatchCount The number of times the user has restarted or finished the media.
 */
@Serializable
data class LibraryAttributes(
    var rating: Double? = null,
    var review: String? = null,
    var isFavorited: Boolean? = null,
    private var _favoriteStatus: FavoriteStatus? = null,
    var isReminded: Boolean? = null,
    private var _reminderStatus: ReminderStatus? = null,
    var isHidden: Boolean? = null,
    private var _hiddenStatus: HiddenStatus? = null,
    var status: KKLibrary.Status? = null,
    var rewatchCount: Int? = null,
) {

    /**
     * The resolved favorite status.
     * Prioritizes the explicit [_favoriteStatus] enum, falling back to [isFavorited]
     * for legacy compatibility.
     */
    var favoriteStatus: FavoriteStatus
        get() = _favoriteStatus ?: FavoriteStatus.fromBoolean(isFavorited)
        set(value) { _favoriteStatus = value }

    /**
     * The resolved reminder status.
     * Prioritizes the explicit [_reminderStatus] enum, falling back to [isReminded].
     */
    var reminderStatus: ReminderStatus
        get() = _reminderStatus ?: ReminderStatus.fromBoolean(isReminded)
        set(value) { _reminderStatus = value }

    /**
     * The resolved hidden/privacy status.
     * Prioritizes the explicit [_hiddenStatus] enum, falling back to [isHidden].
     */
    var hiddenStatus: HiddenStatus
        get() = _hiddenStatus ?: HiddenStatus.fromBoolean(isHidden)
        set(value) { _hiddenStatus = value }

    /**
     * Performs an in-place mutation of the current state using values from a [LibraryUpdate].
     * Useful for updating an existing UI model without re-instantiation.
     * * @param libraryUpdate The delta containing the new attributes to apply.
     */
    fun update(libraryUpdate: LibraryUpdate) {
        favoriteStatus = libraryUpdate.favoriteStatus
        reminderStatus = libraryUpdate.reminderStatus
        status = libraryUpdate.status
    }

    /**
     * Returns a new instance of [LibraryAttributes] with the updated values applied.
     * This follows functional programming principles, making it ideal for state-driven
     * architectures like MVI or Compose.
     * * @param libraryUpdate The delta containing the new attributes.
     * @return A fresh copy of the attributes with updated state.
     */
    fun updated(libraryUpdate: LibraryUpdate): LibraryAttributes {
        return this.copy(
            _favoriteStatus = libraryUpdate.favoriteStatus,
            _reminderStatus = libraryUpdate.reminderStatus,
            status = libraryUpdate.status
        )
    }
}