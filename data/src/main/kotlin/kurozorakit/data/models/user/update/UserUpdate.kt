package kurozorakit.data.models.user.update

import kotlinx.serialization.Serializable
import kurozorakit.data.models.media.Media

/**
 * A data transfer object (DTO) used to submit updates to a user's profile and preferences.
 *
 * This model contains mutable fields representing the various aspects of a user account
 * that can be modified. All properties are optional (nullable), allowing for partial
 * updates where only changed fields are sent to the server.
 *
 * @property username The unique handle for the user. Note that changing this may affect profile URLs.
 * @property nickname The display name shown to other users, which does not have to be unique.
 * @property biography A short personal description or "about me" text provided by the user.
 * @property profile A [Media] object representing the user's avatar or profile picture.
 * @property banner A [Media] object representing the background header image on the user's profile.
 * @property preferredLanguage The ISO language code for the user's UI (e.g., "en", "tr").
 * @property preferredTVRating The age-rating threshold for content filtering (e.g., 13 for PG-13).
 * @property preferredTimezone The IANA timezone database name (e.g., "UTC", "Europe/Istanbul").
 */
@Serializable
data class UserUpdate(
    var username: String? = null,
    var nickname: String? = null,
    var biography: String? = null,
    var profile: ByteArray? = null,
    var banner: ByteArray? = null,
    var preferredLanguage: String? = null,
    var preferredTVRating: Int? = null,
    var preferredTimezone: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserUpdate

        if (preferredTVRating != other.preferredTVRating) return false
        if (username != other.username) return false
        if (nickname != other.nickname) return false
        if (biography != other.biography) return false
        if (!profile.contentEquals(other.profile)) return false
        if (!banner.contentEquals(other.banner)) return false
        if (preferredLanguage != other.preferredLanguage) return false
        if (preferredTimezone != other.preferredTimezone) return false

        return true
    }

    override fun hashCode(): Int {
        var result = preferredTVRating ?: 0
        result = 31 * result + (username?.hashCode() ?: 0)
        result = 31 * result + (nickname?.hashCode() ?: 0)
        result = 31 * result + (biography?.hashCode() ?: 0)
        result = 31 * result + (profile?.contentHashCode() ?: 0)
        result = 31 * result + (banner?.contentHashCode() ?: 0)
        result = 31 * result + (preferredLanguage?.hashCode() ?: 0)
        result = 31 * result + (preferredTimezone?.hashCode() ?: 0)
        return result
    }
}