package kurozorakit.data.repositories.store

import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.store.StoreTransactionResponse
import kurozorakit.data.models.user.receipt.ReceiptResponse
import kurozorakit.shared.Result

/**
 * Data repository responsible for managing in-app purchases, order restoration, and transaction history.
 */
interface StoreRepository {

    /**
     * Validates a transaction receipt from a store provider (`POST /store/verify`).
     *
     * @param receipt The base64-encoded receipt data provided by the billing system.
     * @param password Optional hexadecimal shared secret password.
     * @return A [Result] containing the [ReceiptResponse] with the verification status.
     */
    suspend fun verifyReceipt(receipt: String, password: String? = null): Result<ReceiptResponse>

    /**
     * Restores a previous store order by order ID (`POST /store/restore-order`).
     *
     * @param orderId The store order identifier.
     * @return A [Result] containing the [ReceiptResponse].
     */
    suspend fun restoreOrder(orderId: String): Result<ReceiptResponse>

    /**
     * Retrieves the authenticated user's store transaction history (`GET /store/transactions`).
     *
     * @return A [Result] containing the [StoreTransactionResponse].
     */
    suspend fun getTransactions(): Result<StoreTransactionResponse>

    /**
     * Deprecated: `store/purchases/google-play/verify` does not exist in the Kurozora API.
     */
    @Deprecated("Endpoint store/purchases/google-play/verify is not defined in Kurozora API.")
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

    override suspend fun verifyReceipt(receipt: String, password: String?): Result<ReceiptResponse> {
        return apiClient.post<ReceiptResponse, Unit>(KKEndpoint.Store.Verify) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("receipt", receipt)
                password?.let { append("password", it) }
            }))
        }
    }

    override suspend fun restoreOrder(orderId: String): Result<ReceiptResponse> {
        return apiClient.post<ReceiptResponse, Unit>(KKEndpoint.Store.RestoreOrder) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("orderID", orderId)
            }))
        }
    }

    override suspend fun getTransactions(): Result<StoreTransactionResponse> {
        return apiClient.get<StoreTransactionResponse>(KKEndpoint.Store.Transactions)
    }

    @Deprecated("Endpoint store/purchases/google-play/verify is not defined in Kurozora API.")
    @Suppress("DEPRECATION")
    override suspend fun verifyGooglePlayPurchase(purchaseToken: String, productId: String): Result<Map<String, String>> {
        val body = mapOf(
            "purchase_token" to purchaseToken,
            "product_id" to productId
        )
        return apiClient.post<Map<String, String>, Map<String, String>>(KKEndpoint.Store.GooglePlayVerify, body) {
            contentType(ContentType.Application.Json)
        }
    }
}
