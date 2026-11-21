package kurozorakit.store

import kurozorakit.data.enums.KKLibrary
import kurozorakit.data.models.country.Country
import kurozorakit.data.models.language.Language
import kurozorakit.data.models.library.LibraryAttributes
import kurozorakit.data.models.media.MediaStat
import kurozorakit.data.models.show.Show
import kurozorakit.data.models.show.attributes.AdaptationSource
import kurozorakit.data.models.show.attributes.AiringStatus
import kurozorakit.data.models.show.attributes.MediaType
import kurozorakit.data.models.show.attributes.TVRating
import kurozorakit.store.db.ShowEntity
import kurozorakit.store.mapper.ShowMapper
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Builds a fully-populated [Show] fixture for tests.
 */
fun buildTestShow(
    id: String = "1",
    title: String = "Test Anime",
    slug: String = "test-anime",
    isNSFW: Boolean = false,
    libraryStatus: KKLibrary.Status? = null,
): Show {
    return Show(
        id = id,
        type = "shows",
        href = "https://api.kurozora.app/v1/anime/$id",
        attributes = Show.Attributes(
            slug = slug,
            title = title,
            originalTitle = "テストアニメ",
            tagline = "A test show",
            synopsis = "This is a test anime used in unit tests.",
            genres = listOf("Action", "Drama"),
            themes = listOf("Space", "Mecha"),
            studio = "Test Studio",
            languages = listOf(
                Language(name = "Japanese", code = "ja", iso6393 = "jpn"),
                Language(name = "English", code = "en", iso6393 = "eng"),
            ),
            countryOfOrigin = Country(name = "Japan", code = "JP", iso31663 = "JPN"),
            tvRating = TVRating(name = "PG-13", description = "Teens 13 or older"),
            type = MediaType(name = "TV", description = "TV Series"),
            source = AdaptationSource(name = "Manga", description = "Manga"),
            status = AiringStatus(name = "Completed", description = "Finished airing", color = "#00FF00"),
            episodeCount = 12,
            seasonCount = 1,
            stats = MediaStat(
                ratingCountList = listOf(10, 20, 30),
                ratingAverage = 8.2,
                ratingCount = 60,
                rankGlobal = 100,
                rankTotal = 500,
                sentiment = "positive",
                highestRatingPercentage = 0.75,
            ),
            startedAt = Instant.parse("2024-01-05T12:00:00Z"),
            endedAt = Instant.parse("2024-03-28T12:00:00Z"),
            nextBroadcastAt = null,
            duration = "24m",
            durationCount = 24,
            durationTotal = "4h 48m",
            durationTotalCount = 288,
            airSeason = "Winter 2024",
            airDay = "Saturday",
            isNSFW = isNSFW,
            copyright = "© 2024 Test Studio",
            library = libraryStatus?.let { LibraryAttributes(status = it) },
        ),
        relationships = Show.Relationships(),
    )
}

class ShowMapperTest {

    @Test
    fun `round trip preserves all scalar fields`() {
        val show = buildTestShow(
            id = "42",
            title = "Full Metal Alchemist",
            slug = "full-metal-alchemist",
            isNSFW = true,
            libraryStatus = KKLibrary.Status.INPROGRESS,
        )

        val params = ShowMapper.toEntityParams(show)
        val entity = ShowEntity(
            id = params.id,
            type = params.type,
            href = params.href,
            slug = params.slug,
            title = params.title,
            original_title = params.originalTitle,
            synopsis = params.synopsis,
            tagline = params.tagline,
            video_url = params.videoUrl,
            poster_json = params.posterJson,
            banner_json = params.bannerJson,
            logo_json = params.logoJson,
            studio = params.studio,
            tv_rating_json = params.tvRatingJson,
            media_type_json = params.mediaTypeJson,
            source_json = params.sourceJson,
            status_json = params.statusJson,
            episode_count = params.episodeCount,
            season_count = params.seasonCount,
            duration = params.duration,
            duration_count = params.durationCount,
            duration_total = params.durationTotal,
            duration_total_count = params.durationTotalCount,
            air_season = params.airSeason,
            air_time = params.airTime,
            air_day = params.airDay,
            is_nsfw = params.isNsfw,
            copyright = params.copyright,
            started_at = params.startedAt,
            ended_at = params.endedAt,
            next_broadcast_at = params.nextBroadcastAt,
            anidb_id = params.anidbId,
            anilist_id = params.anilistId,
            anime_planet_id = params.animePlanetId,
            anisearch_id = params.anisearchId,
            imdb_id = params.imdbId,
            kitsu_id = params.kitsuId,
            mal_id = params.malId,
            notify_id = params.notifyId,
            syoboi_id = params.syoboiId,
            trakt_id = params.traktId,
            tvdb_id = params.tvdbId,
            library_json = params.libraryJson,
            genres_json = params.genresJson,
            themes_json = params.themesJson,
            synonym_titles_json = params.synonymTitlesJson,
            languages_json = params.languagesJson,
            country_of_origin_json = params.countryOfOriginJson,
            stats_json = params.statsJson,
            relationships_json = params.relationshipsJson,
            created_at = params.createdAt,
            updated_at = params.updatedAt,
        )

        val restored = ShowMapper.toDomain(entity)

        assertEquals(show.id, restored.id)
        assertEquals(show.type, restored.type)
        assertEquals(show.href, restored.href)
        assertEquals(show.attributes.slug, restored.attributes.slug)
        assertEquals(show.attributes.title, restored.attributes.title)
        assertEquals(show.attributes.originalTitle, restored.attributes.originalTitle)
        assertEquals(show.attributes.synopsis, restored.attributes.synopsis)
        assertEquals(show.attributes.genres, restored.attributes.genres)
        assertEquals(show.attributes.themes, restored.attributes.themes)
        assertEquals(show.attributes.languages, restored.attributes.languages)
        assertEquals(show.attributes.countryOfOrigin, restored.attributes.countryOfOrigin)
        assertEquals(show.attributes.tvRating, restored.attributes.tvRating)
        assertEquals(show.attributes.type, restored.attributes.type)
        assertEquals(show.attributes.source, restored.attributes.source)
        assertEquals(show.attributes.status, restored.attributes.status)
        assertEquals(show.attributes.episodeCount, restored.attributes.episodeCount)
        assertEquals(show.attributes.seasonCount, restored.attributes.seasonCount)
        assertEquals(show.attributes.stats, restored.attributes.stats)
        assertEquals(show.attributes.startedAt, restored.attributes.startedAt)
        assertEquals(show.attributes.endedAt, restored.attributes.endedAt)
        assertEquals(show.attributes.duration, restored.attributes.duration)
        assertEquals(show.attributes.isNSFW, restored.attributes.isNSFW)
        assertEquals(show.attributes.library?.status, restored.attributes.library?.status)
    }

    @Test
    fun `corrupted JSON columns degrade silently without crashing`() {
        val params = ShowMapper.toEntityParams(buildTestShow())
        val entity = ShowEntity(
            id = params.id,
            type = params.type,
            href = params.href,
            slug = params.slug,
            title = params.title,
            original_title = params.originalTitle,
            synopsis = params.synopsis,
            tagline = params.tagline,
            video_url = params.videoUrl,
            poster_json = "{broken json", // corrupted
            banner_json = null,
            logo_json = null,
            studio = params.studio,
            tv_rating_json = "not-json",
            media_type_json = "not-json",
            source_json = "not-json",
            status_json = "not-json",
            episode_count = params.episodeCount,
            season_count = params.seasonCount,
            duration = params.duration,
            duration_count = params.durationCount,
            duration_total = params.durationTotal,
            duration_total_count = params.durationTotalCount,
            air_season = params.airSeason,
            air_time = params.airTime,
            air_day = params.airDay,
            is_nsfw = params.isNsfw,
            copyright = params.copyright,
            started_at = params.startedAt,
            ended_at = params.endedAt,
            next_broadcast_at = params.nextBroadcastAt,
            anidb_id = params.anidbId,
            anilist_id = params.anilistId,
            anime_planet_id = params.animePlanetId,
            anisearch_id = params.anisearchId,
            imdb_id = params.imdbId,
            kitsu_id = params.kitsuId,
            mal_id = params.malId,
            notify_id = params.notifyId,
            syoboi_id = params.syoboiId,
            trakt_id = params.traktId,
            tvdb_id = params.tvdbId,
            library_json = params.libraryJson,
            genres_json = params.genresJson,
            themes_json = params.themesJson,
            synonym_titles_json = params.synonymTitlesJson,
            languages_json = params.languagesJson,
            country_of_origin_json = params.countryOfOriginJson,
            stats_json = params.statsJson,
            relationships_json = params.relationshipsJson,
            created_at = params.createdAt,
            updated_at = params.updatedAt,
        )

        val restored = ShowMapper.toDomain(entity)

        // Corrupted optional columns fall back to defaults; scalar fields survive.
        assertNull(restored.attributes.poster)
        assertEquals(TVRating("", ""), restored.attributes.tvRating)
        assertEquals(MediaType("", ""), restored.attributes.type)
        assertEquals(AdaptationSource("", ""), restored.attributes.source)
        assertEquals(AiringStatus("", "", ""), restored.attributes.status)
        assertNotNull(restored.attributes.title)
    }
}
