package kurozorakit.data.repositories.store

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.Result
import kurozorakit.data.models.user.receipt.ReceiptResponse

/**
 * Data repository responsible for managing in-app purchases and transaction validation.
 *
 * This repository handles the communication with the backend to ensure that
 * digital purchases (e.g., premium subscriptions, digital goods) are legitimate
 * and properly processed through the platform's verification system.
 */
interface StoreRepository {

    /**
     * Validates a transaction receipt from a store provider (e.g., Apple App Store, Google Play).
     *
     * This method sends the receipt data to the server to verify the authenticity
     * of the purchase and unlock the associated digital content or status for the user.
     *
     * @param receipt The raw receipt data or token provided by the store's billing system.
     * @return A [Result] containing the [ReceiptResponse] with the verification status.
     */
    suspend fun verifyReceipt(receipt: String): Result<ReceiptResponse>

    /**
     * Validates a Google Play purchase by sending the purchase token and product ID
     * to the server for verification.
     *
     * @param purchaseToken The purchase token from Google Play Billing.
     * @param productId The product ID of the purchased item.
     * @return A [Result] containing a map with the server response.
     */
    suspend fun verifyGooglePlayPurchase(purchaseToken: String, productId: String): Result<Map<String, String>>
}

/**
 * Implementation of [StoreRepository] that utilizes [KurozoraApiClient] to
 * validate store transactions and process digital receipts.
 *
 * @property apiClient The client used to communicate with the billing and verification endpoints.
 */
open class StoreRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : StoreRepository {

    override suspend fun verifyReceipt(receipt: String): Result<ReceiptResponse> {
        val body = mapOf("receipt" to receipt)
        val result = apiClient.post<ReceiptResponse, Map<String, String>>(KKEndpoint.Store.Verify, body)

        result.onSuccess { response ->
            response.data.firstOrNull()?.let { receipt ->
                //User.current?.attributes?.updateSubscription(from = receipt)
            }
        }.onError { error ->
            println("Received validate receipt error: ${error.message}")
        }

        return result
    }

    override suspend fun verifyGooglePlayPurchase(purchaseToken: String, productId: String): Result<Map<String, String>> {
        val body = mapOf(
            "purchase_token" to purchaseToken,
            "product_id" to productId
        )
        return apiClient.post<Map<String, String>, Map<String, String>>(KKEndpoint.Store.GooglePlayVerify, body)
    }
}