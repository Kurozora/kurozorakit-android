package kurozora.kit.data.models.misc

import kotlinx.serialization.Serializable

@Serializable
data class LibraryImport(
    var message: String?
) {
    enum class Service(val stringValue: String) {
        MAL("MyAnimeList"),
        KITSU("Kitsu")
    }

    enum class Behavior(val stringValue: String) {
        OVERWRITE("Overwrite"),
        MERGE("Merge")
    }
}
