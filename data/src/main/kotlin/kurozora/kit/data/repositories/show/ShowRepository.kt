package kurozora.kit.data.repositories.show

import kotlinx.serialization.json.Json
import kurozora.kit.api.KKEndpoint
import kurozora.kit.api.KurozoraApiClient
import kurozora.kit.shared.Result
import kurozora.kit.data.models.character.Character
import kurozora.kit.data.models.character.CharacterIdentityResponse
import kurozora.kit.data.models.character.CharacterResponse
import kurozora.kit.data.models.person.Person
import kurozora.kit.data.models.person.PersonIdentityResponse
import kurozora.kit.data.models.person.PersonResponse
import kurozora.kit.data.models.review.Review
import kurozora.kit.data.models.review.ReviewResponse
import kurozora.kit.data.models.search.filters.ShowFilter
import kurozora.kit.data.models.season.Season
import kurozora.kit.data.models.season.SeasonIdentityResponse
import kurozora.kit.data.models.season.SeasonResponse
import kurozora.kit.data.models.show.Show
import kurozora.kit.data.models.show.ShowIdentity
import kurozora.kit.data.models.show.ShowIdentityResponse
import kurozora.kit.data.models.show.ShowResponse
import kurozora.kit.data.models.show.cast.CastIdentity
import kurozora.kit.data.models.show.cast.CastIdentityResponse
import kurozora.kit.data.models.show.related.RelatedGame
import kurozora.kit.data.models.show.related.RelatedGameResponse
import kurozora.kit.data.models.show.related.RelatedLiterature
import kurozora.kit.data.models.show.related.RelatedLiteratureResponse
import kurozora.kit.data.models.show.related.RelatedShow
import kurozora.kit.data.models.show.related.RelatedShowResponse
import kurozora.kit.data.models.show.song.ShowSong
import kurozora.kit.data.models.show.song.ShowSongResponse
import kurozora.kit.data.models.studio.Studio
import kurozora.kit.data.models.studio.StudioIdentityResponse
import java.util.Base64

interface ShowRepository {
    // Basic operations
    suspend fun getShows(next: String? = null, limit: Int = 20, filter: ShowFilter? = null): Result<List<ShowIdentity>>
    suspend fun getShows(ids: List<String>, relationships: List<String> = emptyList()): Result<List<ShowIdentity>>
    suspend fun getShow(id: String, relationships: List<String> = emptyList()): Result<Show>
    suspend fun getUpcomingShows(next: String? = null, limit: Int = 20): Result<List<ShowIdentity>>
    // Related content
    suspend fun getShowCast(showId: String, next: String? = null, limit: Int = 20): Result<List<Person>>
    suspend fun getShowCharacters(showId: String, next: String? = null, limit: Int = 20): Result<List<Character>>
    suspend fun getShowPeople(showId: String, next: String? = null, limit: Int = 20): Result<List<Person>>
    suspend fun getShowReviews(showId: String, next: String? = null, limit: Int = 20): Result<List<Review>>
    suspend fun getShowSeasons(showId: String, next: String? = null, limit: Int = 20): Result<List<Season>>
    suspend fun getShowSongs(showId: String, next: String? = null, limit: Int = 20): Result<List<ShowSong>>
    suspend fun getShowStudios(showId: String, next: String? = null, limit: Int = 20): Result<List<Studio>>
    suspend fun getMoreByStudio(showId: String, next: String? = null, limit: Int = 20): Result<List<Show>>
    suspend fun getRelatedShows(showId: String, next: String? = null, limit: Int = 20): Result<List<RelatedShow>>
    suspend fun getRelatedLiteratures(showId: String, next: String? = null, limit: Int = 20): Result<List<RelatedLiterature>>
    suspend fun getRelatedGames(showId: String, next: String? = null, limit: Int = 20): Result<List<RelatedGame>>
    // Rating
    suspend fun rateShow(showId: String, rating: Double, review: String? = null): Result<Unit>
}

open class ShowRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ShowRepository {

    override suspend fun getShows(next: String?, limit: Int, filter: ShowFilter?): Result<List<ShowIdentity>> {
        var parameters: MutableMap<String, String> = mutableMapOf()
        if (next == null) {
            parameters = mutableMapOf("limit" to limit.toString())

            filter?.let { f ->
                val filters = f.toFilterMap().filterValues { it != null }

                try {
                    val filterJson = Json.encodeToString(filters)
                    parameters["filter"] = Base64.getEncoder().encodeToString(filterJson.toByteArray())
                } catch (e: Exception) {
                    println("❌ Encode error: Could not make base64 string from filter data $filters")
                }
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Index()
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getShows(ids: List<String>, relationships: List<String>): Result<List<ShowIdentity>> {
        val parameters: MutableMap<String, String> = mutableMapOf()
        if (!relationships.isEmpty()) {
            parameters.put("include", relationships.joinToString(","))
        }
        return apiClient.get<ShowIdentityResponse>(KKEndpoint.Show.Index(ids), parameters).map { it.data }
    }

    override suspend fun getShow(id: String, relationships: List<String>): Result<Show> {
        val parameters: MutableMap<String, String> = mutableMapOf()
        if (!relationships.isEmpty()) {
            parameters.put("include", relationships.joinToString(","))
        }
        return apiClient.get<ShowResponse>(KKEndpoint.Show.Details(id)).map { it.data.first() }
    }

    override suspend fun getUpcomingShows(next: String?, limit: Int): Result<List<ShowIdentity>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Upcoming
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getShowCast(showId: String, next: String?, limit: Int): Result<List<Person>> {
        val parameters = mapOf("limit" to limit.toString())
        return apiClient.get<CastIdentityResponse>(KKEndpoint.Show.Cast(showId), parameters).map { it.data }
    }

    override suspend fun getShowCharacters(showId: String, next: String?, limit: Int): Result<List<Character>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Characters(showId)
        return apiClient.get<CharacterIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getShowPeople(showId: String, next: String?, limit: Int): Result<List<Person>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.People(showId)
        return apiClient.get<PersonIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getShowReviews(showId: String, next: String?, limit: Int): Result<List<Review>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Reviews(showId)
        return apiClient.get<ReviewResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getShowSeasons(showId: String, next: String?, limit: Int): Result<List<Season>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Seasons(showId)
        return apiClient.get<SeasonIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getShowSongs(showId: String, next: String?, limit: Int): Result<List<ShowSong>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Songs(showId)
        return apiClient.get<ShowSongResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getShowStudios(showId: String, next: String?, limit: Int): Result<List<Studio>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.Studios(showId)
        return apiClient.get<StudioIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getMoreByStudio(showId: String, next: String?, limit: Int): Result<List<Show>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.MoreByStudio(showId)
        return apiClient.get<ShowIdentityResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getRelatedShows(showId: String, next: String?, limit: Int): Result<List<RelatedShow>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.RelatedShows(showId)
        return apiClient.get<RelatedShowResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getRelatedLiteratures(showId: String, next: String?, limit: Int): Result<List<RelatedLiterature>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.RelatedLiteratures(showId)
        return apiClient.get<RelatedLiteratureResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun getRelatedGames(showId: String, next: String?, limit: Int): Result<List<RelatedGame>> {
        val parameters = mapOf("limit" to limit.toString())
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Show.RelatedGames(showId)
        return apiClient.get<RelatedGameResponse>(endpoint, parameters).map { it.data }
    }

    override suspend fun rateShow(showId: String, rating: Double, review: String?): Result<Unit> {
        val body = mapOf(
            "rating" to rating,
            "review" to review
        ).filterValues { it != null }

        return apiClient.post<Unit, Map<String, Any?>>(KKEndpoint.Show.Rate(showId), body)
    }
}
