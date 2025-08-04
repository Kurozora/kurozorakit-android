package kurozora.kit.data.models.misc

import kotlinx.serialization.Serializable

@Serializable
data class LibraryImport(
    var message: String?
) {
    enum class Service(val value: Int) {
        MAL(0),
        KITSU(1)
    }

    enum class Behavior(val value: Int) {
        OVERWRITE(0),
        MERGE(1)
    }
}
