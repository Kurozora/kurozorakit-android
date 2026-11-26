package kurozorakit.data.models.provider

import kotlinx.serialization.Serializable
import kurozorakit.data.models.media.Media

/**
 * Represents a streaming or content provider listed by `GET /providers`.
 */
@Serializable
data class Provider(
    val id: String,
    val type: String,
    val attributes: Attributes,
) {
    @Serializable
    data class Attributes(
        val slug: String,
        val name: String,
        val alternativeNames: List<String>? = null,
        val url: String? = null,
        val logo: Media? = null,
        val isPublic: Boolean = true,
    )
}

@Serializable
data class ProviderResponse(
    val data: List<Provider> = emptyList(),
    val next: String? = null,
)
