package kurozorakit.data.models.library

import kotlinx.serialization.Serializable

/**
 * The standard API response envelope for a library mutation.
 *
 * After a client sends a [LibraryUpdate] (patch) to the server, this model is
 * returned to confirm the success of the operation. It contains the updated
 * [LibraryAttributes], reflecting any server-side transformations or validations
 * (e.g., ensuring a rating is within the 0.0-10.0 range).
 *
 * @property data The confirmed [LibraryUpdate] (LibraryAttributes) state.
 */
@Serializable
data class LibraryUpdateResponse(
    val data: LibraryUpdate
)