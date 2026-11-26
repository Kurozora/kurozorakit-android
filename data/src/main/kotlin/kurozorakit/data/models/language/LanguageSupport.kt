package kurozorakit.data.models.language

import kotlinx.serialization.Serializable
import kurozorakit.data.enums.LanguageSupportType

/**
 * Summarizes the primary language support available for a media item (Show, Literature, or Game),
 * replacing the deprecated flat `languages` list.
 *
 * @property name Display name of the primary supported language (e.g., "Japanese").
 * @property code ISO 639-1 language code (e.g., "ja").
 * @property iso6393 ISO 639-3 bibliographic language code (e.g., "jpn").
 * @property types The support categories available for the primary language (e.g., Audio, Subtitles).
 * @property count Total number of distinct supported languages for this media item.
 */
@Serializable
data class LanguageSupport(
    val name: String? = null,
    val code: String? = null,
    val iso6393: String? = null,
    val types: List<LanguageSupportType> = emptyList(),
    val count: Int = 0,
)

/**
 * Detailed language availability entry returned by `/anime/{id}/languages`,
 * `/manga/{id}/languages`, and `/games/{id}/languages`.
 *
 * @property name Display name of the language.
 * @property code ISO 639-1 language code.
 * @property iso6393 ISO 639-3 language code.
 * @property type How this language is supported ([LanguageSupportType]).
 * @property isOriginal True if this matches the original language of the media.
 */
@Serializable
data class MediaLanguage(
    val name: String,
    val code: String,
    val iso6393: String,
    val type: LanguageSupportType,
    val isOriginal: Boolean = false,
)

/**
 * Response envelope for a list of [MediaLanguage] entries.
 */
@Serializable
data class MediaLanguageResponse(
    val data: List<MediaLanguage>,
)
