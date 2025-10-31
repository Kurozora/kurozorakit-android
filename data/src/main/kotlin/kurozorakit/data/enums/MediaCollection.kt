package kurozorakit.data.enums

enum class MediaCollection {
    artwork,
    banner,
    logo,
    poster,
    profile,
    screenshot,
    symbol;

    val stringValue: String
        get() = when (this) {
            artwork -> "artwork"
            banner -> "banner"
            logo -> "logo"
            poster -> "poster"
            profile -> "profile"
            screenshot -> "screenshot"
            symbol -> "symbol"
        }
}
