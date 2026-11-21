package kurozorakit.store.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kurozorakit.data.models.game.Game
import kurozorakit.store.GameKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.mapper.GameMapper
import org.mobilenativefoundation.store.store5.SourceOfTruth

class GameSourceOfTruth(private val database: KurozoraDatabase) {
    private val queries = database.gameQueries

    fun forSingle(): SourceOfTruth<GameKey.ById, Game, Game> = SourceOfTruth.of(
        reader = { key ->
            queries.selectById(key.id).asFlow().mapToOneOrNull(Dispatchers.IO).map {
                it?.let { GameMapper.toDomain(it) }
            }
        },
        writer = { _, game -> queries.insertOrReplace(GameMapper.toEntity(game)) }
    )

    fun forList(): SourceOfTruth<GameKey.ByIds, List<Game>, List<Game>> = SourceOfTruth.of(
        reader = { key ->
            queries.selectByIds(key.ids).asFlow().mapToList(Dispatchers.IO).map { list ->
                // An empty result must be reported as `null` so Store5 knows
                // there is no cached data and invokes the fetcher. An empty list
                // would be treated as existing data and the list would stay empty.
                if (list.isEmpty()) null else list.map { GameMapper.toDomain(it) }
            }
        },
        writer = { _, list ->
            database.transaction {
                list.forEach { queries.insertOrReplace(GameMapper.toEntity(it)) }
            }
        }
    )
}