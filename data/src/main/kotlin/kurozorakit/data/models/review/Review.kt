package kurozorakit.data.models.review

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.LocalDateSerializer
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.song.SongIdentityResponse
import kurozorakit.data.models.studio.StudioIdentityResponse
import kurozorakit.data.models.user.UserResponse

/**
 * Represents a user-generated review within the Kurozora ecosystem.
 *
 * This model encapsulates a user's qualitative and quantitative feedback regarding various
 * resources. A single review can be linked to almost any major entity in the database,
 * ranging from a specific episode to a production studio or a staff member.
 *
 * @property id The unique identifier for the review.
 * @property type The resource type designation, typically "reviews".
 * @property href The canonical API link to access the detailed data of this specific review.
 * @property attributes The core content of the review, including the numerical score and text.
 * @property relationships Pointers to the resource being reviewed and the user who authored it.
 * @see IdentityResource
 */
@Serializable
data class Review(
    override val id: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null
) : IdentityResource {

    /**
     * Contains the core data and metadata for a [Review].
     *
     * @property score The numerical rating provided by the user (e.g., 8.5 or 10.0).
     * @property description The long-form text content of the review. May be null if only a score was given.
     * @property createdAt The date when the review was originally published.
     * Uses [LocalDateSerializer] for ISO date handling.
     */
    @Serializable
    data class Attributes(
        val score: Double,
        val description: String? = null,
        @Serializable(with = LocalDateSerializer::class)
        val createdAt: LocalDate?
    )

    /**
     * Defines the contextual connections for this [Review].
     * * In the Kurozora API, a review is typically linked to exactly one media resource and one user.
     *
     * @property shows The anime or series this review is about.
     * @property games The video game this review is about.
     * @property literatures The manga, light novel, or book being reviewed.
     * @property episodes A specific episode if the review is not for the entire series.
     * @property songs The specific song or soundtrack being reviewed.
     * @property characters The fictional character profile this review is attached to.
     * @property people The real-world person (staff/voice actor) being reviewed.
     * @property studios The animation or game development studio being reviewed.
     * @property users The [UserResponse] containing the profile of the author who wrote this review.
     */
    @Serializable
    data class Relationships(
        val shows: ShowIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null,
        val episodes: EpisodeIdentityResponse? = null,
        val songs: SongIdentityResponse? = null,
        val characters: CharacterIdentityResponse? = null,
        val people: PersonIdentityResponse? = null,
        val studios: StudioIdentityResponse? = null,
        val users: UserResponse? = null
    )
}