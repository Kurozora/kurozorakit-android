package kurozorakit.data.enums

/**
 * Defines the specific entity types available for search within the Kurozora ecosystem.
 *
 * This enum is used to tell the search engine which specific database table or index
 * to query. Each type corresponds to a distinct data model and set of search filters.
 */
enum class KKSearchType {
    /** Search for fictional characters from anime, manga, or games. */
    characters,

    /** Search for specific episodes across all available shows. */
    episodes,

    /** Search for video games, including base games, DLCs, and mods. */
    games,

    /** Search for written media such as Manga, Light Novels, and One-shots. */
    literatures,

    /** Search for real-world individuals, including voice actors (Seiyuu), authors, and staff. */
    people,

    /** Search for animated series, movies, and OVAs. */
    shows,

    /** Search for music tracks, including openings (OPs), endings (EDs), and soundtracks. */
    songs,

    /** Search for production companies and animation studios. */
    studios,

    /** Search for other members of the Kurozora community. */
    users
}