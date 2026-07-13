package kurozorakit.store.stores

import kurozorakit.store.stores.StoreData.*
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin

open class KKStore() {
    // ==========================================================================
    // HELPER FUNCTIONS
    // ==========================================================================

    fun <T> mapStoreResponse(response: StoreReadResponse<T>): StoreData<T> {
        return when (response) {
            is StoreReadResponse.Initial -> StoreData.Loading // TODO yerine güvenli bir varsayılan
            is StoreReadResponse.Loading -> StoreData.Loading
            is StoreReadResponse.NoNewData -> StoreData.NoNewData

            is StoreReadResponse.Data -> Data(
                value = response.value,
                origin = when (response.origin) {
                    is StoreReadResponseOrigin.Cache -> DataOrigin.MEMORY_CACHE
                    is StoreReadResponseOrigin.SourceOfTruth -> DataOrigin.DATABASE
                    is StoreReadResponseOrigin.Fetcher -> DataOrigin.NETWORK // Hata buradaydı, düzeltildi.
                    else -> DataOrigin.UNKNOWN
                }
            )

            is StoreReadResponse.Error.Exception -> Error(
                message = response.error.message ?: "Unknown error",
                cause = response.error
            )
            is StoreReadResponse.Error.Message -> Error(
                message = response.message,
                cause = null
            )
            is StoreReadResponse.Error.Custom<*> -> Error(
                message = "Custom store error occurred",
                cause = null
            )
        }
    }
}

/**
 * Sealed class representing the state of data from the Store.
 */
sealed class StoreData<out T> {
    data object Loading : StoreData<Nothing>()

    data class Data<T>(
        val value: T,
        val origin: DataOrigin
    ) : StoreData<T>()

    data class Error(
        val message: String,
        val cause: Throwable?
    ) : StoreData<Nothing>()

    data object NoNewData : StoreData<Nothing>()

    val isData: Boolean get() = this is Data

    fun dataOrNull(): T? = (this as? Data<T>)?.value
}

/**
 * Enum indicating where the data originated from.
 */
enum class DataOrigin {
    MEMORY_CACHE,
    DATABASE,
    NETWORK,
    UNKNOWN
}