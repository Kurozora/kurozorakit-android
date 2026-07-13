package kurozorakit.store.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kurozorakit.data.models.person.Person
import kurozorakit.store.PersonKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.mapper.PersonMapper
import org.mobilenativefoundation.store.store5.SourceOfTruth

class PersonSourceOfTruth(private val database: KurozoraDatabase) {
    private val queries = database.personQueries

    fun forSingle(): SourceOfTruth<PersonKey.ById, Person, Person> = SourceOfTruth.of(
        reader = { key ->
            queries.selectById(key.id).asFlow().mapToOneOrNull(Dispatchers.IO).map {
                it?.let { PersonMapper.toDomain(it) }
            }
        },
        writer = { _, person -> queries.insertOrReplace(PersonMapper.toEntity(person)) }
    )

    fun forList(): SourceOfTruth<PersonKey.ByIds, List<Person>, List<Person>> = SourceOfTruth.of(
        reader = { key ->
            queries.selectByIds(key.ids).asFlow().mapToList(Dispatchers.IO).map { list ->
                list.map { PersonMapper.toDomain(it) }
            }
        },
        writer = { _, list ->
            database.transaction {
                list.forEach { queries.insertOrReplace(PersonMapper.toEntity(it)) }
            }
        }
    )
}