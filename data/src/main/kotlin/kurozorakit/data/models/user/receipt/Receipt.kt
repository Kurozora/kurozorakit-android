package kurozorakit.data.models.user.receipt

import kotlinx.serialization.Serializable

/**
 * Represents a validation receipt for user subscriptions or in-app purchases.
 *
 * This model is used to synchronize the local user state with their
 * subscription status on the server. It determines whether a user currently
 * has access to premium features (Pro) based on the validity of their purchase.
 *
 * @property type The resource type, typically "receipts".
 * @property attributes The validation status and lifecycle metadata of the receipt.
 */
@Serializable
data class Receipt(
    val type: String,
    var attributes: Attributes
) {
    /**
     * Metadata regarding the validity of a [Receipt].
     *
     * @property isValid Indicates if the subscription or purchase is currently active
     * and authorized. If true, the user is granted 'Pro' status.
     * @property needsRefresh Indicates if the receipt data is stale and requires
     * re-validation with the app store (App Store/Play Store) or the backend.
     */
    @Serializable
    data class Attributes(
        val isValid: Boolean,
        val needsRefresh: Boolean
    )
}