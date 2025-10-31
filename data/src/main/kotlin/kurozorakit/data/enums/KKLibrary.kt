package kurozorakit.data.enums

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IntEnumSerializer

object KKLibrary {
    // MARK: - Kind
    @Serializable
    enum class Kind(val value: Int) {
        SHOWS(0),
        LITERATURES(1),
        GAMES(2);

        val stringValue: String
            get() = when (this) {
                SHOWS -> "Shows"
                LITERATURES -> "Literatures"
                GAMES -> "Games"
            }
    }

    // MARK: - Status
    @Serializable(with = IntEnumSerializer::class)
    enum class Status(val value: Int) {
        NONE(-1),
        INPROGRESS(0),
        PLANNING(2),
        COMPLETED(3),
        ONHOLD(4),
        DROPPED(1),
        INTERESTED(6),
        IGNORED(5);

        val stringValue: String
            get() = when (this) {
                NONE -> "None"
                INPROGRESS -> "In Progress"
                PLANNING -> "Planning"
                COMPLETED -> "Completed"
                ONHOLD -> "On-Hold"
                DROPPED -> "Dropped"
                INTERESTED -> "Interested"
                IGNORED -> "Ignored"
            }
        val sectionValue: String
            get() = when (this) {
                INPROGRESS -> "InProgress"
                ONHOLD -> "OnHold"
                else -> stringValue
            }

        companion object {
            val all =
                listOf(INPROGRESS, PLANNING, COMPLETED, ONHOLD, DROPPED, INTERESTED, IGNORED)
            fun fromInt(value: Int): Status? = entries.find { it.value == value }
        }
    }

    // MARK: - SortType
    @Serializable
    enum class SortType(val value: Int) {
        NONE(0),
        ALPHABETICALLY(1),
        POPULARITY(2),
        DATE(3),
        RATING(4),
        MYRATING(5);

        val stringValue: String
            get() = when (this) {
                NONE -> "None"
                ALPHABETICALLY -> "Alphabetically"
                POPULARITY -> "Popularity"
                DATE -> "Date"
                RATING -> "Rating"
                MYRATING -> "My Rating"
            }
        val parameterValue: String
            get() = when (this) {
                NONE -> ""
                ALPHABETICALLY -> "title"
                POPULARITY -> "popularity"
                DATE -> "date"
                RATING -> "rating"
                MYRATING -> "my-rating"
            }
        val optionValue: List<Option>
            get() = when (this) {
                NONE -> emptyList()
                ALPHABETICALLY -> listOf(Option.ASCENDING, Option.DESCENDING)
                POPULARITY -> listOf(Option.MOST, Option.LEAST)
                DATE -> listOf(Option.NEWEST, Option.OLDEST)
                RATING, MYRATING -> listOf(Option.BEST, Option.WORST)
            }

        companion object {
            val all = listOf(ALPHABETICALLY, POPULARITY, DATE, RATING, MYRATING)
            fun fromInt(value: Int): SortType? = entries.find { it.value == value }
        }
    }

    // MARK: - SortType Option
    @Serializable
    enum class Option(val value: Int) {
        NONE(0),
        ASCENDING(1),
        DESCENDING(2),
        MOST(3),
        LEAST(4),
        NEWEST(5),
        OLDEST(6),
        WORST(7),
        BEST(8);

        val stringValue: String
            get() = when (this) {
                NONE -> "None"
                ASCENDING -> "A-Z"
                DESCENDING -> "Z-A"
                MOST -> "Most"
                LEAST -> "Least"
                NEWEST -> "Newest"
                OLDEST -> "Oldest"
                WORST -> "Worst"
                BEST -> "Best"
            }
        val parameterValue: String
            get() = when (this) {
                NONE -> "()"
                ASCENDING -> "(asc)"
                DESCENDING -> "(desc)"
                MOST -> "(most)"
                LEAST -> "(least)"
                NEWEST -> "(newest)"
                OLDEST -> "(oldest)"
                BEST -> "(best)"
                WORST -> "(worst)"
            }

        companion object {
            val all = listOf(ASCENDING, DESCENDING, NEWEST, OLDEST, WORST, BEST)
            fun fromInt(value: Int): Option? = entries.find { it.value == value }
        }
    }
}
