package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

@Serializable
data class PersonFilter(
    val astrologicalSign: Int? = null,
    val birthDate: Long? = null,
    val deceasedDate: Long? = null
) : Filterable {
    override fun toFilterMap() = mapOf(
        "astrological_sign" to astrologicalSign,
        "birthdate" to birthDate,
        "deceased_date" to deceasedDate
    ).filterValues { it != null }
}