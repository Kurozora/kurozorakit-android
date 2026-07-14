package kurozorakit.data.repositories.parentalguide

import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kurozorakit.api.KKEndpoint
import kurozorakit.api.KurozoraApiClient
import kurozorakit.shared.Result
import kurozorakit.data.enums.ParentalGuideReportReason
import kurozorakit.data.models.parentalguide.ParentalGuideEntryResponse
import kurozorakit.data.models.parentalguide.ParentalGuideResponse
import kurozorakit.data.models.parentalguide.ParentalGuideVoteUpdateResponse
import kurozorakit.data.models.parentalguide.request.ParentalGuideEntryRequest

interface ParentalGuideRepository {
    suspend fun getShowParentalGuide(showId: String): Result<ParentalGuideResponse>
    suspend fun getLiteratureParentalGuide(literatureId: String): Result<ParentalGuideResponse>
    suspend fun getGameParentalGuide(gameId: String): Result<ParentalGuideResponse>

    suspend fun submitShowEntry(showId: String, request: ParentalGuideEntryRequest): Result<ParentalGuideEntryResponse>
    suspend fun submitLiteratureEntry(literatureId: String, request: ParentalGuideEntryRequest): Result<ParentalGuideEntryResponse>
    suspend fun submitGameEntry(gameId: String, request: ParentalGuideEntryRequest): Result<ParentalGuideEntryResponse>

    suspend fun updateEntry(entryId: String, request: ParentalGuideEntryRequest): Result<ParentalGuideEntryResponse>
    suspend fun deleteEntry(entryId: String): Result<Unit>
    suspend fun voteEntry(entryId: String, vote: Boolean?): Result<ParentalGuideVoteUpdateResponse>
    suspend fun reportEntry(entryId: String, reason: ParentalGuideReportReason, details: String? = null): Result<Unit>
}

open class ParentalGuideRepositoryImpl(
    private val apiClient: KurozoraApiClient
) : ParentalGuideRepository {

    override suspend fun getShowParentalGuide(showId: String): Result<ParentalGuideResponse> {
        return apiClient.get<ParentalGuideResponse>(KKEndpoint.Show.ParentalGuide(showId))
    }

    override suspend fun getLiteratureParentalGuide(literatureId: String): Result<ParentalGuideResponse> {
        return apiClient.get<ParentalGuideResponse>(KKEndpoint.Literature.ParentalGuide(literatureId))
    }

    override suspend fun getGameParentalGuide(gameId: String): Result<ParentalGuideResponse> {
        return apiClient.get<ParentalGuideResponse>(KKEndpoint.Game.ParentalGuide(gameId))
    }

    override suspend fun submitShowEntry(showId: String, request: ParentalGuideEntryRequest): Result<ParentalGuideEntryResponse> {
        return submitEntry(KKEndpoint.Show.ParentalGuide(showId), request)
    }

    override suspend fun submitLiteratureEntry(literatureId: String, request: ParentalGuideEntryRequest): Result<ParentalGuideEntryResponse> {
        return submitEntry(KKEndpoint.Literature.ParentalGuide(literatureId), request)
    }

    override suspend fun submitGameEntry(gameId: String, request: ParentalGuideEntryRequest): Result<ParentalGuideEntryResponse> {
        return submitEntry(KKEndpoint.Game.ParentalGuide(gameId), request)
    }

    override suspend fun updateEntry(entryId: String, request: ParentalGuideEntryRequest): Result<ParentalGuideEntryResponse> {
        return submitEntry(KKEndpoint.ParentalGuideEntry.Update(entryId), request)
    }

    override suspend fun deleteEntry(entryId: String): Result<Unit> {
        return apiClient.delete<Unit>(KKEndpoint.ParentalGuideEntry.Delete(entryId))
    }

    override suspend fun voteEntry(entryId: String, vote: Boolean?): Result<ParentalGuideVoteUpdateResponse> {
        return apiClient.post<ParentalGuideVoteUpdateResponse, Map<String, String>>(KKEndpoint.ParentalGuideEntry.Vote(entryId)) {
            contentType(ContentType.Application.FormUrlEncoded)
            if (vote != null) {
                setBody(FormDataContent(Parameters.build {
                    append("vote", if (vote) "helpful" else "unhelpful")
                }))
            }
        }
    }

    override suspend fun reportEntry(entryId: String, reason: ParentalGuideReportReason, details: String?): Result<Unit> {
        return apiClient.post<Unit, Map<String, String>>(KKEndpoint.ParentalGuideEntry.Report(entryId)) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("reason_key", reason.value)
                if (!details.isNullOrBlank()) {
                    append("details", details)
                }
            }))
        }
    }

    private suspend fun submitEntry(endpoint: KKEndpoint, request: ParentalGuideEntryRequest): Result<ParentalGuideEntryResponse> {
        return apiClient.post<ParentalGuideEntryResponse, ParentalGuideEntryRequest>(endpoint, request) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(Parameters.build {
                append("category", request.category.toString())
                append("rating", request.rating.toString())
                append("is_spoiler", if (request.isSpoiler) "1" else "0")
                request.frequency?.let { append("frequency", it.toString()) }
                request.depiction?.let { append("depiction", it.toString()) }
                request.reason?.let { append("reason", it) }
            }))
        }
    }
}
