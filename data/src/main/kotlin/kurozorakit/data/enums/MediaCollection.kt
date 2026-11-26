package kurozorakit.data.enums

/**
 * Categorizes the various types of visual assets associated with a resource.
 *
 * This enum is used to distinguish between different image formats and purposes,
 * allowing the client to request specific assets for different parts of the interface.
 */
enum class MediaCollection {
    /** Default fallback media collection. */
    default,

    /** High-quality promotional art, usually without text. */
    artwork,

    /** A wide, horizontal image used for header backgrounds and "hero" sections. */
    banner,

    /** The official transparent logo of the media or studio. */
    logo,

    /** The standard vertical key visual used for grid listings and catalogs. */
    poster,

    /** A portrait-oriented image used for users, characters, or staff members. */
    profile,

    /** A direct frame capture from a video or gameplay session. */
    screenshot,

    /** A small, simplified graphic or icon representing the resource. */
    symbol;

    /**
     * The raw string identifier used for API endpoints and asset path construction.
     */
    val stringValue: String
        get() = when (this) {
            default -> "default"
            artwork -> "artwork"
            banner -> "banner"
            logo -> "logo"
            poster -> "poster"
            profile -> "profile"
            screenshot -> "screenshot"
            symbol -> "symbol"
        }
}