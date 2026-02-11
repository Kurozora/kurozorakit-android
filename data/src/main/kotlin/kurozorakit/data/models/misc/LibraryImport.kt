package kurozorakit.data.models.misc

import kotlinx.serialization.Serializable

/**
 * Represents the configuration and result status of a library import process.
 *
 * This model is used when a user attempts to migrate their media lists from external
 * tracking services to Kurozora. It defines the origin service, how to handle
 * existing data conflicts, and provides feedback on the operation's outcome.
 *
 * @property message An optional status message returned by the server indicating
 * the result of the import (e.g., "Import successful", "In queue", or error details).
 */
@Serializable
data class LibraryImport(
    var message: String?
) {
    /**
     * Defines the external third-party services supported for library migration.
     *
     * @property value The integer representation of the service, often used for API mapping.
     */
    enum class Service(val value: Int) {
        /** Represents MyAnimeList.net integration. */
        MAL(0),
        /** Represents Kitsu.io integration. */
        KITSU(1)
    }

    /**
     * Defines the strategy for handling data conflicts during the import process.
     *
     * @property value The integer representation of the behavior logic.
     */
    enum class Behavior(val value: Int) {
        /** * Existing Kurozora library entries will be replaced by the data
         * coming from the external service.
         */
        OVERWRITE(0),
        /** * External data will be combined with existing Kurozora entries.
         * Only missing items will be added, preserving existing progress where applicable.
         */
        MERGE(1)
    }
}