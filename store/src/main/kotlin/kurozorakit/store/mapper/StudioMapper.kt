package kurozorakit.store.mapper

import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kurozorakit.data.models.studio.Studio
import kurozorakit.store.db.StudioEntity

object StudioMapper {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    fun toDomain(entity: StudioEntity): Studio {
        return Studio(
            id = entity.id,
            type = entity.type,
            href = entity.href,
            attributes = Studio.Attributes(
                slug = entity.slug,
                name = entity.name,
                japaneseName = entity.japanese_name,
                successor = entity.successor,
                about = entity.about,
                address = entity.address,
                isProducer = entity.is_producer?.let { it == 1L },
                isStudio = entity.is_studio?.let { it == 1L },
                isLicensor = entity.is_licensor?.let { it == 1L },
                isNSFW = entity.is_nsfw == 1L,
                foundedAtTimestamp = entity.founded_at?.let { LocalDate.parse(it) },
                defunctAtTimestamp = entity.defunct_at?.let { LocalDate.parse(it) },
                profile = entity.profile_json?.let { json.decodeFromString(it) },
                banner = entity.banner_json?.let { json.decodeFromString(it) },
                logo = entity.logo_json?.let { json.decodeFromString(it) },
                tvRating = json.decodeFromString(entity.tv_rating_json),
                alternativeNames = entity.alternative_names_json?.let { json.decodeFromString(it) },
                predecessors = entity.predecessors_json?.let { json.decodeFromString(it) } ?: emptyList(),
                socialURLs = entity.social_urls_json?.let { json.decodeFromString(it) },
                websiteURLs = entity.website_urls_json?.let { json.decodeFromString(it) },
                stats = entity.stats_json?.let { json.decodeFromString(it) },
                library = entity.library_json?.let { json.decodeFromString(it) }
            ),
            relationships = entity.relationships_json?.let { json.decodeFromString(it) }
        )
    }

    fun toEntity(studio: Studio): StudioEntity {
        val attr = studio.attributes
        return StudioEntity(
            id = studio.id,
            type = studio.type,
            href = studio.href,
            slug = attr.slug,
            name = attr.name,
            japanese_name = attr.japaneseName,
            successor = attr.successor,
            about = attr.about,
            address = attr.address,
            is_producer = attr.isProducer?.let { if (it) 1L else 0L },
            is_studio = attr.isStudio?.let { if (it) 1L else 0L },
            is_licensor = attr.isLicensor?.let { if (it) 1L else 0L },
            is_nsfw = if (attr.isNSFW) 1L else 0L,
            founded_at = attr.foundedAtTimestamp?.toString(),
            defunct_at = attr.defunctAtTimestamp?.toString(),
            profile_json = attr.profile?.let { json.encodeToString(it) },
            banner_json = attr.banner?.let { json.encodeToString(it) },
            logo_json = attr.logo?.let { json.encodeToString(it) },
            tv_rating_json = json.encodeToString(attr.tvRating),
            alternative_names_json = attr.alternativeNames?.let { json.encodeToString(it) },
            predecessors_json = json.encodeToString(attr.predecessors),
            social_urls_json = attr.socialURLs?.let { json.encodeToString(it) },
            website_urls_json = attr.websiteURLs?.let { json.encodeToString(it) },
            stats_json = attr.stats?.let { json.encodeToString(it) },
            library_json = attr.library?.let { json.encodeToString(it) },
            relationships_json = studio.relationships?.let { json.encodeToString(it) },
            created_at = System.currentTimeMillis(),
            updated_at = System.currentTimeMillis()
        )
    }
}