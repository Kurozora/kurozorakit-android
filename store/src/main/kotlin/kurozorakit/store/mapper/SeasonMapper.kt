package kurozorakit.store.mapper

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kurozorakit.data.models.season.Season
import kurozorakit.store.db.SeasonEntity

object SeasonMapper {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    fun toDomain(entity: SeasonEntity): Season {
        val attr = Season.Attributes(
            number = entity.number.toInt(),
            title = entity.title,
            synopsis = entity.synopsis,
            episodeCount = entity.episode_count.toInt(),
            ratingAverage = entity.rating_average,
            startedAtTimestamp = entity.started_at_timestamp,
            poster = entity.poster_json?.let { json.decodeFromString(it) },
            // Episode'daki gibi private alanı internal setter üzerinden besliyoruz
            _watchStatus = entity.watch_status_json?.let { json.decodeFromString(it) }
        )

        return Season(
            id = entity.id,
            type = entity.type,
            href = entity.href,
            attributes = attr
        )
    }

    fun toEntity(domain: Season): SeasonEntity {
        val attr = domain.attributes
        return SeasonEntity(
            id = domain.id,
            type = domain.type,
            href = domain.href,
            number = attr.number.toLong(),
            title = attr.title,
            synopsis = attr.synopsis,
            episode_count = attr.episodeCount.toLong(),
            rating_average = attr.ratingAverage,
            started_at_timestamp = attr.startedAtTimestamp,
            watch_status_json = attr.watchStatus?.let { json.encodeToString(it) },
            poster_json = attr.poster?.let { json.encodeToString(it) },
            created_at = System.currentTimeMillis(),
            updated_at = System.currentTimeMillis()
        )
    }
}