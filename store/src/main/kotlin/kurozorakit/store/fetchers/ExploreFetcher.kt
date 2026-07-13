package kurozorakit.store.fetcher

import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.models.explore.ExploreCategory
import kurozorakit.data.models.explore.ExploreCategoryResponse
import kurozorakit.shared.Result
import kurozorakit.shared.toException
import kurozorakit.store.ExploreKey
import org.mobilenativefoundation.store.store5.Fetcher

class ExploreFetcher(private val apiClient: KurozoraApiClient) {

    /** Ana keşfet akışını getirir */
    fun forAll(): Fetcher<ExploreKey.All, List<ExploreCategory>> = Fetcher.of {
        val result = apiClient.get<ExploreCategoryResponse>(KKEndpoint.Explore.Index)
        when (result) {
            is Result.Success -> result.data.data
            is Result.Error -> throw result.error.toException()
        }
    }

    /** Tekil kategori (örn: See More tıklandığında) */
    fun forSingle(): Fetcher<ExploreKey.ById, ExploreCategory> = Fetcher.of { key ->
        val result = apiClient.get<ExploreCategoryResponse>(KKEndpoint.Explore.Details(key.id))
        when (result) {
            is Result.Success -> result.data.data.firstOrNull() ?: throw NoSuchElementException()
            is Result.Error -> throw result.error.toException()
        }
    }
}