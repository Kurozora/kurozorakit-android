package kurozorakit.store.mapper

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kurozorakit.data.models.theme.Theme
import kurozorakit.data.models.media.Media
import kurozorakit.store.db.ThemeEntity

object ThemeMapper {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun toDomain(entity: ThemeEntity): Theme {
        return Theme(
            id = entity.id,
            type = entity.type,
            href = entity.href,
            attributes = Theme.Attributes(
                slug = entity.slug,
                name = entity.name,
                description = entity.description,
                backgroundColor1 = entity.background_color1,
                backgroundColor2 = entity.background_color2,
                textColor1 = entity.text_color1,
                textColor2 = entity.text_color2,
                symbol = entity.symbol_json?.let { decodeMedia(it) },
                isNSFW = entity.is_nsfw == 1L
            )
        )
    }

    fun toEntityParams(theme: Theme): ThemeEntityParams {
        val attrs = theme.attributes
        return ThemeEntityParams(
            id = theme.id,
            type = theme.type,
            href = theme.href,
            slug = attrs.slug,
            name = attrs.name,
            description = attrs.description,
            backgroundColor1 = attrs.backgroundColor1,
            backgroundColor2 = attrs.backgroundColor2,
            textColor1 = attrs.textColor1,
            textColor2 = attrs.textColor2,
            symbolJson = attrs.symbol?.let { json.encodeToString(it) },
            isNsfw = if (attrs.isNSFW) 1L else 0L,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }

    private fun decodeMedia(jsonStr: String): Media? = try {
        json.decodeFromString<Media>(jsonStr)
    } catch (e: Exception) {
        null
    }
}

data class ThemeEntityParams(
    val id: String, val type: String, val href: String,
    val slug: String, val name: String, val description: String?,
    val backgroundColor1: String, val backgroundColor2: String,
    val textColor1: String, val textColor2: String,
    val symbolJson: String?, val isNsfw: Long,
    val createdAt: Long, val updatedAt: Long
)