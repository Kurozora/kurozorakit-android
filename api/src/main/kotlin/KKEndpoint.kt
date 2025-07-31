package kurozora.kit.api

sealed class KKEndpoint(val path: String) {
    sealed class Explore(path: String): KKEndpoint(path) {
        object Index : Explore("explore")
        class Details(id: String) : Explore("explore/$id")
    }

    // --- Search ---
    sealed class Search(path: String): KKEndpoint(path) {
        object Index       : Search("search/")
        object Suggestions : Search("search/suggestions")
    }

    // MARK: - Show
    sealed class Show(endpointValue: String) : KKEndpoint(endpointValue) {
        class Index(vararg showIds: List<String>) : Show(
            if (showIds.isEmpty()) "anime"
            else "anime/${showIds.joinToString(",")}"
        )
        class Details(showId: String) : Show("anime/${showId}")
        class Cast(showId: String) : Show("anime/${showId}/cast")
        class Characters(showId: String) : Show("anime/${showId}/characters")
        class People(showId: String) : Show("anime/${showId}/people")
        class Reviews(showId: String) : Show("anime/${showId}/reviews")
        class Seasons(showId: String) : Show("anime/${showId}/seasons")
        class Songs(showId: String) : Show("anime/${showId}/songs")
        class Studios(showId: String) : Show("anime/${showId}/studios")
        class MoreByStudio(showId: String) : Show("anime/${showId}/more-by-studio")
        class RelatedShows(showId: String) : Show("anime/${showId}/related-shows")
        class RelatedLiteratures(showId: String) : Show("anime/${showId}/related-literatures")
        class RelatedGames(showId: String) : Show("anime/${showId}/related-games")
        object Upcoming : Show("anime/upcoming")
        class Rate(showId: String) : Show("anime/${showId}/rate")
    }

    // --- Literature ---
    sealed class Literature(path: String): KKEndpoint(path) {
        object Index : Literature("manga")
        class Details(id: String) : Literature("manga/$id")
        class Cast(id: String)    : Literature("manga/$id/cast")
        class Characters(id: String) : Literature("manga/$id/characters")
        class People(id: String)  : Literature("manga/$id/people")
        class Reviews(id: String) : Literature("manga/$id/reviews")
        class Studios(id: String) : Literature("manga/$id/studios")
        class MoreByStudio(id: String) : Literature("manga/$id/more-by-studio")
        class RelatedShows(id: String) : Literature("manga/$id/related-shows")
        class RelatedLiteratures(id: String) : Literature("manga/$id/related-literatures")
        class RelatedGames(id: String) : Literature("manga/$id/related-games")
        object Upcoming           : Literature("manga/upcoming")
        class Rate(id: String)    : Literature("manga/$id/rate")
    }

    sealed class Game(path: String) : KKEndpoint(path) {
        object Index : Game("game")
        class Details(gameId: String) : Game("game/$gameId")
        class Cast(gameId: String) : Game("game/$gameId/cast")
        class People(gameId: String) : Game("game/$gameId/people")
        class Characters(gameId: String) : Game("game/$gameId/characters")
        class Reviews(gameId: String) : Game("game/$gameId/reviews")
        class Studios(gameId: String) : Game("game/$gameId/studios")
        class MoreByStudio(gameId: String) : Game("game/$gameId/more-by-studio")
        class RelatedAnimes(gameId: String) : Game("game/$gameId/related-shows")
        class RelatedMangas(gameId: String) : Game("game/$gameId/related-literatures")
        class RelatedGames(gameId: String) : Game("game/$gameId/related-games")
        object Upcoming : Game("game/upcoming")
        class Rate(gameId: String) : Game("game/$gameId/studios")
    }

    sealed class Character(path: String) : KKEndpoint(path) {
        object All : Character("characters")
        class Details(characterId: String) : Character("characters/$characterId")
        class People(characterId: String) : Character("characters/$characterId/people")
        class Animes(characterId: String) : Character("characters/$characterId/anime")
        class Mangas(characterId: String) : Character("characters/$characterId/literatures")
        class Games(characterId: String) : Character("characters/$characterId/games")
        class Reviews(characterId: String) : Character("characters/$characterId/reviews")
        class Rate(characterId: String) : Character("characters/$characterId/rate")
        class DeleteRate(characterId: String) : Character("characters/$characterId/rate")
    }

    sealed class Person(path: String) : KKEndpoint(path) {
        object All : Person("people")
        class Details(peopleId: String) : Person("people/$peopleId")
        class Anime(peopleId: String) : Person("people/$peopleId/anime")
        class Game(peopleId: String) : Person("people/$peopleId/games")
        class Manga(peopleId: String) : Person("people/$peopleId/literatures")
        class Characters(peopleId: String) : Person("people/$peopleId/characters")
        class Reviews(peopleId: String) : Person("people/$peopleId/reviews")
        class Rate(peopleId: String) : Person("people/$peopleId/rate")
    }

    // --- Studios ---
    sealed class Studios(path: String): KKEndpoint(path) {
        object Index : Studios("studios")
        class Details(id: String) : Studios("studios/$id")
        class Games(id: String)   : Studios("studios/$id/games")
        class Literatures(id: String) : Studios("studios/$id/literatures")
        class Shows(id: String)   : Studios("studios/$id/anime")
        class Reviews(id: String) : Studios("studios/$id/reviews")
        class Rate(id: String)    : Studios("studios/$id/rate")
    }

    // --- Episodes ---
    sealed class Episodes(path: String): KKEndpoint(path) {
        class Details(epId: String)    : Episodes("episodes/$epId")
        class Suggestions(epId: String): Episodes("episodes/$epId/suggestions")
        class Watched(epId: String)    : Episodes("episodes/$epId/watched")
        class Rate(epId: String)       : Episodes("episodes/$epId/rate")
        class Reviews(epId: String)    : Episodes("episodes/$epId/reviews")
    }

    // --- Songs ---
    sealed class Songs(path: String): KKEndpoint(path) {
        object Index : Songs("songs")
        class Details(id: String) : Songs("songs/$id")
        class Shows(id: String)   : Songs("songs/$id/anime")
        class Games(id: String)   : Songs("songs/$id/games")
        class Rate(id: String)    : Songs("songs/$id/rate")
        class Reviews(id: String) : Songs("songs/$id/reviews")
    }

    // --- Cast ---
    sealed class Cast(path: String): KKEndpoint(path) {
        class ShowCast(castId: String)       : Cast("show-cast/$castId")
        class LiteratureCast(castId: String): Cast("literature-cast/$castId")
        class GameCast(castId: String)      : Cast("game-cast/$castId")
    }

    // --- Genres ---
    sealed class Genres(path: String): KKEndpoint(path) {
        object Index : Genres("genres")
        class Details(id: String) : Genres("genres/$id")
    }

    // --- Reviews ---
    sealed class Reviews(path: String): KKEndpoint(path) {
        class Delete(reviewId: String) : Reviews("reviews/$reviewId/delete")
    }

    // --- Schedule ---
    sealed class Schedule(path: String): KKEndpoint(path) {
        object Index : Schedule("schedule")
    }

    // --- Feed ---
    sealed class Feed(path: String): KKEndpoint(path) {
        object Explore : Feed("feed/explore")
        object Home    : Feed("feed/home")
        object Post    : Feed("feed")
        sealed class Messages(path: String): KKEndpoint(path) {
            class Details(messageId: String) : Messages("feed/messages/$messageId")
            class Replies(messageId: String) : Messages("feed/messages/$messageId/replies")
            class Heart(messageId: String) : Messages("feed/messages/$messageId/heart")
            class Pin(messageId: String) : Messages("feed/messages/$messageId/pin")
            class Update(messageId: String) : Messages("feed/messages/$messageId/update")
            class Delete(messageId: String) : Messages("feed/messages/$messageId/delete")
        }
    }

    // --- Images ---
    sealed class Images(path: String): KKEndpoint(path) {
        object Random : Images("images/random")
    }

    // --- Misc ---
    sealed class Misc(path: String): KKEndpoint(path) {
        object Info     : Misc("info")
        object Settings : Misc("settings")
    }

    // --- Auth ---
    sealed class Auth(path: String): KKEndpoint(path) {
        object SignUp     : Auth("users")
        object SignIn     : Auth("users/signin")
        object SiwaSignIn : Auth("users/siwa/signin")
        object ResetPassword : Auth("users/reset-password")
        class Block(id: String)     : Auth("users/$id/block")
        class Blocking(id: String)  : Auth("users/$id/blocking")
        class FeedMessages(id: String) : Auth("users/$id/feed-messages")
        class Follow(id: String)    : Auth("users/$id/follow")
        class Followers(id: String) : Auth("users/$id/followers")
        class Following(id: String) : Auth("users/$id/following")
        class Favorites(id: String) : Auth("users/$id/favorites")
        class Library(id: String)   : Auth("users/$id/library")
        class Profile(id: String)   : Auth("users/$id/profile")
        class Reviews(id: String)   : Auth("users/$id/reviews")
        class Search(username: String) : Auth("users/search/$username")
        object Delete     : Auth("users/delete")
    }

    // --- Me ---
    sealed class Me(path: String) : KKEndpoint(path) {
        // Base endpoints
        object Profile : Me("me")
        object Update : Me("me")
        object Followers : Me("me/followers")
        object Following : Me("me/following")

        // Access Tokens
        sealed class AccessTokens(path: String) : Me(path) {
            object Index : AccessTokens("me/access-tokens")
            class Details(accessToken: String) : AccessTokens("me/access-tokens/$accessToken")
            class Update(accessToken: String) : AccessTokens("me/access-tokens/$accessToken/update")
            class Delete(accessToken: String) : AccessTokens("me/access-tokens/$accessToken/delete")
        }

        // Favorites
        sealed class Favorites(path: String) : Me(path) {
            object Index : Favorites("me/favorites")
            object Update : Favorites("me/favorites")
        }

        // Feed
        sealed class Feed(path: String) : Me(path) {
            object Messages : Feed("me/feed-messages")
        }

        // Library
        sealed class Library(path: String) : Me(path) {
            object Index : Library("me/library")
            object Update : Library("me/library/update")
            object Delete : Library("me/library/delete")
            object Clear : Library("me/library/clear")
            object MalImport : Library("me/library/mal-import")
            object Import : Library("me/library/import")
        }

        // Notifications
        sealed class Notifications(path: String) : Me(path) {
            object Index : Notifications("me/notifications")
            class Details(notificationID: String) : Notifications("me/notifications/$notificationID")
            class Delete(notificationID: String) : Notifications("me/notifications/$notificationID/delete")
            object Update : Notifications("me/notifications/update")
        }

        // Recap
        sealed class Recap(path: String) : Me(path) {
            object Index : Recap("me/recap")
            class Details(year: String, month: String) : Recap("me/recap/$year/$month")
        }

        // Reminders
        sealed class Reminders(path: String) : Me(path) {
            object Index : Reminders("me/reminders")
            object Update : Reminders("me/reminders")
            object Download : Reminders("me/reminders/download")
        }

        // Sessions
        sealed class Sessions(path: String) : Me(path) {
            object Index : Sessions("me/sessions")
            class Details(sessionId: String) : Sessions("me/sessions/${sessionId}")
            class Delete(sessionId: String) : Sessions("me/sessions/${sessionId}/delete")
        }
    }

    // --- Themes ---
    sealed class Themes(path: String): KKEndpoint(path) {
        object Index : Themes("themes")
        class Details(id: String) : Themes("themes/$id")
    }

    // --- ThemeStore ---
    sealed class ThemeStore(path: String): KKEndpoint(path) {
        object Index : ThemeStore("theme-store")
        class Details(id: String) : ThemeStore("theme-store/$id")
    }

    // --- Store ---
    sealed class Store(path: String): KKEndpoint(path) {
        object Verify : Store("store/verify")
    }

    // --- Legal ---
    sealed class Legal(path: String): KKEndpoint(path) {
        object PrivacyPolicy : Legal("legal/privacy-policy")
        object TermsOfUse    : Legal("legal/terms-of-use")
    }
}
