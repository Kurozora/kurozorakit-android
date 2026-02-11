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

/**
 * Represents a production entity, such as an animation studio, producer, or licensor.
 *
 * The [Studio] model provides a central hub for organizational information, including
 * its corporate history, branding assets, and a comprehensive portfolio of associated
 * media (shows, games, and literature).
 *
 * @property id The unique identifier for the studio.
 * @property type The resource type, typically "studios".
 * @property href The canonical API link to this studio's details.
 * @property attributes The descriptive, technical, and historical data of the studio.
 * @property relationships Connections to related studios and the media projects they produced.
 */
@Serializable
data class Studio(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null
) : IdentityResource {

    /**
     * Detailed organizational and metadata attributes for a [Studio].
     *
     * @property slug A URL-friendly version of the studio's name.
     * @property profile Visual asset representing the studio's profile or primary image.
     * @property banner Visual asset representing the studio's wide banner.
     * @property logo Visual asset for the studio's official logo.
     * @property name The primary name of the studio.
     * @property japaneseName The official name of the studio in Japanese (Kanji/Kana).
     * @property alternativeNames A list of aliases or common abbreviations for the studio.
     * @property predecessors String names of companies that existed before this studio was formed.
     * @property successor The name of the company that took over after this studio became defunct.
     * @property about A detailed biography or history of the studio.
     * @property address The physical headquarters address of the company.
     * @property tvRating The general content rating associated with the studio's typical output ([TVRating]).
     * @property stats Engagement and performance metrics ([MediaStat]) related to the studio's popularity.
     * @property socialURLs Links to official social media profiles (Twitter, Instagram, etc.).
     * @property websiteURLs A list of official corporate websites, handled by [WebsiteURLsAsListSerializer].
     * @property isProducer Indicates if this entity acts as a production committee member or producer.
     * @property isStudio Indicates if this entity is a primary animation or development studio.
     * @property isLicensor Indicates if this entity handles licensing for domestic or international distribution.
     * @property isNSFW Indicates if the studio primarily produces adult-oriented content.
     * @property foundedAtTimestamp The date the studio was officially established.
     * @property defunctAtTimestamp The date the studio ceased operations, if applicable.
     * @property library User-specific tracking data (e.g., following the studio) if authenticated.
     */
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

    /**
     * Represents the relational connections for a [Studio].
     *
     * @property predecessors Identity references to companies that preceded this studio.
     * @property successors Identity references to companies that succeeded this studio.
     * @property shows A paginated list of shows produced or licensed by this studio.
     * @property games A paginated list of games developed or published by this studio.
     * @property literatures A paginated list of literary works published by this studio.
     */
    @Serializable
    data class Relationships(
        val predecessors: StudioIdentityResponse? = null,
        val successors: StudioIdentityResponse? = null,
        val shows: ShowIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null
    )
}