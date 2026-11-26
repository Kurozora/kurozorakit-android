package kurozorakit.data.repositories.literature

import io.ktor.client.request.*
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.data.enums.ReviewRecommendation
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.editorial.EditorialResponse
import kurozorakit.data.models.language.MediaLanguageResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.literature.LiteratureResponse
import kurozorakit.data.models.media.MediaResponse
import kurozorakit.data.models.museum.MuseumItemResponse
import kurozorakit.data.models.museum.MuseumYearResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.review.ReviewResponse
import kurozorakit.data.models.search.SearchResponse
import kurozorakit.data.models.search.filters.LiteratureFilter
import kurozorakit.data.models.show.cast.CastIdentityResponse
import kurozorakit.data.models.show.related.RelatedGameResponse
import kurozorakit.data.models.show.related.RelatedLiteratureResponse
import kurozorakit.data.models.show.related.RelatedShowResponse
import kurozorakit.data.models.staff.StaffResponse
import kurozorakit.data.models.studio.StudioResponse
import kurozorakit.shared.Result
import java.util.*

/**
 * Data repository responsible for managing and retrieving literature-based media.
 *
 * This repository handles requests for various forms of literature, including
 * Manga, Light Novels, and Webtoons.
 */
interface LiteratureRepository {

    /** Retrieves a paginated list of literature entries, optionally filtered by specific criteria. */
    suspend fun getLiteratures(next: String? = null, limit: Int = 20, filter: LiteratureFilter? = null): Result<LiteratureIdentityResponse>

    /** Fetches specific literature entries by a list of identifiers in a single request. */
    suspend fun getLiteratures(ids: List<String>, relationships: List<String> = emptyList()): Result<LiteratureResponse>

    /** Fetches detailed information for a specific literature entry. */
    suspend fun getLiterature(id: String, relationships: List<String> = emptyList(), forceRefresh: Boolean = false): Result<LiteratureResponse>

    /** Retrieves a list of literature items scheduled for future release or publication. */
    suspend fun getUpcomingLiteratures(next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>

    /** Retrieves literature items that have been adapted into other media (`GET /manga/adapted`). */
    suspend fun getAdaptedLiteratures(next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>

    /** Retrieves the literature museum yearly overview (`GET /manga/museum`). */
    suspend fun getLiteratureMuseum(next: String? = null, limit: Int = 25): Result<MuseumYearResponse>

    /** Retrieves literature museum entries for a specific release year (`GET /manga/museum/{year}`). */
    suspend fun getLiteratureMuseumYear(year: Int, next: String? = null, limit: Int = 25): Result<MuseumItemResponse>

    /** Retrieves gallery images for a literature entry (`GET /manga/{id}/gallery`). */
    suspend fun getLiteratureGallery(literatureId: String, next: String? = null, limit: Int = 20): Result<MediaResponse>

    /** Retrieves supported languages for a literature entry (`GET /manga/{id}/languages`). */
    suspend fun getLiteratureLanguages(literatureId: String, next: String? = null, limit: Int = 20): Result<MediaLanguageResponse>

    /** Retrieves editorial articles/notes for a literature entry (`GET /manga/{id}/editorial`). */
    suspend fun getLiteratureEditorial(literatureId: String): Result<EditorialResponse>

    /** Retrieves the cast associated with a literature piece. */
    suspend fun getLiteratureCast(literatureId: String, next: String? = null, limit: Int = 20): Result<CastIdentityResponse>

    /** Fetches the production staff for the literature. */
    suspend fun getLiteratureStaff(literatureId: String, next: String? = null, limit: Int = 20): Result<StaffResponse>

    /** Retrieves the characters featured within the specific literature. */
    suspend fun getLiteratureCharacters(literatureId: String, next: String? = null, limit: Int = 20): Result<CharacterIdentityResponse>

    /** Retrieves real-life people associated with the literature. */
    suspend fun getLiteraturePeople(literatureId: String, next: String? = null, limit: Int = 20): Result<PersonIdentityResponse>

    /** Retrieves user-written reviews for a specific literature entry. */
    suspend fun getLiteratureReviews(literatureId: String, next: String? = null, limit: Int = 20): Result<ReviewResponse>

    /** Fetches the studios or publishing houses responsible for this literature. */
    suspend fun getLiteratureStudios(literatureId: String, next: String? = null, limit: Int = 20): Result<StudioResponse>

    /** Retrieves other literature entries published by the same studio or publisher. */
    suspend fun getMoreByStudio(literatureId: String, next: String? = null, limit: Int = 20): Result<LiteratureIdentityResponse>

    /** Fetches shows (Anime/TV) that are related to or adapted from this literature. */
    suspend fun getRelatedShows(literatureId: String, next: String? = null, limit: Int = 20): Result<RelatedShowResponse>

    /** Fetches other literature entries that are part of the same franchise or series. */
    suspend fun getRelatedLiteratures(literatureId: String, next: String? = null, limit: Int = 20): Result<RelatedLiteratureResponse>

    /** Retrieves video games related to or adapted from this literature. */
    suspend fun getRelatedGames(literatureId: String, next: String? = null, limit: Int = 20): Result<RelatedGameResponse>

    /** Submits or updates a user's rating and optional review for a literature entry. */
    suspend fun rateLiterature(
        literatureId: String,
        rating: Double,
        review: String? = null,
        recommendation: ReviewRecommendation? = null,
        isSpoiler: Boolean? = null,
    ): Result<Unit>

    /** Removes the authenticated user's rating and review for a literature entry (`DELETE /manga/{id}/rate`). */
    suspend fun deleteLiteratureRating(literatureId: String): Result<Unit>
}

/**
 * Implementation of [LiteratureRepository] that utilizes [KurozoraApiClient] directly to
 * manage literature content and community data.
 *
 * @property apiClient The client used to communicate with the literature and relationship endpoints.
 */
open class LiteratureRepositoryImpl(
    private val apiClient: KurozoraApiClient,
) : LiteratureRepository {

    override suspend fun getLiteratures(next: String?, limit: Int, filter: LiteratureFilter?): Result<LiteratureIdentityResponse> {
        var parameters: MutableMap<String, String> = mutableMapOf()
        if (next == null) {
            parameters = mutableMapOf("limit" to limit.toString())

            filter?.let { f ->
                val filters = f.toFilterMap(false).filterValues { it != null }

                try {
                    val jsonObject = buildJsonObject {
                        filters.forEach { (key, value) ->
                            when (value) {
                                is String -> put(key, JsonPrimitive(value))
                                is Boolean -> put(key, JsonPrimitive(value))
                                is Number -> put(key, JsonPrimitive(value))
                                else -> println("⚠️ Unknown type for key=$key, value=$value")
                            }
                        }
                    }

                    val filterJson = jsonObject.toString()
                    parameters["filter"] = Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(filterJson.toByteArray(Charsets.UTF_8))
                } catch (e: Exception) {
                    println("❌ Encode error: Could not make base64 string from filter data $filters \n$e")
                }
            }
        }
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Index()
        return apiClient.get<SearchResponse>(endpoint, parameters)
            .map { it.data.literatures ?: LiteratureIdentityResponse(data = emptyList()) }
    }

    override suspend fun getLiteratures(ids: List<String>, relationships: List<String>): Result<LiteratureResponse> {
        return apiClient.get(KKEndpoint.Literature.Index(*ids.toTypedArray())) {
            url { relationships.forEach { parameters.append("include[]", it) } }
        }
    }

    override suspend fun getLiterature(id: String, relationships: List<String>, forceRefresh: Boolean): Result<LiteratureResponse> {
        return apiClient.get(KKEndpoint.Literature.Details(id)) {
            url { relationships.forEach { parameters.append("include[]", it) } }
        }
    }

    override suspend fun getUpcomingLiteratures(next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Upcoming
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getAdaptedLiteratures(next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Adapted
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureMuseum(next: String?, limit: Int): Result<MuseumYearResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Museum
        return apiClient.get<MuseumYearResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureMuseumYear(year: Int, next: String?, limit: Int): Result<MuseumItemResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.MuseumYear(year)
        return apiClient.get<MuseumItemResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureGallery(literatureId: String, next: String?, limit: Int): Result<MediaResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Gallery(literatureId)
        return apiClient.get<MediaResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureLanguages(literatureId: String, next: String?, limit: Int): Result<MediaLanguageResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Languages(literatureId)
        return apiClient.get<MediaLanguageResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureEditorial(literatureId: String): Result<EditorialResponse> {
        return apiClient.get<EditorialResponse>(KKEndpoint.Literature.Editorial(literatureId))
    }

    override suspend fun getLiteratureCast(literatureId: String, next: String?, limit: Int): Result<CastIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Cast(literatureId)
        return apiClient.get<CastIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureStaff(literatureId: String, next: String?, limit: Int): Result<StaffResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Staff(literatureId)
        return apiClient.get<StaffResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureCharacters(literatureId: String, next: String?, limit: Int): Result<CharacterIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Characters(literatureId)
        return apiClient.get<CharacterIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteraturePeople(literatureId: String, next: String?, limit: Int): Result<PersonIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.People(literatureId)
        return apiClient.get<PersonIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureReviews(literatureId: String, next: String?, limit: Int): Result<ReviewResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Reviews(literatureId)
        return apiClient.get<ReviewResponse>(endpoint, parameters)
    }

    override suspend fun getLiteratureStudios(literatureId: String, next: String?, limit: Int): Result<StudioResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.Studios(literatureId)
        return apiClient.get<StudioResponse>(endpoint, parameters)
    }

    override suspend fun getMoreByStudio(literatureId: String, next: String?, limit: Int): Result<LiteratureIdentityResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.MoreByStudio(literatureId)
        return apiClient.get<LiteratureIdentityResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedShows(literatureId: String, next: String?, limit: Int): Result<RelatedShowResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.RelatedShows(literatureId)
        return apiClient.get<RelatedShowResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedLiteratures(literatureId: String, next: String?, limit: Int): Result<RelatedLiteratureResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.RelatedLiteratures(literatureId)
        return apiClient.get<RelatedLiteratureResponse>(endpoint, parameters)
    }

    override suspend fun getRelatedGames(literatureId: String, next: String?, limit: Int): Result<RelatedGameResponse> {
        val parameters = if (next == null) mapOf("limit" to limit.toString()) else emptyMap()
        val endpoint: KKEndpoint = next?.let { KKEndpoint.Url(it) } ?: KKEndpoint.Literature.RelatedGames(literatureId)
        return apiClient.get<RelatedGameResponse>(endpoint, parameters)
    }

    override suspend fun rateLiterature(
        literatureId: String,
        rating: Double,
        review: String?,
        recommendation: ReviewRecommendation?,
        isSpoiler: Boolean?,
    ): Result<Unit> {
        return apiClient.post<Unit, Unit>(KKEndpoint.Literature.Rate(literatureId)) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("rating", rating.toString())
                if (!review.isNullOrBlank()) {
                    append("description", review)
                }
                recommendation?.let { append("recommendation", it.rawValue.toString()) }
                isSpoiler?.let { append("isSpoiler", if (it) "1" else "0") }
            }))
        }
    }

    override suspend fun deleteLiteratureRating(literatureId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.Literature.DeleteRate(literatureId))
    }
}
