package kurozora.kit.data.models.person

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.character.CharacterIdentityResponse
import kurozora.kit.data.models.game.GameIdentityResponse
import kurozora.kit.data.models.literature.LiteratureIdentityResponse
import kurozora.kit.data.models.media.Media
import kurozora.kit.data.models.media.MediaStat
import kurozora.kit.data.models.show.ShowIdentityResponse

@Serializable
data class Person(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null
) : IdentityResource {

    @Serializable
    data class Attributes(
        val slug: String,
        val profile: Media? = null,
        val fullName: String,
        val fullGivenName: String? = null,
        val alternativeNames: List<String>? = null,
        val age: String? = null,
        @SerialName("birthdate")
        val birthdateTimestamp: Long? = null,
        @SerialName("deceasedDate")
        val deceasedDateTimestamp: Long? = null,
        val about: String? = null,
        val shortDescription: String? = null,
        val websiteURLs: List<String>? = null,
        val astrologicalSign: String? = null,
        val stats: MediaStat? = null,
        // Authenticated Attributes
        var givenRating: Double? = null,
        var givenReview: String? = null
    ) {
        @Transient
        val birthdate: Instant? = birthdateTimestamp?.let { Instant.fromEpochSeconds(it) }
        @Transient
        val deceasedDate: Instant? = deceasedDateTimestamp?.let { Instant.fromEpochSeconds(it) }
    }

    @Serializable
    data class Relationships(
        val characters: CharacterIdentityResponse? = null,
        val shows: ShowIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null
    )
}
