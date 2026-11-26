package kurozorakit.data.models.person

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.models.media.MediaRelation

/**
 * Represents a personal or familial relationship between two [Person] entities (e.g., Spouse).
 *
 * Returned by `GET /people/{person}/relationships`.
 */
@Serializable
data class PersonRelationship(
    val person: Person,
    val attributes: Attributes,
) {
    @Serializable
    data class Attributes(
        val relation: MediaRelation,
        @SerialName("startedAt")
        val startedAtTimestamp: Long? = null,
        @SerialName("endedAt")
        val endedAtTimestamp: Long? = null,
    ) {
        @Transient
        val startedAt: Instant? = startedAtTimestamp?.let { Instant.fromEpochSeconds(it) }

        @Transient
        val endedAt: Instant? = endedAtTimestamp?.let { Instant.fromEpochSeconds(it) }
    }
}

@Serializable
data class PersonRelationshipResponse(
    val data: List<PersonRelationship> = emptyList(),
    val next: String? = null,
)
