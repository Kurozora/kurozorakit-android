package kurozorakit.store.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kurozorakit.data.models.character.Character
import kurozorakit.store.CharacterKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.mapper.CharacterMapper
import org.mobilenativefoundation.store.store5.SourceOfTruth

class CharacterSourceOfTruth(private val database: KurozoraDatabase) {
    private val queries = database.characterQueries

    fun forSingle(): SourceOfTruth<CharacterKey.ById, Character, Character> = SourceOfTruth.of(
        reader = { key ->
            queries.selectById(key.id).asFlow().mapToOneOrNull(Dispatchers.IO).map {
                it?.let { CharacterMapper.toDomain(it) }
            }
        },
        writer = { _, character -> queries.insertOrReplace(CharacterMapper.toEntity(character)) }
    )

    fun forList(): SourceOfTruth<CharacterKey.ByIds, List<Character>, List<Character>> = SourceOfTruth.of(
        reader = { key ->
            queries.selectByIds(key.ids).asFlow().mapToList(Dispatchers.IO).map { list ->
                // An empty result must be reported as `null` so Store5 knows
                // there is no cached data and invokes the fetcher. An empty list
                // would be treated as existing data and the list would stay empty.
                if (list.isEmpty()) null else list.map { CharacterMapper.toDomain(it) }
            }
        },
        writer = { _, list ->
            database.transaction {
                list.forEach { queries.insertOrReplace(CharacterMapper.toEntity(it)) }
            }
        }
    )
}