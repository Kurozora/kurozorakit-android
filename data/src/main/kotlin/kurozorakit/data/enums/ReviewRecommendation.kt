package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Represents a reviewer's overall recommendation stance on a rated resource.
 *
 * @property rawValue The integer identifier returned by the Kurozora API.
 */
@Serializable(with = ReviewRecommendationSerializer::class)
enum class ReviewRecommendation(val rawValue: Int) {
    /** The reviewer does not recommend the media. */
    notRecommended(0),

    /** The reviewer has mixed feelings about the media. */
    mixedFeelings(1),

    /** The reviewer recommends the media. */
    recommended(2);

    /** Human-readable label for the recommendation. */
    val displayName: String
        get() = when (this) {
            notRecommended -> "Not Recommended"
            mixedFeelings -> "Mixed Feelings"
            recommended -> "Recommended"
        }

    companion object {
        val allCases: Array<ReviewRecommendation>
            get() = entries.toTypedArray()

        fun fromInt(value: Int): ReviewRecommendation? = entries.find { it.rawValue == value }
    }
}

object ReviewRecommendationSerializer : IntValueEnumSerializer<ReviewRecommendation>(
    serialName = "ReviewRecommendation",
    entries = ReviewRecommendation.entries,
    valueSelector = { it.rawValue },
    fallback = ReviewRecommendation.mixedFeelings,
)
