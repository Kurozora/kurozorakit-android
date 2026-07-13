package kurozorakit.store.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.data.models.genre.Genre
import kurozorakit.store.GenreKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.mapper.GenreMapper
import org.mobilenativefoundation.store.store5.SourceOfTruth

class GenreSourceOfTruth(
    private val database: KurozoraDatabase
) {
    private val queries get() = database.genreQueries

    fun forSingleGenre(): SourceOfTruth<GenreKey.ById, Genre, Genre> = SourceOfTruth.of(
        reader = { key ->
            queries.selectById(key.id).asFlow().mapToOneOrNull(Dispatchers.IO).map { entity ->
                entity?.let { GenreMapper.toDomain(it) }
            }
        },
        writer = { _, genre -> insertGenre(genre) }
    )

    fun forGenreList(): SourceOfTruth<GenreKey.All, List<Genre>, List<Genre>> = SourceOfTruth.of(
        reader = { _ ->
            queries.selectAll().asFlow().mapToList(Dispatchers.IO).map { entities ->
                entities.map { GenreMapper.toDomain(it) }
            }
        },
        writer = { _, genres ->
            database.transaction {
                genres.forEach { insertGenre(it) }
            }
        }
    )

    private fun insertGenre(genre: Genre) {
        val p = GenreMapper.toEntityParams(genre)
        queries.insertOrReplace(
            p.id, p.type, p.href, p.slug, p.name, p.description,
            p.backgroundColor1, p.backgroundColor2, p.textColor1, p.textColor2,
            p.symbolJson, p.isNsfw, p.createdAt, p.updatedAt
        )
    }
}