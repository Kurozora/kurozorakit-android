package kurozorakit.data.models.search.filters

import kotlinx.serialization.Serializable
import kurozorakit.data.models.Filterable

@Serializable
data class CharacterFilter(
    val age: Int? = null,
    val astrologicalSign: FilterValue? = null,
    val birthDay: Int? = null,
    val birthMonth: Int? = null,
    val bust: String? = null,
    val height: String? = null,
    val hip: String? = null,
    val status: String? = null,
    val waist: String? = null,
    val weight: String? = null
) : Filterable {
    override fun toFilterMap(forLibrary: Boolean) = mapOf(
        "age" to age,
        "astrological_sign" to astrologicalSign,
        "birth_day" to birthDay,
        "birth_month" to birthMonth,
        "bust" to bust,
        "height" to height,
        "hip" to hip,
        "status" to status,
        "waist" to waist,
        "weight" to weight
    ).filterValues { it != null }
}
