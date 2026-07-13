package kurozorakit.store.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kurozorakit.data.models.theme.Theme
import kurozorakit.store.ThemeKey
import kurozorakit.store.db.KurozoraDatabase
import kurozorakit.store.mapper.ThemeMapper
import org.mobilenativefoundation.store.store5.SourceOfTruth

class ThemeSourceOfTruth(private val database: KurozoraDatabase) {
    private val queries get() = database.themeQueries

    fun forSingleTheme(): SourceOfTruth<ThemeKey.ById, Theme, Theme> = SourceOfTruth.of(
        reader = { key ->
            queries.selectById(key.id).asFlow().mapToOneOrNull(Dispatchers.IO).map { entity ->
                entity?.let { ThemeMapper.toDomain(it) }
            }
        },
        writer = { _, theme -> insertTheme(theme) }
    )

    fun forThemeList(): SourceOfTruth<ThemeKey.All, List<Theme>, List<Theme>> = SourceOfTruth.of(
        reader = { _ ->
            queries.selectAll().asFlow().mapToList(Dispatchers.IO).map { entities ->
                entities.map { ThemeMapper.toDomain(it) }
            }
        },
        writer = { _, themes ->
            database.transaction {
                themes.forEach { insertTheme(it) }
            }
        }
    )

    private fun insertTheme(theme: Theme) {
        val p = ThemeMapper.toEntityParams(theme)
        queries.insertOrReplace(
            p.id, p.type, p.href, p.slug, p.name, p.description,
            p.backgroundColor1, p.backgroundColor2, p.textColor1, p.textColor2,
            p.symbolJson, p.isNsfw, p.createdAt, p.updatedAt
        )
    }
}