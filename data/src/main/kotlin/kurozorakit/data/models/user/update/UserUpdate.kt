package kurozorakit.data.models.user.update

import kotlinx.serialization.Serializable
import kurozorakit.data.models.media.Media

@Serializable
data class UserUpdate(
    var username: String? = null,
    var nickname: String? = null,
    var biography: String? = null,
    var profile: Media? = null,
    var banner: Media? = null,
    var preferredLanguage: String? = null,
    var preferredTVRating: Int? = null,
    var preferredTimezone: String? = null
)