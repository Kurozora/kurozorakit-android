package kurozora.kit.data.models.user.update

import kotlinx.serialization.Serializable
import kurozora.kit.data.models.media.Media

@Serializable
data class UserUpdate(
    var username: String,
    var nickname: String,
    var biography: String? = null,
    var profile: Media? = null,
    var banner: Media? = null,
    var preferredLanguage: String,
    var preferredTVRating: Int,
    var preferredTimezone: String
)