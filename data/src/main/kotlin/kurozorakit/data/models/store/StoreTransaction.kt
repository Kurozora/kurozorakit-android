package kurozorakit.data.models.store

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.enums.StoreProductType

/**
 * Represents a verified store purchase transaction returned by `GET /store/transactions`.
 */
@Serializable
data class StoreTransaction(
    val id: String,
    val type: String,
    val attributes: Attributes,
) {
    @Serializable
    data class Attributes(
        val transactionID: String,
        val productType: StoreProductType? = null,
        val productID: String,
        val priceMilliunits: Long? = null,
        val currency: String? = null,
        @SerialName("purchasedAt")
        val purchasedAtTimestamp: Long? = null,
        @SerialName("expiresAt")
        val expiresAtTimestamp: Long? = null,
        @SerialName("revokedAt")
        val revokedAtTimestamp: Long? = null,
        val isRefundable: Boolean = false,
    ) {
        @Transient
        val purchasedAt: Instant? = purchasedAtTimestamp?.let { Instant.fromEpochSeconds(it) }

        @Transient
        val expiresAt: Instant? = expiresAtTimestamp?.let { Instant.fromEpochSeconds(it) }

        @Transient
        val revokedAt: Instant? = revokedAtTimestamp?.let { Instant.fromEpochSeconds(it) }
    }
}

@Serializable
data class StoreTransactionResponse(
    val data: List<StoreTransaction> = emptyList(),
    val next: String? = null,
)
