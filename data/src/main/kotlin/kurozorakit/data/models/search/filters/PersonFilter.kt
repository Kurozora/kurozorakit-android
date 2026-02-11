package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

/**
 * Filter criteria for querying people (Staff, Voice Actors, Creators).
 *
 * This model allows for searching individuals based on their life timeline and
 * astrological associations. It is primarily used to discover staff members
 * or voice actors (Seiyuu) sharing specific biological milestones.
 *
 * @property astrologicalSign Filters individuals by their zodiac sign using [FilterValue]
 * for inclusion or exclusion logic.
 * @property birthDate Unix timestamp representing the person's date of birth.
 * @property deceasedDate Unix timestamp representing the date of passing, if applicable.
 */
@Serializable
data class PersonFilter(
    val astrologicalSign: FilterValue? = null,
    val birthDate: Long? = null,
    val deceasedDate: Long? = null
) : Filterable {

    /**
     * Maps the person-specific filter properties to API query parameters.
     * * Converts Kotlin property names to the snake_case keys expected by the backend.
     * * Ensures that [birthDate] and [deceasedDate] are passed as numerical timestamps.
     *
     * @param forLibrary Required by [Filterable], though person resources are typically
     * not part of a "trackable" library in the same way media is.
     * @return A map of non-null filter parameters.
     */
    override fun toFilterMap(forLibrary: Boolean) = mapOf(
        "astrological_sign" to astrologicalSign,
        "birthdate" to birthDate,
        "deceased_date" to deceasedDate
    ).filterValues { it != null }
}