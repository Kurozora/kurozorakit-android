package kurozorakit.data.models.user.receipt

import kotlinx.serialization.Serializable

/**
 * A response container for a list of [Receipt] entities.
 *
 * This structure is typically returned when querying a user's purchase
 * history or validating multiple subscription tiers. It provides the
 * necessary data to sync the user's premium status across devices.
 *
 * @property data A collection of [Receipt] objects representing the
 * user's transaction or subscription records.
 */
@Serializable
data class ReceiptResponse(
    val data: List<Receipt>
)