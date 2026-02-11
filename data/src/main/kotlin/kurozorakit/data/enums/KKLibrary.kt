package kurozorakit.data.enums

import kotlinx.serialization.Serializable
import kurozorakit.data.models.IntEnumSerializer

/**
 * A container for all library-related enums used to manage user collections.
 *
 * This object groups enums that define the type of media, the user's progress status,
 * and the sorting options available for the library view.
 */
object KKLibrary {

    /**
     * Represents the broad classification of media entries in the library.
     *
     * @property value The unique integer identifier for the media kind.
     */
    @Serializable
    enum class Kind(val value: Int) {
        /** Animated series, movies, or specials. */
        SHOWS(0),
        /** Printed or digital reading material like Manga and Light Novels. */
        LITERATURES(1),
        /** Interactive video game titles. */
        GAMES(2);

        /** The human-readable label for the media kind. */
        val stringValue: String
            get() = when (this) {
                SHOWS -> "Shows"
                LITERATURES -> "Literatures"
                GAMES -> "Games"
            }
    }

    /**
     * Represents the user's personal progress or interest level for a library item.
     *
     * @property value The integer ID used by the API and [IntEnumSerializer].
     */
    @Serializable(with = IntEnumSerializer::class)
    enum class Status(val value: Int) {
        /** No specific status assigned. */
        NONE(-1),
        /** Currently watching, reading, or playing. */
        INPROGRESS(0),
        /** Intending to start in the future. */
        PLANNING(2),
        /** Finished the media entirely. */
        COMPLETED(3),
        /** Temporarily paused progress. */
        ONHOLD(4),
        /** Decided to stop progress before completion. */
        DROPPED(1),
        /** Has shown interest but hasn't moved to planning or progress yet. */
        INTERESTED(6),
        /** Explicitly marked as not interested to hide from recommendations. */
        IGNORED(5);

        /** The human-readable label for the status. */
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

        /** The internal section name used for grouping items in the UI. */
        val sectionValue: String
            get() = when (this) {
                INPROGRESS -> "InProgress"
                ONHOLD -> "OnHold"
                else -> stringValue
            }

        companion object {
            /** A list of all valid user-selectable statuses. */
            val all = listOf(INPROGRESS, PLANNING, COMPLETED, ONHOLD, DROPPED, INTERESTED, IGNORED)

            /** Matches an integer ID to its corresponding [Status]. */
            fun fromInt(value: Int): Status? = entries.find { it.value == value }
        }
    }

    /**
     * Defines the criteria by which library items can be ordered.
     *
     * @property value The integer ID for the sort type.
     */
    @Serializable
    enum class SortType(val value: Int) {
        /** No specific sorting applied. */
        NONE(0),
        /** Order by the title of the media. */
        ALPHABETICALLY(1),
        /** Order by global community engagement. */
        POPULARITY(2),
        /** Order by release or addition date. */
        DATE(3),
        /** Order by the global community rating. */
        RATING(4),
        /** Order by the user's personal score. */
        MYRATING(5);

        /** Human-readable name of the sort criteria. */
        val stringValue: String
            get() = when (this) {
                NONE -> "None"
                ALPHABETICALLY -> "Alphabetically"
                POPULARITY -> "Popularity"
                DATE -> "Date"
                RATING -> "Rating"
                MYRATING -> "My Rating"
            }

        /** The query parameter key used when making API requests. */
        val parameterValue: String
            get() = when (this) {
                NONE -> ""
                ALPHABETICALLY -> "title"
                POPULARITY -> "popularity"
                DATE -> "date"
                RATING -> "rating"
                MYRATING -> "my-rating"
            }

        /** Returns the valid [Option] set (e.g., Ascending vs Descending) for this sort type. */
        val optionValue: List<Option>
            get() = when (this) {
                NONE -> emptyList()
                ALPHABETICALLY -> listOf(Option.ASCENDING, Option.DESCENDING)
                POPULARITY -> listOf(Option.MOST, Option.LEAST)
                DATE -> listOf(Option.NEWEST, Option.OLDEST)
                RATING, MYRATING -> listOf(Option.BEST, Option.WORST)
            }

        companion object {
            /** All available sorting criteria. */
            val all = listOf(ALPHABETICALLY, POPULARITY, DATE, RATING, MYRATING)

            /** Matches an integer ID to its [SortType]. */
            fun fromInt(value: Int): SortType? = entries.find { it.value == value }
        }
    }

    /**
     * Defines the direction or modifier for a [SortType].
     */
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

        /** Human-readable shorthand for the direction (e.g., "A-Z"). */
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

        /** The suffix added to the API query parameter to specify direction. */
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
            /** All common sorting modifiers. */
            val all = listOf(ASCENDING, DESCENDING, NEWEST, OLDEST, WORST, BEST)

            /** Matches an integer ID to its [Option]. */
            fun fromInt(value: Int): Option? = entries.find { it.value == value }
        }
    }
}