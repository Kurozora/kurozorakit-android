package kurozorakit.store.mapper

import kotlinx.datetime.LocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kurozorakit.data.models.game.Game
import kurozorakit.store.db.GameEntity

object GameMapper {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    fun toDomain(entity: GameEntity): Game {
        return Game(
            id = entity.id,
            type = entity.type,
            href = entity.href,
            attributes = Game.Attributes(
                igdbID = entity.igdb_id?.toInt(),
                igdbSlug = entity.igdb_slug,
                slug = entity.slug,
                title = entity.title,
                originalTitle = entity.original_title,
                synopsis = entity.synopsis,
                tagline = entity.tagline,
                studio = entity.studio,
                poster = entity.poster_json?.let { json.decodeFromString(it) },
                banner = entity.banner_json?.let { json.decodeFromString(it) },
                logo = entity.logo_json?.let { json.decodeFromString(it) },
                tvRating = json.decodeFromString(entity.tv_rating_json),
                type = json.decodeFromString(entity.media_type_json),
                source = json.decodeFromString(entity.source_json),
                status = json.decodeFromString(entity.status_json),
                editionCount = entity.edition_count.toInt(),
                duration = entity.duration,
                durationCount = entity.duration_count.toInt(),
                durationTotal = entity.duration_total,
                durationTotalCount = entity.duration_total_count.toInt(),
                publicationSeason = entity.publication_season,
                publicationTime = entity.publication_time,
                publicationDay = entity.publication_day,
                isNSFW = entity.is_nsfw == 1L,
                copyright = entity.copyright,
                startedAt = entity.started_at?.let { LocalDate.parse(it) },
                endedAt = entity.ended_at?.let { LocalDate.parse(it) },
                synonymTitles = entity.synonym_titles_json?.let { json.decodeFromString(it) },
                genres = entity.genres_json?.let { json.decodeFromString(it) },
                themes = entity.themes_json?.let { json.decodeFromString(it) },
                languages = entity.languages_json?.let { json.decodeFromString(it) },
                countryOfOrigin = entity.country_of_origin_json?.let { json.decodeFromString(it) },
                stats = entity.stats_json?.let { json.decodeFromString(it) },
                library = entity.library_json?.let { json.decodeFromString(it) }
            ),
            relationships = entity.relationships_json?.let { json.decodeFromString(it) }
        )
    }

    fun toEntity(game: Game): GameEntity {
        val attr = game.attributes
        return GameEntity(
            id = game.id,
            type = game.type,
            href = game.href,
            slug = attr.slug,
            title = attr.title,
            original_title = attr.originalTitle,
            synopsis = attr.synopsis,
            tagline = attr.tagline,
            studio = attr.studio,
            igdb_id = attr.igdbID?.toLong(),
            igdb_slug = attr.igdbSlug,
            poster_json = attr.poster?.let { json.encodeToString(it) },
            banner_json = attr.banner?.let { json.encodeToString(it) },
            logo_json = attr.logo?.let { json.encodeToString(it) },
            tv_rating_json = json.encodeToString(attr.tvRating),
            media_type_json = json.encodeToString(attr.type),
            source_json = json.encodeToString(attr.source),
            status_json = json.encodeToString(attr.status),
            edition_count = attr.editionCount.toLong(),
            duration = attr.duration,
            duration_count = attr.durationCount.toLong(),
            duration_total = attr.durationTotal,
            duration_total_count = attr.durationTotalCount.toLong(),
            publication_season = attr.publicationSeason,
            publication_time = attr.publicationTime,
            publication_day = attr.publicationDay,
            is_nsfw = if (attr.isNSFW) 1L else 0L,
            copyright = attr.copyright,
            started_at = attr.startedAt?.toString(),
            ended_at = attr.endedAt?.toString(),
            synonym_titles_json = attr.synonymTitles?.let { json.encodeToString(it) },
            genres_json = attr.genres?.let { json.encodeToString(it) },
            themes_json = attr.themes?.let { json.encodeToString(it) },
            languages_json = json.encodeToString(attr.languages),
            country_of_origin_json = attr.countryOfOrigin?.let { json.encodeToString(it) },
            stats_json = attr.stats?.let { json.encodeToString(it) },
            library_json = attr.library?.let { json.encodeToString(it) },
            relationships_json = game.relationships?.let { json.encodeToString(it) },
            created_at = System.currentTimeMillis(),
            updated_at = System.currentTimeMillis()
        )
    }
}