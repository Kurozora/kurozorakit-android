package kurozorakit.data.models.person

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.WebsiteURLsAsListSerializer
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaStat
import kurozorakit.data.models.show.ShowIdentityResponse

/**
 * Represents a real-world individual involved in the creation of media within the Kurozora database.
 * * This includes creators such as voice actors (Seiyuu), authors, illustrators, directors,
 * and other staff members. The model provides biographical data, social links, and
 * their professional portfolio across different media types.
 *
 * @property id The unique identifier for the person.
 * @property type The resource type, typically "people".
 * @property href The canonical API link to this person's full profile.
 * @property attributes Biographical details and localized metadata for the person.
 * @property relationships Connections to the characters they play or works they have contributed to.
 * @see IdentityResource
 */
@Serializable
data class Person(
    override val id: String,
    override val type: String = "people",
    override val href: String = "",
    val attributes: Attributes,
    val relationships: Relationships? = null
) : IdentityResource {

    /**
     * Biographical and professional metadata for a [Person].
     *
     * @property slug A URL-friendly identifier based on the person's name.
     * @property profile The primary portrait or profile image of the individual.
     * @property fullName The primary display name (usually in the user's localized format).
     * @property fullGivenName The original name in its native script/order (e.g., Japanese name order).
     * @property alternativeNames A list of aliases, stage names, or nicknames.
     * @property age The calculated age of the person as a displayable string.
     * @property birthdateTimestamp Unix epoch seconds representing the date of birth.
     * @property deceasedDateTimestamp Unix epoch seconds representing the date of passing (if applicable).
     * @property about A detailed biography or career overview.
     * @property shortDescription A concise summary of who the person is (e.g., "Japanese Voice Actress").
     * @property websiteURLs A list of official social media or personal website links (X, Instagram, etc.).
     * @property astrologicalSign The zodiac sign of the person based on their birthdate.
     * @property stats Analytical engagement data like popularity and ranking.
     * @property givenRating The rating assigned to this person by the currently authenticated user.
     * @property givenReview The written review or notes provided for this person by the authenticated user.
     */
    @Serializable
    data class Attributes(
        val slug: String = "",
        val profile: Media? = null,
        val fullName: String = "",
        val fullGivenName: String? = null,
        val alternativeNames: List<String>? = null,
        val age: String? = null,
        @SerialName("birthdate")
        val birthdateTimestamp: Long? = null,
        @SerialName("deceasedDate")
        val deceasedDateTimestamp: Long? = null,
        val about: String? = null,
        val shortDescription: String? = null,
        @Serializable(with = WebsiteURLsAsListSerializer::class)
        val websiteURLs: List<String>? = null,
        val astrologicalSign: String? = null,
        val stats: MediaStat? = null,

        // Authenticated Attributes
        var givenRating: Double? = null,
        var givenReview: String? = null
    ) {
        /**
         * The birthdate converted to a [Instant] object for easier date manipulation in Kotlin.
         * Derived from [birthdateTimestamp].
         */
        @Transient
        val birthdate: Instant? = birthdateTimestamp?.let { Instant.fromEpochSeconds(it) }

        /**
         * The date of death converted to a [Instant] object.
         * Derived from [deceasedDateTimestamp].
         */
        @Transient
        val deceasedDate: Instant? = deceasedDateTimestamp?.let { Instant.fromEpochSeconds(it) }
    }

    /**
     * Defines the professional portfolio and relations of the [Person].
     *
     * @property characters A list of [CharacterIdentityResponse] representing roles the person has voiced or acted.
     * @property shows The anime or TV series the person has worked on.
     * @property games Video games where the person is credited (e.g., voice work or development).
     * @property literatures Books or manga written or illustrated by this person.
     */
    @Serializable
    data class Relationships(
        val characters: CharacterIdentityResponse? = null,
        val shows: ShowIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null
    )
}