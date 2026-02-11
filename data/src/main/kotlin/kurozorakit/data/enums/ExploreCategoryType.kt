package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Defines the content source and semantic meaning of an Explore section category.
 *
 * This enum is used by the [kurozorakit.data.repositories.explore.ExploreRepository]
 * to determine which data models to expect and how to fetch the items for a specific
 * discovery block.
 *
 * @property value The raw string slug used in API request paths.
 */
@Serializable
enum class ExploreCategoryType(val value: String) {
    // --- Shows (Anime) ---
    /** High-traffic anime series based on user engagement. */
    mostPopularShows("most-popular-shows"),
    /** Anime series scheduled for release in the near future. */
    upcomingShows("upcoming-shows"),
    /** Recently released or newly added anime series. */
    newShows("new-shows"),
    /** A generic collection of anime series. */
    shows("shows"),

    // --- Literatures (Manga/Novels) ---
    /** High-traffic manga or light novels. */
    mostPopularLiteratures("most-popular-literatures"),
    /** Manga or novels scheduled for release. */
    upcomingLiteratures("upcoming-literatures"),
    /** Recently released or newly added literature. */
    newLiteratures("new-literatures"),
    /** A generic collection of literature. */
    literatures("literatures"),

    // --- Games ---
    /** High-traffic video games. */
    mostPopularGames("most-popular-games"),
    /** Video games scheduled for release. */
    upcomingGames("upcoming-games"),
    /** Recently released or newly added games. */
    newGames("new-games"),
    /** A generic collection of games. */
    games("games"),

    // --- Activity & Media ---
    /** Episodes from series the user is currently watching that are next in line. */
    upNextEpisodes("up-next-episodes"),
    /** A generic collection of recently aired or featured episodes. */
    episodes("episodes"),
    /** Featured theme songs, OPs, or EDs. */
    songs("songs"),
    /** Popular or trending fictional characters. */
    characters("characters"),
    /** Featured staff members, voice actors, or creators. */
    people("people"),

    // --- Metadata & Specials ---
    /** Browsable list of media genres. */
    genres("genres"),
    /** Browsable list of visual or narrative themes. */
    themes("themes"),
    /** A personalized or global summary of activity (e.g., Year in Review). */
    recap("recap")
}