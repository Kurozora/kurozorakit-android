package kurozorakit.data.models.studio

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.LocalDateSerializer
import kurozorakit.data.models.WebsiteURLsAsListSerializer
import kurozorakit.data.models.library.LibraryAttributes
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaStat
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.show.attributes.TVRating

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
