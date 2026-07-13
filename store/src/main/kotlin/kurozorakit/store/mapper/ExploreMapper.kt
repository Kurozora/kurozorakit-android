package kurozorakit.store.mapper

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kurozorakit.data.models.explore.ExploreCategory
import kurozorakit.store.db.ExploreEntity

object ExploreMapper {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    fun toDomain(entity: ExploreEntity): ExploreCategory {
        return ExploreCategory(
            id = entity.id,
            type = entity.type,
            href = entity.href,
            attributes = ExploreCategory.Attributes(
                slug = entity.slug,
                title = entity.title,
                description = entity.description,
                position = entity.position.toInt(),
                type = entity.content_type,
                size = entity.size
            ),
            relationships = entity.relationships_json?.let { json.decodeFromString(it) }
        )
    }

    fun toEntity(domain: ExploreCategory): ExploreEntity {
        val attr = domain.attributes
        return ExploreEntity(
            id = domain.id,
            type = domain.type,
            href = domain.href,
            slug = attr.slug,
            title = attr.title,
            description = attr.description,
            position = attr.position.toLong(),
            content_type = attr.type,
            size = attr.size,
            relationships_json = domain.relationships?.let { json.encodeToString(it) },
            created_at = System.currentTimeMillis(),
            updated_at = System.currentTimeMillis()
        )
    }
}