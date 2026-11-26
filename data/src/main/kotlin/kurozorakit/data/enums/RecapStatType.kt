package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Identifies a specific highlight statistic inside a user's periodic Re:CAP stats.
 */
@Serializable(with = RecapStatTypeSerializer::class)
enum class RecapStatType(val rawValue: Int) {
    busiestWeekday(1),
    busiestHour(2),
    longestStreak(3),
    biggestBinge(4),
    firstTitle(5),
    lastTitle(6),
    topProvider(7),
    ratingsGiven(8),
    averageRating(9),
    reviewsWritten(10),
    titlesCompleted(11),
    titlesDropped(12),
    titlesAdded(13),
    favoritesAdded(14),
    achievementsEarned(15);

    companion object {
        fun fromInt(value: Int): RecapStatType? = entries.find { it.rawValue == value }
    }
}

object RecapStatTypeSerializer : IntValueEnumSerializer<RecapStatType>(
    serialName = "RecapStatType",
    entries = RecapStatType.entries,
    valueSelector = { it.rawValue },
    fallback = RecapStatType.busiestWeekday,
)
