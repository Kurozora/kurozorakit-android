package kurozorakit.data.models.language

import kotlinx.serialization.Serializable

/**
 * A container for a collection of [Language] resources.
 *
 * This response is the primary source of truth for all languages supported
 * within the Kurozora database. Unlike media resources, languages are
 * considered static reference data; therefore, this response is not
 * paginated and is intended to be fetched and cached during the app's
 * initialization or when entering a settings/filter context.
 *
 * @property data The complete list of [Language] objects available for filtering or metadata display.
 */
@Serializable
data class LanguageResponse(
    val data: List<Language>
)