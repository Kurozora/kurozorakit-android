package kurozorakit.store.mapper

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kurozorakit.data.models.episode.Episode
import kurozorakit.store.db.EpisodeEntity

object EpisodeMapper {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    fun toDomain(entity: EpisodeEntity): Episode {
        val attr = Episode.Attributes(
            number = entity.number.toInt(),
            numberTotal = entity.number_total.toInt(),
            seasonNumber = entity.season_number.toInt(),
            title = entity.title,
            synopsis = entity.synopsis,
            previousEpisodeTitle = entity.previous_episode_title,
            nextEpisodeTitle = entity.next_episode_title,
            showTitle = entity.show_title,
            duration = entity.duration,
            startedAtTimestamp = entity.started_at_timestamp,
            viewCount = entity.view_count.toInt(),
            isFiller = entity.is_filler == 1L,
            isVerified = entity.is_verified == 1L,
            givenRating = entity.given_rating,
            givenReview = entity.given_review,
            poster = entity.poster_json?.let { json.decodeFromString(it) },
            banner = entity.banner_json?.let { json.decodeFromString(it) },
            stats = entity.stats_json?.let { json.decodeFromString(it) },
            // WatchStatus bilgisini internal setter üzerinden yüklüyoruz
            _watchStatus = entity.watch_status_json?.let { json.decodeFromString(it) }
        )

        return Episode(
            id = entity.id,
            type = entity.type,
            href = entity.href,
            attributes = attr,
            relationships = entity.relationships_json?.let { json.decodeFromString(it) }
        )
    }

    fun toEntity(episode: Episode): EpisodeEntity {
        val attr = episode.attributes
        return EpisodeEntity(
            id = episode.id,
            type = episode.type,
            href = episode.href,
            number = attr.number.toLong(),
            number_total = attr.numberTotal.toLong(),
            season_number = attr.seasonNumber.toLong(),
            title = attr.title,
            synopsis = attr.synopsis,
            previous_episode_title = attr.previousEpisodeTitle,
            next_episode_title = attr.nextEpisodeTitle,
            show_title = attr.showTitle,
            duration = attr.duration,
            started_at_timestamp = attr.startedAtTimestamp,
            view_count = attr.viewCount.toLong(),
            is_filler = if (attr.isFiller) 1L else 0L,
            is_verified = if (attr.isVerified) 1L else 0L,
            given_rating = attr.givenRating,
            given_review = attr.givenReview,
            // Sınıfın kendi resolve ettiği watchStatus değerini kaydediyoruz
            watch_status_json = attr.watchStatus?.let { json.encodeToString(it) },
            poster_json = attr.poster?.let { json.encodeToString(it) },
            banner_json = attr.banner?.let { json.encodeToString(it) },
            stats_json = attr.stats?.let { json.encodeToString(it) },
            relationships_json = episode.relationships?.let { json.encodeToString(it) },
            created_at = System.currentTimeMillis(),
            updated_at = System.currentTimeMillis()
        )
    }
}