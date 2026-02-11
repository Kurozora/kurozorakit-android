package kurozorakit.data.models.genre

import kotlinx.serialization.Serializable

/**
 * A collection of full [Genre] resource objects.
 *
 * This response model is typically utilized by the "Browse Categories" or "Explore"
 * sections of the app. Because it contains the full [Attributes] for each genre,
 * the UI can immediately render the colorful, branded cards that represent
 * each category without further network requests.
 *
 * Unlike many other response models in the kit, this does not typically include
 * a `next` pagination link, as the total number of genres is usually small
 * enough to be delivered in a single, comprehensive payload.
 *
 * @property data A list of [Genre] resources, each containing its unique visual
 * styling, names, and symbols.
 */
@Serializable
data class GenreResponse(
    val data: List<Genre>
)