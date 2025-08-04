package kurozora.kit.data.models.studio

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.LocalDateSerializer
import kurozora.kit.data.models.WebsiteURLsAsListSerializer
import kurozora.kit.data.models.library.LibraryAttributes
import kurozora.kit.data.models.game.GameIdentityResponse
import kurozora.kit.data.models.literature.LiteratureIdentityResponse
import kurozora.kit.data.models.media.Media
import kurozora.kit.data.models.media.MediaStat
import kurozora.kit.data.models.show.ShowIdentityResponse
import kurozora.kit.data.models.show.attributes.TVRating

@Serializable
data class Studio(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null
) : IdentityResource {

    @Serializable
    data class Attributes(
        val slug: String,
        val profile: Media? = null,
        val banner: Media? = null,
        val logo: Media? = null,
        val name: String,
        val japaneseName: String? = null,
        val alternativeNames: List<String>? = null,
        val predecessors: List<String> = emptyList(),
        val successor: String? = null,
        val about: String? = null,
        val address: String? = null,
        val tvRating: TVRating,
        val stats: MediaStat? = null,
        val socialURLs: List<String>? = null,
        @Serializable(with = WebsiteURLsAsListSerializer::class)
        val websiteURLs: List<String>? = null,
        val isProducer: Boolean? = null,
        val isStudio: Boolean? = null,
        val isLicensor: Boolean? = null,
        val isNSFW: Boolean,
        @Serializable(LocalDateSerializer::class)
        val foundedAtTimestamp: LocalDate?,
        @Serializable(LocalDateSerializer::class)
        val defunctAtTimestamp: LocalDate? = null,
        var library: LibraryAttributes? = null
    )

    @Serializable
    data class Relationships(
        val predecessors: StudioIdentityResponse? = null,
        val successors: StudioIdentityResponse? = null,
        val shows: ShowIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null
    )
}
