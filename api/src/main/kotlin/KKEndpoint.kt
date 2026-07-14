package kurozorakit.api

/**
 * A sealed class hierarchy representing all available API endpoints in the Kurozora ecosystem.
 *
 * This class provides a type-safe way to construct API paths. Each subclass corresponds to a
 * specific resource or action within the API. The [path] property contains the relative URL
 * string required to make the HTTP request.
 *
 * @property path The relative URL path for the endpoint (e.g., "anime/123/cast").
 */
sealed class KKEndpoint(val path: String) {

    /**
     * Represents a raw, absolute URL.
     * Useful when the API returns a full pagination URL or an external link that needs to be requested directly.
     *
     * @property url The full URL string.
     */
    data class Url(val url: String): KKEndpoint(url)

    /**
     * Endpoints related to content discovery and exploration.
     */
    sealed class Explore(path: String): KKEndpoint(path) {
        /** The main exploration landing page/feed. */
        object Index : Explore("explore")
        /** Details for a specific exploration category or item. */
        class Details(id: String) : Explore("explore/$id")
    }

    // --- Search ---
    /**
     * Endpoints for searching content across the platform.
     */
    sealed class Search(path: String): KKEndpoint(path) {
        /** The main search endpoint for executing queries. */
        object Index       : Search("search/")
        /** Provides auto-complete suggestions based on partial input. */
        object Suggestions : Search("search/suggestions")
    }

    // MARK: - Show
    /**
     * Endpoints related to Anime (Shows).
     * Includes details, cast, staff, seasons, and related media.
     */
    sealed class Show(endpointValue: String) : KKEndpoint(endpointValue) {
        /**
         * Fetches a list of shows, or specific shows if IDs are provided.
         * @param showIds Optional list of Anime IDs. If empty, returns the index of shows.
         */
        class Index(vararg showIds: String) : Show(
            if (showIds.isEmpty()) "anime"
            else "anime/${showIds.joinToString(",")}"
        )
        /** Detailed information for a specific show. */
        class Details(showId: String) : Show("anime/${showId}")
        /** The voice acting cast for a specific show. */
        class Cast(showId: String) : Show("anime/${showId}/cast")
        /** The production staff for a specific show. */
        class Staff(showId: String) : Show("anime/${showId}/staff")
        /** The characters appearing in a specific show. */
        class Characters(showId: String) : Show("anime/${showId}/characters")
        /** (Alias for Staff) The people involved in the show. */
        class People(showId: String) : Show("anime/${showId}/staff")
        /** User reviews for a specific show. */
        class Reviews(showId: String) : Show("anime/${showId}/reviews")

        /**
         * Endpoints for handling seasons within a specific TV Show context.
         * Note: This differs from the top-level [kurozorakit.api.KKEndpoint.Seasons] which refers to calendar seasons.
         */
        class Seasons(showId: String) : Show("anime/${showId}/seasons") {
            /** Lists seasons for the show, or details for specific season IDs. */
            class Index(vararg seasonIds: String) : KKEndpoint(
                if (seasonIds.isEmpty()) "seasons"
                else "seasons/${seasonIds.joinToString(",")}"
            )
            /** Details for a specific season entity. */
            class Details(seasonId: String) : KKEndpoint("seasons/$seasonId")
            /** List of episodes belonging to a specific season. */
            class Episodes(seasonId: String) : KKEndpoint("seasons/$seasonId/episodes")
            /** Watched status for a specific season. */
            class Watched(seasonId: String) : KKEndpoint("seasons/$seasonId/watched")
        }

        /** Opening and Ending themes (songs) for a show. */
        class Songs(showId: String) : Show("anime/${showId}/songs")
        /** Animation studios that produced the show. */
        class Studios(showId: String) : Show("anime/${showId}/studios")
        /** Other works produced by the same studio. */
        class MoreByStudio(showId: String) : Show("anime/${showId}/more-by-studio")
        /** Anime recommendations or related series (sequels, prequels). */
        class RelatedShows(showId: String) : Show("anime/${showId}/related-shows")
        /** Manga or Light Novels related to this anime (adaptations). */
        class RelatedLiteratures(showId: String) : Show("anime/${showId}/related-literatures")
        /** Games related to this anime. */
        class RelatedGames(showId: String) : Show("anime/${showId}/related-games")
        /** List of upcoming anime. */
        object Upcoming : Show("anime/upcoming")
        /** Endpoint to rate a specific show. */
        class Rate(showId: String) : Show("anime/${showId}/rate")
        /** Parental guide stats and entries for a show. */
        class ParentalGuide(showId: String) : Show("anime/${showId}/parentalguide")
    }

    // --- Literature ---
    /**
     * Endpoints related to Literature (Manga, Light Novels, Webtoons).
     */
    sealed class Literature(path: String): KKEndpoint(path) {
        /**
         * Fetches a list of manga, or specific manga if IDs are provided.
         * @param litIds Optional list of Literature IDs.
         */
        class Index(vararg litIds: String) : Literature(
            if (litIds.isEmpty()) "manga"
            else "manga/${litIds.joinToString(",")}"
        )
        /** Detailed information for a specific manga/novel. */
        class Details(id: String) : Literature("manga/$id")
        /** Characters appearing in the literature. */
        class Cast(id: String)    : Literature("manga/$id/cast")
        /** Authors and artists involved. */
        class Staff(id: String)   : Literature("manga/$id/staff")
        /** Characters appearing in the literature. */
        class Characters(id: String) : Literature("manga/$id/characters")
        /** (Alias for Staff) People involved. */
        class People(id: String)  : Literature("manga/$id/staff")
        /** User reviews. */
        class Reviews(id: String) : Literature("manga/$id/reviews")
        /** Publishers or magazines. */
        class Studios(id: String) : Literature("manga/$id/studios")
        /** More works by the same authors/publishers. */
        class MoreByStudio(id: String) : Literature("manga/$id/more-by-studio")
        /** Anime adaptations of this work. */
        class RelatedShows(id: String) : Literature("manga/$id/related-shows")
        /** Related manga (spin-offs, sequels). */
        class RelatedLiteratures(id: String) : Literature("manga/$id/related-literatures")
        /** Related games. */
        class RelatedGames(id: String) : Literature("manga/$id/related-games")
        /** Upcoming literature releases. */
        object Upcoming           : Literature("manga/upcoming")
        /** Endpoint to rate a specific literature item. */
        class Rate(id: String)    : Literature("manga/$id/rate")
        /** Parental guide stats and entries for a literature item. */
        class ParentalGuide(id: String) : Literature("manga/$id/parentalguide")
    }

    /**
     * Endpoints related to Games.
     */
    sealed class Game(path: String) : KKEndpoint(path) {
        /** Fetches a list of games or specific games by ID. */
        class Index(vararg gameIds: String) : Show(
            if (gameIds.isEmpty()) "game"
            else "game/${gameIds.joinToString(",")}"
        )
        /** Detailed information for a specific game. */
        class Details(gameId: String) : Game("games/$gameId")
        /** Voice cast for the game. */
        class Cast(gameId: String) : Game("games/$gameId/cast")
        /** Developers and staff. */
        class Staff(gameId: String) : Game("game/$gameId/staff")
        /** (Alias for Staff) People involved. */
        class People(gameId: String) : Game("games/$gameId/staff")
        /** Characters appearing in the game. */
        class Characters(gameId: String) : Game("games/$gameId/characters")
        /** User reviews. */
        class Reviews(gameId: String) : Game("games/$gameId/reviews")
        /** Development studios. */
        class Studios(gameId: String) : Game("games/$gameId/studios")
        /** More games by the same studio. */
        class MoreByStudio(gameId: String) : Game("games/$gameId/more-by-studio")
        /** Anime adaptations of the game. */
        class RelatedShows(gameId: String) : Game("games/$gameId/related-shows")
        /** Manga/Novel adaptations. */
        class RelatedLiteratures(gameId: String) : Game("games/$gameId/related-literatures")
        /** Sequels or prequels. */
        class RelatedGames(gameId: String) : Game("games/$gameId/related-games")
        /** Upcoming game releases. */
        object Upcoming : Game("games/upcoming")
        /** Endpoint to rate a specific game. */
        class Rate(gameId: String) : Game("games/$gameId/rate")
        /** Parental guide stats and entries for a game. */
        class ParentalGuide(gameId: String) : Game("games/$gameId/parentalguide")
    }

    /**
     * Endpoints related to fictional Characters.
     */
    sealed class Character(path: String) : KKEndpoint(path) {
        /** Fetches a list of characters or specific characters by ID. */
        class Index(vararg characterIds: String) : Show(
            if (characterIds.isEmpty()) "characters"
            else "characters/${characterIds.joinToString(",")}"
        )
        /** Detailed bio and info for a character. */
        class Details(characterId: String) : Character("characters/$characterId")
        /** Voice actors (Seiyuu) for this character. */
        class People(characterId: String) : Character("characters/$characterId/people")
        /** Anime this character appears in. */
        class Shows(characterId: String) : Character("characters/$characterId/anime")
        /** Manga/Novels this character appears in. */
        class Literatures(characterId: String) : Character("characters/$characterId/literatures")
        /** Games this character appears in. */
        class Games(characterId: String) : Character("characters/$characterId/games")
        /** User reviews/comments about the character. */
        class Reviews(characterId: String) : Character("characters/$characterId/reviews")
        /** Endpoint to rate (like/love) a character. */
        class Rate(characterId: String) : Character("characters/$characterId/rate")
        /** Endpoint to remove a rating. */
        class DeleteRate(characterId: String) : Character("characters/$characterId/rate")
    }

    /**
     * Endpoints related to real People (Voice Actors, Staff, Authors).
     */
    sealed class Person(path: String) : KKEndpoint(path) {
        /** Fetches a list of people or specific people by ID. */
        class Index(vararg peopleIds: String) : Person(
            if (peopleIds.isEmpty()) "people"
            else "people/${peopleIds.joinToString(",")}"
        )
        /** Detailed bio for a person. */
        class Details(peopleId: String) : Person("people/$peopleId")
        /** Anime this person worked on. */
        class Show(peopleId: String) : Person("people/$peopleId/anime")
        /** Games this person worked on. */
        class Game(peopleId: String) : Person("people/$peopleId/games")
        /** Literature this person wrote or illustrated. */
        class Literature(peopleId: String) : Person("people/$peopleId/literatures")
        /** Characters voiced by this person. */
        class Characters(peopleId: String) : Person("people/$peopleId/characters")
        /** User reviews regarding this person. */
        class Reviews(peopleId: String) : Person("people/$peopleId/reviews")
        /** Endpoint to rate a person. */
        class Rate(peopleId: String) : Person("people/$peopleId/rate")
    }

    // --- Studios ---
    /**
     * Endpoints related to production Studios and Publishers.
     */
    sealed class Studios(path: String): KKEndpoint(path) {
        /** Fetches a list of studios or specific studios by ID. */
        class Index(vararg studioIds: String) : Studios(
            if (studioIds.isEmpty()) "studios"
            else "studios/${studioIds.joinToString(",")}"
        )
        /** Detailed info for a studio. */
        class Details(id: String) : Studios("studios/$id")
        /** Games produced by this studio. */
        class Games(id: String)   : Studios("studios/$id/games")
        /** Literature published by this company. */
        class Literatures(id: String) : Studios("studios/$id/literatures")
        /** Anime produced by this studio. */
        class Shows(id: String)   : Studios("studios/$id/anime")
        /** Reviews for the studio. */
        class Reviews(id: String) : Studios("studios/$id/reviews")
        /** Endpoint to rate the studio. */
        class Rate(id: String)    : Studios("studios/$id/rate")
    }

    // --- Episodes ---
    /**
     * Endpoints related to specific Episodes of an Anime.
     */
    sealed class Episodes(path: String): KKEndpoint(path) {
        /** Fetches a list of episodes or specific episodes by ID. */
        class Index(vararg episodeIds: String) : Episodes(
            if (episodeIds.isEmpty()) "episodes"
            else "episodes/${episodeIds.joinToString(",")}"
        )
        /** Detailed info for an episode. */
        class Details(epId: String)    : Episodes("episodes/$epId")
        /** Suggestions/Recommendations based on this episode. */
        class Suggestions(epId: String): Episodes("episodes/$epId/suggestions")
        /** Mark episode as watched or get status. */
        class Watched(epId: String)    : Episodes("episodes/$epId/watched")
        /** Rate the episode. */
        class Rate(epId: String)       : Episodes("episodes/$epId/rate")
        /** Reviews/comments for the episode. */
        class Reviews(epId: String)    : Episodes("episodes/$epId/reviews")
    }

    // --- Songs ---
    /**
     * Endpoints related to Songs (OSTs, Openings, Endings).
     */
    sealed class Songs(path: String): KKEndpoint(path) {
        /** Fetches a list of songs or specific songs by ID. */
        class Index(vararg songIds: String) : Songs(
            if (songIds.isEmpty()) "songs"
            else "songs/${songIds.joinToString(",")}"
        )
        /** Detailed info for a song. */
        class Details(id: String) : Songs("songs/$id")
        /** Anime where this song appears. */
        class Shows(id: String)   : Songs("songs/$id/anime")
        /** Games where this song appears. */
        class Games(id: String)   : Songs("songs/$id/games")
        /** Rate the song. */
        class Rate(id: String)    : Songs("songs/$id/rate")
        /** Reviews for the song. */
        class Reviews(id: String) : Songs("songs/$id/reviews")
    }

    // --- Cast ---
    /**
     * Endpoints related to Cast listings (linking Characters to People).
     */
    sealed class Cast(path: String): KKEndpoint(path) {
        /** Details of a specific cast entry (pairing of character and actor). */
        class CastDetail(id: String)       : Cast("cast/$id")
        /** Full cast list for a show. */
        class ShowCast(showId: String)       : Cast("anime/$showId/cast")
        /** Full cast list for literature (e.g. Drama CD adaptations). */
        class LiteratureCast(literatureId: String): Cast("manga/$literatureId/cast")
        /** Full cast list for a game. */
        class GameCast(gameId: String)      : Cast("games/$gameId/cast")
    }

    // --- Genres ---
    /**
     * Endpoints related to content Genres and Tags.
     */
    sealed class Genres(path: String): KKEndpoint(path) {
        /** List of all available genres. */
        object Index : Genres("genres")
        /** Details for a specific genre (and potentially items within it). */
        class Details(id: String) : Genres("genres/$id")
    }

    // --- Reviews ---
    /**
     * Endpoints for managing user Reviews.
     */
    sealed class Reviews(path: String): KKEndpoint(path) {
        /** Deletes a specific review. */
        class Delete(reviewId: String) : Reviews("reviews/$reviewId/delete")
    }

    // --- Parental Guide ---
    /**
     * Endpoints for managing Parental Guide entries.
     */
    sealed class ParentalGuideEntry(path: String): KKEndpoint(path) {
        /** Fetches a specific entry. */
        class Details(entryId: String) : ParentalGuideEntry("parentalguide/entries/$entryId")
        /** Deletes a specific entry. */
        class Delete(entryId: String) : ParentalGuideEntry("parentalguide/entries/$entryId/delete")
        /** Updates a specific entry in place. */
        class Update(entryId: String) : ParentalGuideEntry("parentalguide/entries/$entryId/update")
        /** Votes on a parental guide entry. */
        class Vote(entryId: String) : ParentalGuideEntry("parentalguide/entries/$entryId/vote")
        /** Reports a parental guide entry. */
        class Report(entryId: String) : ParentalGuideEntry("parentalguide/entries/$entryId/report")
    }

    // --- Schedule ---
    /**
     * Endpoints for release Schedules.
     */
    sealed class Schedule(path: String): KKEndpoint(path) {
        /** Get the current anime/manga release schedule. */
        object Index : Schedule("schedule")
    }

    // --- Feed ---
    /**
     * Endpoints related to the social Feed (Posts, Activity).
     */
    sealed class Feed(path: String): KKEndpoint(path) {
        /** Global explore feed. */
        object Explore : Feed("feed/explore")
        /** User's personal home feed (following). */
        object Home    : Feed("feed/home")
        /** Posting to the feed. */
        object Post    : Feed("feed")

        /**
         * Nested endpoints for specific Feed Messages (Posts).
         */
        sealed class Messages(path: String): KKEndpoint(path) {
            /** Details of a specific message. */
            class Details(messageId: String) : Messages("feed/messages/$messageId")
            /** Replies/Comments on a message. */
            class Replies(messageId: String) : Messages("feed/messages/$messageId/replies")
            /** Quote re-shares of a message. */
            class Quotes(messageId: String) : Messages("feed/messages/$messageId/quotes")
            /** Simple re-shares of a message. */
            class ReShares(messageId: String) : Messages("feed/messages/$messageId/re-shares")
            /** Like/Heart a message. */
            class Heart(messageId: String) : Messages("feed/messages/$messageId/heart")
            /** Pin a message (usually for profile owners). */
            class Pin(messageId: String) : Messages("feed/messages/$messageId/pin")
            /** Edit a message. */
            class Update(messageId: String) : Messages("feed/messages/$messageId/update")
            /** Delete a message. */
            class Delete(messageId: String) : Messages("feed/messages/$messageId/delete")
        }
    }

    // --- Images ---
    /**
     * Endpoints for fetching content by Calendar Season (Year/Season).
     * Note: This differs from [KKEndpoint.Show.Seasons] which are TV seasons of a show.
     */
    sealed class Seasons(path: String): KKEndpoint(path) {
        /** Anime airing in a specific year and season (e.g., 2024/winter). */
        class Anime(year: String, season: String) : Seasons("anime/seasons/$year/$season")
        /** Manga published in a specific season. */
        class Manga(year: String, season: String) : Seasons("manga/seasons/$year/$season")
        /** Games released in a specific season. */
        class Game(year: String, season: String) : Seasons("games/seasons/$year/$season")
    }

    // --- Images ---
    /**
     * Endpoints for generic Image handling.
     */
    sealed class Images(path: String): KKEndpoint(path) {
        /** Fetch random images from the platform. */
        object Random : Images("images/random")
    }

    // --- Misc ---
    /**
     * Miscellaneous endpoints (System info, app settings).
     */
    sealed class Misc(path: String): KKEndpoint(path) {
        /** General application or API information. */
        object Info     : Misc("info")
        /** Application configuration/settings. */
        object Settings : Misc("settings")
    }

    // --- Auth ---
    /**
     * Public endpoints related to User Accounts and Public Profiles.
     * (See [Me] for authenticated user private actions).
     */
    sealed class Auth(path: String): KKEndpoint(path) {
        /** Fetch list of users or specific user details. */
        class Index(vararg userIds: String) : Auth(
            if (userIds.isEmpty()) "users"
            else "users/${userIds.joinToString(",")}"
        )
        /** Create a new user account. */
        object SignUp     : Auth("users")
        /** Sign in with credentials. */
        object SignIn     : Auth("users/signin")
        /** Sign in with Apple (SIWA). */
        object SiwaSignIn : Auth("users/siwa/signin")
        /** Resolve a two-factor authentication challenge. */
        object TwoFactorChallenge : Auth("users/two-factor-challenge")
        /** Initiate password reset flow. */
        object ResetPassword : Auth("users/reset-password")
        /** Block a user. */
        class Block(id: String)     : Auth("users/$id/block")
        /** List of users blocked by this user (or checking block status). */
        class Blocking(id: String)  : Auth("users/$id/blocked")
        /** Feed posts created by a specific user. */
        class FeedMessages(id: String) : Auth("users/$id/feed-messages")
        /** Follow a user. */
        class Follow(id: String)    : Auth("users/$id/follow")
        /** List of followers for a user. */
        class Followers(id: String) : Auth("users/$id/followers")
        /** List of users this user is following. */
        class Following(id: String) : Auth("users/$id/following")
        /** Favorites list of a user. */
        class Favorites(id: String) : Auth("users/$id/favorites")
        /** Library (Watchlist/Readlist) of a user. */
        class Library(id: String)   : Auth("users/$id/library")
        /** Public profile details. */
        class Profile(id: String)   : Auth("users/$id/profile")
        /** Reviews written by a user. */
        class Reviews(id: String)   : Auth("users/$id/reviews")
        /** Search for users by username. */
        class Search(username: String) : Auth("users/search/$username")
        /** Delete own account. */
        object Delete     : Auth("users/delete")
    }

    // --- Me ---
    /**
     * Endpoints for the currently authenticated User ("Me").
     * These require a valid auth token.
     */
    sealed class Me(path: String) : KKEndpoint(path) {
        // Base endpoints
        /** Get own profile. */
        object Profile : Me("me")
        /** Update own profile. */
        object Update : Me("me")
        /** Get own followers. */
        object Followers : Me("me/followers")
        /** Get who I am following. */
        object Following : Me("me/following")
        /** Get my blocked users list. */
        object Blocked : Me("me/blocked")

        // Access Tokens
        /** Manage API Access Tokens / Devices. */
        sealed class AccessTokens(path: String) : Me(path) {
            /** List active access tokens. */
            object Index : AccessTokens("me/access-tokens")
            /** Get details of a specific token. */
            class Details(accessToken: String) : AccessTokens("me/access-tokens/$accessToken")
            /** Update token metadata. */
            class Update(accessToken: String) : AccessTokens("me/access-tokens/$accessToken/update")
            /** Revoke a token. */
            class Delete(accessToken: String) : AccessTokens("me/access-tokens/$accessToken/delete")
        }

        // Favorites
        /** Manage own favorites. */
        sealed class Favorites(path: String) : Me(path) {
            /** List favorites. */
            object Index : Favorites("me/favorites")
            /** Add/Remove favorites. */
            object Update : Favorites("me/favorites")
        }

        // Feed
        /** Manage own feed. */
        sealed class Feed(path: String) : Me(path) {
            /** Get own feed messages. */
            object Messages : Feed("me/feed-messages")
        }

        // Library
        /** Manage own library (Watchlist/Readlist). */
        sealed class Library(path: String) : Me(path) {
            /** Get library entries. */
            object Index : Library("me/library")
            /** Update a library entry. */
            object Update : Library("me/library/update")
            /** Remove an entry from library. */
            object Delete : Library("me/library/delete")
            /** Clear entire library. */
            object Clear : Library("me/library/clear")
            /** Import library from MyAnimeList. */
            object MalImport : Library("me/library/mal-import")
            /** Generic import. */
            object Import : Library("me/library/import")
        }

        // Notifications
        /** Manage notifications. */
        sealed class Notifications(path: String) : Me(path) {
            /** List notifications. */
            object Index : Notifications("me/notifications")
            /** Get specific notification details. */
            class Details(notificationID: String) : Notifications("me/notifications/$notificationID")
            /** Delete one, multiple, or all notifications. */
            object Delete : Notifications("me/notifications/delete")
            /** Mark notifications as read/update. */
            object Update : Notifications("me/notifications/update")
        }

        // Recap
        /** User statistics and yearly summaries. */
        sealed class Recap(path: String) : Me(path) {
            /** Get general recap index. */
            object Index : Recap("me/recap")
            /** Get recap for a specific month and year. */
            class Details(year: String, month: String) : Recap("me/recap/$year/$month")
        }

        // Reminders
        /** Manage release reminders. */
        sealed class Reminders(path: String) : Me(path) {
            /** List active reminders. */
            object Index : Reminders("me/reminders")
            /** Update reminders. */
            object Update : Reminders("me/reminders")
            /** Download reminders (e.g. iCal). */
            object Download : Reminders("me/reminders/download")
        }

        // Sessions
        /** Manage active login sessions. */
        sealed class Sessions(path: String) : Me(path) {
            /** List sessions. */
            object Index : Sessions("me/sessions")
            /** Get session details. */
            class Details(sessionId: String) : Sessions("me/sessions/${sessionId}")
            /** Revoke/Delete a session. */
            class Delete(sessionId: String) : Sessions("me/sessions/${sessionId}/delete")
        }
    }

    // --- Themes ---
    /**
     * Endpoints related to UI Themes.
     */
    sealed class Themes(path: String): KKEndpoint(path) {
        /** List available themes. */
        object Index : Themes("themes")
        /** Get theme details. */
        class Details(id: String) : Themes("themes/$id")
    }

    // --- ThemeStore ---
    /**
     * Endpoints related to the Theme Store.
     */
    sealed class ThemeStore(path: String): KKEndpoint(path) {
        /** Browse theme store. */
        object Index : ThemeStore("theme-store")
        /** Get store item details. */
        class Details(id: String) : ThemeStore("theme-store/$id")
    }

    // --- Store ---
    /**
     * Endpoints related to In-App Purchases and verification.
     */
    sealed class Store(path: String): KKEndpoint(path) {
        /** Verify a purchase receipt. */
        object Verify : Store("store/verify")
    }

    // --- Legal ---
    /**
     * Endpoints for Legal documents.
     */
    sealed class Legal(path: String): KKEndpoint(path) {
        /** Get Privacy Policy text. */
        object PrivacyPolicy : Legal("legal/privacy-policy")
        /** Get Terms of Use text. */
        object TermsOfUse    : Legal("legal/terms-of-use")
    }
}