package kurozorakit.store.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kurozorakit.data.models.explore.ExploreCategory
import kurozorakit.store.ExploreKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.mapper.ExploreMapper
import org.mobilenativefoundation.store.store5.SourceOfTruth

class ExploreSourceOfTruth(private val database: KurozoraDatabase) {
    private val queries = database.exploreQueries

    fun forAll(): SourceOfTruth<ExploreKey.All, List<ExploreCategory>, List<ExploreCategory>> = SourceOfTruth.of(
        reader = { _ ->
            queries.selectAll().asFlow().mapToList(Dispatchers.IO).map { list ->
                // An empty result must be reported as `null` so Store5 knows there
                // is no cached data and invokes the fetcher. An empty list would be
                // treated as existing data and the explore feed would stay empty.
                if (list.isEmpty()) null else list.map { ExploreMapper.toDomain(it) }
            }
        },
        writer = { _, list ->
            database.transaction {
                // Keşfet listesi her yenilendiğinde eski sırayı temizlemek mantıklıdır
                queries.deleteAll()
                list.forEach { queries.insertOrReplace(ExploreMapper.toEntity(it)) }
            }
        }
    )

    fun forSingle(): SourceOfTruth<ExploreKey.ById, ExploreCategory, ExploreCategory> = SourceOfTruth.of(
        reader = { key ->
            queries.selectById(key.id).asFlow().mapToOneOrNull(Dispatchers.IO).map {
                it?.let { ExploreMapper.toDomain(it) }
            }
        },
        writer = { _, category -> queries.insertOrReplace(ExploreMapper.toEntity(category)) }
    )
}