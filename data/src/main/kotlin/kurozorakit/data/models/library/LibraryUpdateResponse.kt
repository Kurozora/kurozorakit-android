package kurozorakit.data.models.library

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.FavoriteStatus
import kurozorakit.data.enums.HiddenStatus
import kurozorakit.data.enums.KKLibrary
import kurozorakit.data.enums.ReminderStatus

/**
 * The standard API response envelope for a library mutation (`POST /me/library`,
 * `POST /me/library/update`, `POST /me/library/delete`).
 *
 * Matches Kurozora Web's nested structure:
 * `{ "data": { "attributes": { ... }, "relationships": { "libraries": [ ... ] } } }`
 *
 * @property data The confirmed mutation payload including [LibraryUpdateData.attributes]
 * and affected [LibrarySyncEntry] records.
 */
@Serializable
data class LibraryUpdateResponse(
    val data: LibraryUpdateData
)

/**
 * Data payload returned inside [LibraryUpdateResponse].
 *
 * Exposes convenience accessors delegating to [attributes] so callers can access
 * `response.data.status`, `response.data.favoriteStatus`, etc. directly as well as
 * `response.data.attributes`.
 */
@Serializable
data class LibraryUpdateData(
    val attributes: LibraryUpdate = LibraryUpdate(),
    val relationships: Relationships? = null,
) {
    val rating: Double? get() = attributes.rating
    val review: String? get() = attributes.review
    val isFavorited: Boolean? get() = attributes.isFavorited
    val isReminded: Boolean? get() = attributes.isReminded
    val isHidden: Boolean? get() = attributes.isHidden
    val status: KKLibrary.Status? get() = attributes.status
    val rewatchCount: Int? get() = attributes.rewatchCount
    val favoriteStatus: FavoriteStatus get() = attributes.favoriteStatus
    val reminderStatus: ReminderStatus get() = attributes.reminderStatus
    val hiddenStatus: HiddenStatus get() = attributes.hiddenStatus

    @Serializable
    data class Relationships(
        val libraries: List<LibrarySyncEntry> = emptyList(),
    )
}