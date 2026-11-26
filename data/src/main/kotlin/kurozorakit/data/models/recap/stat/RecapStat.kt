package kurozorakit.data.models.recap.stat

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.enums.RecapStatType
import kurozorakit.data.models.provider.ProviderResponse
import kurozorakit.data.models.show.ShowIdentityResponse

/**
 * Represents a highlight statistic for a periodic Re:CAP returned by
 * `GET /me/recap/{year}/{month}/stats`.
 */
@Serializable
data class RecapStat(
    val id: String,
    val type: String,
    val attributes: Attributes,
    val relationships: Relationships? = null,
) {
    @Serializable
    data class Attributes(
        val year: Int,
        val month: Int,
        val stat: RecapStatType,
        val value: Int,
        @SerialName("occurredAt")
        val occurredAtTimestamp: Long? = null,
    ) {
        @Transient
        val occurredAt: Instant? = occurredAtTimestamp?.let { Instant.fromEpochSeconds(it) }
    }

    @Serializable
    data class Relationships(
        val shows: ShowIdentityResponse? = null,
        val providers: ProviderResponse? = null,
    )
}

@Serializable
data class RecapStatResponse(
    val data: List<RecapStat> = emptyList(),
)
