package kurozorakit.store.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kurozorakit.data.models.studio.Studio
import kurozorakit.store.StudioKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.mapper.StudioMapper
import org.mobilenativefoundation.store.store5.SourceOfTruth

class StudioSourceOfTruth(private val database: KurozoraDatabase) {
    private val queries = database.studioQueries

    fun forSingle(): SourceOfTruth<StudioKey.ById, Studio, Studio> = SourceOfTruth.of(
        reader = { key ->
            queries.selectById(key.id).asFlow().mapToOneOrNull(Dispatchers.IO).map {
                it?.let { StudioMapper.toDomain(it) }
            }
        },
        writer = { _, studio -> queries.insertOrReplace(StudioMapper.toEntity(studio)) }
    )

    fun forAll(): SourceOfTruth<StudioKey.All, List<Studio>, List<Studio>> = SourceOfTruth.of(
        reader = { _ ->
            queries.selectAll().asFlow().mapToList(Dispatchers.IO).map { list ->
                // An empty result must be reported as `null` so Store5 knows
                // there is no cached data and invokes the fetcher. An empty list
                // would be treated as existing data and the list would stay empty.
                if (list.isEmpty()) null else list.map { StudioMapper.toDomain(it) }
            }
        },
        writer = { _, list ->
            database.transaction {
                list.forEach { queries.insertOrReplace(StudioMapper.toEntity(it)) }
            }
        }
    )
}