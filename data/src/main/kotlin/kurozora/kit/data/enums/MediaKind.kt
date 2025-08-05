package kurozora.kit.data.enums

enum class MediaKind {
    episodes,
    games,
    literatures,
    shows;

    val stringValue: String
        get() = when (this) {
            episodes -> "episodes"
            games -> "games"
            literatures -> "literatures"
            shows -> "shows"
        }
}