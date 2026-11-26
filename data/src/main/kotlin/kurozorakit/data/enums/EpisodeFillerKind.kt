package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Represents the canon standing of an episode.
 *
 * Replaces the deprecated boolean `isFiller` flag with a more granular classification
 * distinguishing manga canon, anime canon, mixed canon/filler, and pure filler episodes.
 *
 * @property rawValue The integer identifier returned by the Kurozora API.
 */
@Serializable(with = EpisodeFillerKindSerializer::class)
enum class EpisodeFillerKind(val rawValue: Int) {
    /** Adapted directly from the original source manga. */
    mangaCanon(0),

    /** Canon to the anime adaptation, though original to it. */
    animeCanon(1),

    /** Contains both canon and filler material. */
    mixedCanonFiller(2),

    /** Non-canon content that can be skipped without losing the main story. */
    filler(3);

    /** Human-readable display label for the filler classification. */
    val displayName: String
        get() = when (this) {
            mangaCanon -> "Manga Canon"
            animeCanon -> "Anime Canon"
            mixedCanonFiller -> "Mixed Canon/Filler"
            filler -> "Filler"
        }

    /** Whether this classification counts as filler under legacy boolean semantics. */
    val isFiller: Boolean
        get() = this == filler || this == mixedCanonFiller

    companion object {
        val allCases: Array<EpisodeFillerKind>
            get() = entries.toTypedArray()

        fun fromInt(value: Int): EpisodeFillerKind? = entries.find { it.rawValue == value }
    }
}

object EpisodeFillerKindSerializer : IntValueEnumSerializer<EpisodeFillerKind>(
    serialName = "EpisodeFillerKind",
    entries = EpisodeFillerKind.entries,
    valueSelector = { it.rawValue },
    fallback = EpisodeFillerKind.mangaCanon,
)
