package kurozorakit.data.models.character

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaStat
import kurozorakit.data.models.show.ShowIdentityResponse

/**
 * Represents a fictional entity (Character) within the Kurozora database.
 *
 * Characters serve as the primary junction between various media formats. This model
 * aggregates biographical data, physical statistics, and cross-media appearances,
 * effectively linking a single character concept to its voice actors (People)
 * and its presence in Anime, Manga, or Games.
 *
 * @property id The unique identifier for the character resource.
 * @property type The resource type identifier, explicitly "characters".
 * @property href The direct API link to fetch this character's full profile.
 * @property attributes The biographical, physical, and user-personalized data associated with the character.
 * @property relationships Pointers to the media franchises and staff associated with this character.
 */
@Serializable
data class Character(
    override val id: String,
    override val type: String = "characters",
    override val href: String = "",
    val attributes: Attributes,
    val relationships: Relationships? = null
) : IdentityResource {

    /**
     * Encapsulates the character's biographical profile and physical statistics.
     *
     * This class includes traditional metadata (blood type, birthdate) and physical
     * dimensions common in media databases, alongside user-generated content like
     * personal ratings and reviews.
     *
     * @property slug A URL-friendly unique identifier for the character.
     * @property profile [Media] object containing image assets for the character's avatar/profile.
     * @property name The primary display name of the character.
     * @property about A comprehensive biography, backstory, or character summary.
     * @property shortDescription A brief tagline or introductory text, ideal for list views.
     * @property debut The specific episode, chapter, or game level where the character first appears.
     * @property status The current narrative state of the character (e.g., "Alive", "Deceased").
     * @property bloodType Character's blood type (common in Japanese media context, e.g., "Type B").
     * @property favoriteFood Preferred food or drink items associated with the character.
     * @property bustSize Measurement of the character's bust (in cm).
     * @property waistSize Measurement of the character's waist (in cm).
     * @property hipSize Measurement of the character's hip (in cm).
     * @property height The character's height as a string (to support units like "175cm" or "5'9").
     * @property weight The character's weight as a string (to support units like "60kg" or "132lbs").
     * @property age The character's age, stored as a string to accommodate non-numeric values (e.g., "Unknown", "Centuries old").
     * @property birthdate The character's birthday (typically formatted as MM/DD or a specific date).
     * @property astrologicalSign The Western or Eastern zodiac sign associated with the character's birthdate.
     * @property stats Community-driven metrics such as popularity rankings and total "Favorites".
     * @property givenRating **[Mutable]** The numeric score (1.0 - 10.0) assigned to this character by the authenticated user.
     * @property givenReview **[Mutable]** A private note or public review text written by the authenticated user.
     */
    @Serializable
    data class Attributes(
        val slug: String = "",
        val profile: Media? = null,
        val name: String = "",
        val nicknames: List<String>? = null,
        val about: String? = null,
        val shortDescription: String? = null,
        val debut: String? = null,
        val status: String? = null,
        val bloodType: String? = null,
        val favoriteFood: String? = null,
        @SerialName("bust")
        val bustSize: Double? = null,
        @SerialName("waist")
        val waistSize: Double? = null,
        @SerialName("hip")
        val hipSize: Double? = null,
        val height: String? = null,
        val weight: String? = null,
        val age: String? = null,
        val birthdate: String? = null,
        val astrologicalSign: String? = null,
        val stats: MediaStat? = null,
        var givenRating: Double? = null,
        var givenReview: String? = null
    )

    /**
     * Defines the relational connections between a [Character] and other database entities.
     *
     * These relationships allow for deep navigation, linking a character profile to the
     * voice actors (Seiyuu) who portray them or the specific media entries (Shows, Games, Literatures)
     * where they are featured.
     *
     * @property people [PersonIdentityResponse] containing the voice actors and staff linked to this character.
     * @property shows [ShowIdentityResponse] containing the animated series or films featuring this character.
     * @property games [GameIdentityResponse] containing video games where this character is present.
     * @property literatures [LiteratureIdentityResponse] containing manga, light novels, or books featuring this character.
     */
    @Serializable
    data class Relationships(
        val people: PersonIdentityResponse? = null,
        val shows: ShowIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null
    )
}