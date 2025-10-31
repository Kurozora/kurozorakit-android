package kurozorakit.data.repositories.store

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.Result
import kurozorakit.data.models.user.User
import kurozorakit.data.models.user.receipt.ReceiptResponse

interface StoreRepository {
    suspend fun verifyReceipt(receipt: String): Result<ReceiptResponse>
}

open class StoreRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : StoreRepository {

    override suspend fun verifyReceipt(receipt: String): Result<ReceiptResponse> {
        val body = mapOf("receipt" to receipt)
        val result = apiClient.post<ReceiptResponse, Map<String, String>>(KKEndpoint.Store.Verify, body)

        result.onSuccess { response ->
            response.data.firstOrNull()?.let { receipt ->
                User.current?.attributes?.updateSubscription(from = receipt)
            }
        }.onError { error ->
            println("Received validate receipt error: ${error.message}")
        }

        return result
    }
}