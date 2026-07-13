package kurozorakit.store.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kurozorakit.data.models.season.Season
import kurozorakit.store.SeasonKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.mapper.SeasonMapper
import org.mobilenativefoundation.store.store5.SourceOfTruth

class SeasonSourceOfTruth(private val database: KurozoraDatabase) {
    private val queries = database.seasonQueries

    fun forSingle(): SourceOfTruth<SeasonKey.ById, Season, Season> = SourceOfTruth.of(
        reader = { key ->
            queries.selectById(key.id).asFlow().mapToOneOrNull(Dispatchers.IO).map {
                it?.let { SeasonMapper.toDomain(it) }
            }
        },
        writer = { _, season -> queries.insertOrReplace(SeasonMapper.toEntity(season)) }
    )
}