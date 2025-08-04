package kurozora.kit.data.repositories.store

import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.user.User
import kurozora.kit.data.models.user.receipt.ReceiptResponse

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