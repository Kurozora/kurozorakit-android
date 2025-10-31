package kurozorakit.data.models.literature

import kotlinx.serialization.Serializable

@Serializable
data class LiteratureIdentityResponse(
    val data: List<LiteratureIdentity>,
    val next: String? = null,
)