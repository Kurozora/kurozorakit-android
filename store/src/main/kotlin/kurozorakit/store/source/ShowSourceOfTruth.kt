package kurozorakit.store.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.data.models.show.Show
import kurozorakit.shared.logging.KurozoraLogger
import kurozorakit.store.ShowKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.mapper.ShowMapper
import org.mobilenativefoundation.store.store5.SourceOfTruth

/**
 * Source of Truth implementation for [Show] entities using SQLDelight.
 *
 * This class manages the local database storage for shows, providing
 * reactive flows for reading and methods for writing/deleting data.
 */
class ShowSourceOfTruth(
    private val database: KurozoraDatabase
) {
    private val queries get() = database.showQueries

    /**
     * Creates a Store5 SourceOfTruth for single Show queries.
     */
    fun forSingleShow(): SourceOfTruth<ShowKey.ById, Show, Show> {
        return SourceOfTruth.of(
            reader = { key ->
                queries.selectById(key.id)
                    .asFlow()
                    .mapToOneOrNull(Dispatchers.IO)
                    .map { entity ->
                        try {
                            entity?.let { ShowMapper.toDomain(it) }
                        } catch (e: Exception) {
                            KurozoraLogger.error("[ShowSourceOfTruth]", "Mapping Hatası! Entity ve Domain uyumsuz: ${e.message}")
                            throw e
                        }
                    }
            },
            writer = { key, show ->
                try {
                    KurozoraLogger.debug("[ShowSourceOfTruth]", "Veritabanına Yazılıyor: ${show.id}")
                    insertShow(show)
                    KurozoraLogger.debug("[ShowSourceOfTruth]", "Yazma Başarılı.")
                } catch (e: Exception) {
                    KurozoraLogger.error("[ShowSourceOfTruth]", "Yazma Hatası! (SQLite Sorunu Olabilir): ${e.message}")
                    throw e
                }
            },
            delete = { key ->
                queries.deleteById(key.id)
            },
            deleteAll = {
                queries.deleteAll()
            }
        )
    }

    /**
     * Creates a Store5 SourceOfTruth for multiple Shows queries.
     */
    fun forShowList(): SourceOfTruth<ShowKey.ByIds, List<Show>, List<Show>> {
        return SourceOfTruth.of(
            reader = { key ->
                readShowsByIds(key.ids)
            },
            writer = { _, shows ->
                insertShows(shows)
            },
            delete = { key ->
                // For list queries, we typically don't delete by key
                // Individual shows are deleted separately
            },
            deleteAll = {
                queries.deleteAll()
            }
        )
    }

    // ==========================================================================
    // READ OPERATIONS
    // ==========================================================================


    private fun readShowsByIds(ids: List<String>): Flow<List<Show>?> {
        return queries.selectByIds(ids)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                if (entities.isEmpty()) null
                else entities.map { ShowMapper.toDomain(it) }
            }
    }

    // ==========================================================================
    // WRITE OPERATIONS
    // ==========================================================================

    /**
     * Inserts or updates a single show in the database.
     */
    fun insertShow(show: Show) {
        val params = ShowMapper.toEntityParams(show)
        queries.insertOrReplace(
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
            air_time = params.airTime.toString(),
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
            updated_at = params.updatedAt
        )
    }

    /**
     * Inserts or updates multiple shows in the database.
     */
    fun insertShows(shows: List<Show>) {
        database.transaction {
            shows.forEach { show ->
                insertShow(show)
            }
        }
    }

    /**
     * Updates library status for a show.
     */
//    fun updateLibraryStatus(showId: String, status: String?, isFavorited: Boolean) {
//        queries.updateLibraryStatus(
//            library_status = status,
//            is_favorited = if (isFavorited) 1L else 0L,
//            updated_at = System.currentTimeMillis(),
//            id = showId
//        )
//    }

    /**
     * Deletes stale entries older than the given timestamp.
     */
    fun deleteStaleEntries(olderThan: Long) {
        queries.deleteStale(olderThan)
    }

    /**
     * Gets count of all shows in the database.
     */
    fun getCount(): Long {
        return queries.countAll().executeAsOne()
    }
}
