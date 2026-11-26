package kurozorakit.data.models.media

import kotlinx.serialization.Serializable
import kurozorakit.data.models.character.CharacterIdentityResponse
import kurozorakit.data.models.episode.EpisodeIdentityResponse
import kurozorakit.data.models.game.GameIdentityResponse
import kurozorakit.data.models.literature.LiteratureIdentityResponse
import kurozorakit.data.models.person.PersonIdentityResponse
import kurozorakit.data.models.show.ShowIdentityResponse
import kurozorakit.data.models.song.SongIdentityResponse
import kurozorakit.data.models.studio.StudioIdentityResponse

/**
 * Represents a versatile media asset (Image, Poster, Banner) within the Kurozora ecosystem.
 *
 * This model not only holds the physical properties of a media file like its URL and dimensions
 * but also includes metadata for UI styling (color palettes), face-detection focal coordinates,
 * and relational links to the resources it belongs to.
 *
 * @property url The absolute web address where the media resource is hosted.
 * @property height The vertical dimension of the media in pixels.
 * @property width The horizontal dimension of the media in pixels.
 * @property backgroundColor A hex color code intended for use as a placeholder or background
 * during image loading or UI styling.
 * @property textColor1 Primary foreground color extracted from the image for UI accessibility.
 * @property textColor2 Secondary foreground color extracted from the image.
 * @property textColor3 Tertiary foreground color extracted from the image.
 * @property textColor4 Quaternary foreground color, typically used for subtle UI accents.
 * @property focalX Normalized horizontal focal point (0.0 - 1.0) for smart cropping.
 * @property focalY Normalized vertical focal point (0.0 - 1.0) for smart cropping.
 * @property relationships Connections indicating which entities (Shows, Games, etc.) use this media.
 */
@Serializable
data class Media(
    val url: String,
    val height: Int? = null,
    val width: Int? = null,
    val backgroundColor: String? = null,
    val textColor1: String? = null,
    val textColor2: String? = null,
    val textColor3: String? = null,
    val textColor4: String? = null,
    val focalX: Double? = null,
    val focalY: Double? = null,
    val relationships: Relationships? = null,
) {
    /**
     * Defines the entities associated with this specific [Media] asset.
     *
     * This is particularly useful for reverse-lookup scenarios, such as finding which
     * show or character a specific image belongs to.
     *
     * @property characters The [CharacterIdentityResponse] if this media belongs to a character.
     * @property episodes The [EpisodeIdentityResponse] if this media is an episode thumbnail or still.
     * @property games The [GameIdentityResponse] if this media is associated with a game resource.
     * @property literatures The [LiteratureIdentityResponse] if this media is a book cover or manga page.
     * @property people The [PersonIdentityResponse] if this media belongs to a real-world person.
     * @property shows The [ShowIdentityResponse] if this media belongs to an anime or live-action series.
     * @property songs The [SongIdentityResponse] if this media is song artwork.
     * @property studios The [StudioIdentityResponse] if this media belongs to a studio.
     */
    @Serializable
    data class Relationships(
        val characters: CharacterIdentityResponse? = null,
        val episodes: EpisodeIdentityResponse? = null,
        val games: GameIdentityResponse? = null,
        val literatures: LiteratureIdentityResponse? = null,
        val people: PersonIdentityResponse? = null,
        val shows: ShowIdentityResponse? = null,
        val songs: SongIdentityResponse? = null,
        val studios: StudioIdentityResponse? = null,
    )
}