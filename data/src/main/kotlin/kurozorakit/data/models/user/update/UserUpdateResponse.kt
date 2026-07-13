package kurozorakit.data.models.user.update

import kotlinx.serialization.Serializable

/**
 * A top-level data transfer object (DTO) returned following a user profile update request.
 *
 * This response confirms the outcome of the update operation, providing the finalized
 * state of the user's information and a status message for client-side feedback.
 *
 * @property data The updated [UserUpdate] object, reflecting the current state of the
 * user's profile after the changes have been applied.
 * @property message A human-readable status message indicating the result of the
 * operation (e.g., "Profile updated successfully").
 * * @see UserUpdate For the specific profile fields that can be modified.
 */
@Serializable
data class UserUpdateResponse(
    val data: UserUpdateResponseModel,
    val message: String
)

@Serializable
data class UserUpdateResponseModel(
    var username: String? = null,
    var nickname: String? = null,
    var biography: String? = null,
    var profileImageURL: String? = null,
    var bannerImageURL: String? = null,
    var preferredLanguage: String? = null,
    var preferredTVRating: Int? = null,
    var preferredTimezone: String? = null
)