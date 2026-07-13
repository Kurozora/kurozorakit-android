package kurozorakit.store.mapper

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kurozorakit.data.models.genre.Genre
import kurozorakit.data.models.media.Media
import kurozorakit.store.db.GenreEntity

object GenreMapper {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun toDomain(entity: GenreEntity): Genre {
        return Genre(
            id = entity.id,
            type = entity.type,
            href = entity.href,
            attributes = Genre.Attributes(
                slug = entity.slug,
                name = entity.name,
                description = entity.description,
                backgroundColor1 = entity.background_color1,
                backgroundColor2 = entity.background_color2,
                textColor1 = entity.text_color1,
                textColor2 = entity.text_color2,
                symbol = entity.symbol_json?.let { parseMedia(it) },
                isNSFW = entity.is_nsfw == 1L
            )
        )
    }

    fun toEntityParams(genre: Genre): GenreEntityParams {
        val attrs = genre.attributes
        return GenreEntityParams(
            id = genre.id,
            type = genre.type,
            href = genre.href,
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

    private fun parseMedia(jsonStr: String): Media? = try {
        json.decodeFromString<Media>(jsonStr)
    } catch (e: Exception) {
        null
    }
}

data class GenreEntityParams(
    val id: String,
    val type: String,
    val href: String,
    val slug: String,
    val name: String,
    val description: String?,
    val backgroundColor1: String,
    val backgroundColor2: String,
    val textColor1: String,
    val textColor2: String,
    val symbolJson: String?,
    val isNsfw: Long,
    val createdAt: Long,
    val updatedAt: Long
)