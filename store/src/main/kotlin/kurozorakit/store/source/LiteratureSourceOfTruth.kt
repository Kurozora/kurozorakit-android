package kurozorakit.store.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kurozorakit.data.models.literature.Literature
import kurozorakit.store.LiteratureKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.mapper.LiteratureMapper
import org.mobilenativefoundation.store.store5.SourceOfTruth

class LiteratureSourceOfTruth(private val database: KurozoraDatabase) {
    private val queries = database.literatureQueries

    fun forSingle(): SourceOfTruth<LiteratureKey.ById, Literature, Literature> = SourceOfTruth.of(
        reader = { key ->
            queries.selectById(key.id).asFlow().mapToOneOrNull(Dispatchers.IO).map {
                it?.let { LiteratureMapper.toDomain(it) }
            }
        },
        writer = { _, lit -> queries.insertOrReplace(LiteratureMapper.toEntity(lit)) }
    )

    fun forList(): SourceOfTruth<LiteratureKey.ByIds, List<Literature>, List<Literature>> = SourceOfTruth.of(
        reader = { key ->
            queries.selectByIds(key.ids).asFlow().mapToList(Dispatchers.IO).map { list ->
                list.map { LiteratureMapper.toDomain(it) }
            }
        },
        writer = { _, list ->
            database.transaction { list.forEach { queries.insertOrReplace(LiteratureMapper.toEntity(it)) } }
        }
    )
}