package kurozorakit.store.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kurozorakit.data.models.episode.Episode
import kurozorakit.store.EpisodeKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.mapper.EpisodeMapper
import org.mobilenativefoundation.store.store5.SourceOfTruth

class EpisodeSourceOfTruth(private val database: KurozoraDatabase) {
    private val queries = database.episodeQueries

    fun forSingle(): SourceOfTruth<EpisodeKey.ById, Episode, Episode> = SourceOfTruth.of(
        reader = { key ->
            queries.selectById(key.id).asFlow().mapToOneOrNull(Dispatchers.IO).map {
                it?.let { EpisodeMapper.toDomain(it) }
            }
        },
        writer = { _, episode -> queries.insertOrReplace(EpisodeMapper.toEntity(episode)) }
    )

    fun forList(): SourceOfTruth<EpisodeKey.ByIds, List<Episode>, List<Episode>> = SourceOfTruth.of(
        reader = { key ->
            queries.selectByIds(key.ids).asFlow().mapToList(Dispatchers.IO).map { list ->
                // An empty result must be reported as `null` so Store5 knows
                // there is no cached data and invokes the fetcher. An empty list
                // would be treated as existing data and the list would stay empty.
                if (list.isEmpty()) null else list.map { EpisodeMapper.toDomain(it) }
            }
        },
        writer = { _, list ->
            database.transaction {
                list.forEach { queries.insertOrReplace(EpisodeMapper.toEntity(it)) }
            }
        }
    )
}