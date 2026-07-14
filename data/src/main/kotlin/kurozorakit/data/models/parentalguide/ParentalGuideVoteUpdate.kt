package kurozorakit.data.models.parentalguide

import kotlinx.serialization.Serializable

@Serializable
data class ParentalGuideVoteUpdate(
    val isHelpful: Boolean? = null
)

@Serializable
data class ParentalGuideVoteUpdateResponse(
    val data: ParentalGuideVoteUpdate
)
