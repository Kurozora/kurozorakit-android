package kurozorakit.store.mapper

import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kurozorakit.data.models.country.Country
import kurozorakit.data.models.language.Language
import kurozorakit.data.models.library.LibraryAttributes
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.media.MediaStat
import kurozorakit.data.models.show.Show
import kurozorakit.data.models.show.attributes.AdaptationSource
import kurozorakit.data.models.show.attributes.AiringStatus
import kurozorakit.data.models.show.attributes.MediaType
import kurozorakit.data.models.show.attributes.TVRating
import kurozorakit.store.db.ShowEntity

/**
 * Mapper for converting between [Show] domain model and [ShowEntity] database entity.
 *
 * Handles serialization of complex nested objects (Media, Stats, Languages) to JSON strings
 * for SQLite storage, and deserialization back to domain objects.
 */
object ShowMapper {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    /**
     * Maps a database [ShowEntity] to a domain [Show] model.
     */
    fun toDomain(entity: ShowEntity): Show {
        return Show(
            id = entity.id,
            type = entity.type,
            href = entity.href,
            attributes = Show.Attributes(
                anidbID = entity.anidb_id?.toInt(),
                anilistID = entity.anilist_id?.toInt(),
                animePlanetID = entity.anime_planet_id,
                anisearchID = entity.anisearch_id?.toInt(),
                imdbID = entity.imdb_id,
                kitsuID = entity.kitsu_id?.toInt(),
                malID = entity.mal_id?.toInt(),
                notifyID = entity.notify_id,
                syoboiID = entity.syoboi_id?.toInt(),
                traktID = entity.trakt_id?.toInt(),
                tvdbID = entity.tvdb_id?.toInt(),
                slug = entity.slug,
                videoUrl = entity.video_url,
                poster = entity.poster_json?.let { parseMedia(it) },
                banner = entity.banner_json?.let { parseMedia(it) },
                logo = entity.logo_json?.let { parseMedia(it) },
                originalTitle = entity.original_title,
                title = entity.title,
                synonymTitles = entity.synonym_titles_json?.let { parseStringList(it) },
                tagline = entity.tagline,
                synopsis = entity.synopsis,
                genres = entity.genres_json?.let { parseStringList(it) },
                themes = entity.themes_json?.let { parseStringList(it) },
                studio = entity.studio,
                languages = entity.languages_json?.let { parseLanguages(it) } ?: emptyList(),
                countryOfOrigin = entity.country_of_origin_json?.let { parseCountry(it) },
                tvRating = entity.tv_rating_json?.let { parseTVRating(it) } ?: TVRating("",""),
                type = entity.media_type_json?.let { parseMediaType(it) } ?: MediaType("",""),
                source = entity.source_json?.let { parseAdaptationSource(it) } ?: AdaptationSource("",""),
                status = entity.status_json?.let { parseAiringStatus(it) } ?: AiringStatus("","",""),
                episodeCount = entity.episode_count.toInt(),
                seasonCount = entity.season_count.toInt(),
                stats = entity.stats_json?.let { parseStats(it) },
                startedAt = entity.started_at?.let { Instant.fromEpochMilliseconds(it) },
                endedAt = entity.ended_at?.let { Instant.fromEpochMilliseconds(it) },
                nextBroadcastAt = entity.next_broadcast_at?.let { Instant.fromEpochMilliseconds(it) },
                duration = entity.duration,
                durationCount = entity.duration_count.toInt(),
                durationTotal = entity.duration_total,
                durationTotalCount = entity.duration_total_count.toInt(),
                airSeason = entity.air_season,
                airTime = entity.air_time?.let { safeParseInstant(it) },
                airDay = entity.air_day,
                isNSFW = entity.is_nsfw == 1L,
                copyright = entity.copyright,
                library = entity.library_json?.let { parseLibrary(it) }
            ),
            relationships = entity.relationships_json?.let { parseRelationships(it) }
        )
    }

    /**
     * Maps a domain [Show] model to database entity parameters for insertion.
     * Returns a lambda that can be used with the generated SQLDelight insert function.
     */
    fun toEntityParams(show: Show): ShowEntityParams {
        val attrs = show.attributes
        return ShowEntityParams(
            id = show.id,
            type = show.type,
            href = show.href,
            slug = attrs.slug,
            title = attrs.title,
            originalTitle = attrs.originalTitle,
            synopsis = attrs.synopsis,
            tagline = attrs.tagline,
            videoUrl = attrs.videoUrl,
            posterJson = attrs.poster?.let { encodeMedia(it) },
            bannerJson = attrs.banner?.let { encodeMedia(it) },
            logoJson = attrs.logo?.let { encodeMedia(it) },
            studio = attrs.studio,
            tvRatingJson = attrs.tvRating.let { json.encodeToString(it) },
            mediaTypeJson = attrs.type.let { json.encodeToString(it) },
            sourceJson = attrs.source.let { json.encodeToString(it) },
            statusJson = attrs.status.let { json.encodeToString(it) },
            episodeCount = attrs.episodeCount.toLong(),
            seasonCount = attrs.seasonCount.toLong(),
            duration = attrs.duration,
            durationCount = attrs.durationCount.toLong(),
            durationTotal = attrs.durationTotal,
            durationTotalCount = attrs.durationTotalCount.toLong(),
            airSeason = attrs.airSeason,
            airTime = attrs.airTime?.toString(),
            airDay = attrs.airDay,
            isNsfw = if (attrs.isNSFW) 1L else 0L,
            copyright = attrs.copyright,
            startedAt = attrs.startedAt?.toEpochMilliseconds(),
            endedAt = attrs.endedAt?.toEpochMilliseconds(),
            nextBroadcastAt = attrs.nextBroadcastAt?.toEpochMilliseconds(),
            anidbId = attrs.anidbID?.toLong(),
            anilistId = attrs.anilistID?.toLong(),
            animePlanetId = attrs.animePlanetID,
            anisearchId = attrs.anisearchID?.toLong(),
            imdbId = attrs.imdbID,
            kitsuId = attrs.kitsuID?.toLong(),
            malId = attrs.malID?.toLong(),
            notifyId = attrs.notifyID,
            syoboiId = attrs.syoboiID?.toLong(),
            traktId = attrs.traktID?.toLong(),
            tvdbId = attrs.tvdbID?.toLong(),
            libraryJson = attrs.library.let { json.encodeToString(it) },
            genresJson = attrs.genres?.let { json.encodeToString(it) },
            themesJson = attrs.themes?.let { json.encodeToString(it) },
            synonymTitlesJson = attrs.synonymTitles?.let { json.encodeToString(it) },
            languagesJson = json.encodeToString(attrs.languages),
            countryOfOriginJson = attrs.countryOfOrigin?.let { json.encodeToString(it) },
            statsJson = attrs.stats?.let { json.encodeToString(it) },
            relationshipsJson = show.relationships?.let { json.encodeToString(it) },
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }

    // Helper parsing functions
    private fun parseMedia(jsonStr: String): Media? = try {
        json.decodeFromString<Media>(jsonStr)
    } catch (e: Exception) {
        null
    }

    private fun encodeMedia(media: Media): String = json.encodeToString(media)

    private fun parseStringList(jsonStr: String): List<String> = try {
        json.decodeFromString<List<String>>(jsonStr)
    } catch (e: Exception) {
        emptyList()
    }

    private fun parseLanguages(jsonStr: String): List<Language> = try {
        json.decodeFromString<List<Language>>(jsonStr)
    } catch (e: Exception) {
        emptyList()
    }

    private fun parseCountry(jsonStr: String): Country? = try {
        json.decodeFromString<Country>(jsonStr)
    } catch (e: Exception) {
        null
    }

    private fun parseStats(jsonStr: String): MediaStat? = try {
        json.decodeFromString<MediaStat>(jsonStr)
    } catch (e: Exception) {
        null
    }

    private fun parseLibrary(jsonStr: String): LibraryAttributes? = try {
        json.decodeFromString<LibraryAttributes>(jsonStr)
    } catch (e: Exception) {
        null
    }

    private fun parseTVRating(jsonStr: String): TVRating = try {
        json.decodeFromString<TVRating>(jsonStr)
    } catch (e: Exception) {
        TVRating("","")
    }

    private fun parseMediaType(jsonStr: String): MediaType = try {
        json.decodeFromString<MediaType>(jsonStr)
    } catch (e: Exception) {
        MediaType("","")
    }

    private fun parseAdaptationSource(jsonStr: String): AdaptationSource = try {
        json.decodeFromString<AdaptationSource>(jsonStr)
    } catch (e: Exception) {
        AdaptationSource("","")
    }

    private fun parseAiringStatus(jsonStr: String): AiringStatus = try {
        json.decodeFromString<AiringStatus>(jsonStr)
    } catch (e: Exception) {
        AiringStatus("","","")
    }

    private fun parseRelationships(jsonStr: String): Show.Relationships? = try {
        json.decodeFromString<Show.Relationships>(jsonStr)
    } catch (e: Exception) {
        null
    }

    private fun safeParseInstant(value: String): Instant? = try {
        value.toInstant()
    } catch (e: Exception) {
        null
    }

}

/**
 * Data class holding all parameters needed for inserting a Show into the database.
 */
data class ShowEntityParams(
    val id: String,
    val type: String,
    val href: String,
    val slug: String,
    val title: String,
    val originalTitle: String?,
    val synopsis: String?,
    val tagline: String?,
    val videoUrl: String?,
    val posterJson: String?,
    val bannerJson: String?,
    val logoJson: String?,
    val studio: String?,
    val tvRatingJson: String,
    val mediaTypeJson: String,
    val sourceJson: String,
    val statusJson: String,
    val episodeCount: Long,
    val seasonCount: Long,
    val duration: String,
    val durationCount: Long,
    val durationTotal: String,
    val durationTotalCount: Long,
    val airSeason: String?,
    val airTime: String?,
    val airDay: String?,
    val isNsfw: Long,
    val copyright: String?,
    val startedAt: Long?,
    val endedAt: Long?,
    val nextBroadcastAt: Long?,
    val anidbId: Long?,
    val anilistId: Long?,
    val animePlanetId: String?,
    val anisearchId: Long?,
    val imdbId: String?,
    val kitsuId: Long?,
    val malId: Long?,
    val notifyId: String?,
    val syoboiId: Long?,
    val traktId: Long?,
    val tvdbId: Long?,
    val libraryJson: String?,
    val genresJson: String?,
    val themesJson: String?,
    val synonymTitlesJson: String?,
    val languagesJson: String,
    val countryOfOriginJson: String?,
    val statsJson: String?,
    val relationshipsJson: String?,
    val createdAt: Long,
    val updatedAt: Long
)
