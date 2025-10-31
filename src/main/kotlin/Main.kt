package kurozorakit

import kurozorakit.core.KurozoraApi
import kurozorakit.core.KurozoraKit
import kurozorakit.data.models.search.filters.ShowFilter
import kurozorakit.data.models.user.User

suspend fun main() {
    val kurozoraKit = KurozoraKit.Builder()
        .apiEndpoint(KurozoraApi.V1.baseUrl)
        .apiKey("your-api-key")
        .build()

//    kurozoraKit.auth().signIn("test@example.com", $$"test").onSuccess { res ->
//        println(res.data)
//    }

    println(User.current?.attributes?.username)
//-------------------------------------USERS-------------------------------------------
//    kurozoraKit.user().getMyProfile().onSuccess { res ->
//        println(res.data.first())
//    }
//    kurozoraKit.user().updateMyProfile(UserUpdate(
//        biography = "Hello World"
//    )).onSuccess { response ->
//        println(response)
//    }
//    kurozoraKit.user().getMyFollowers().onSuccess { response ->
//        println(response)
//    }
//    kurozoraKit.user().getMyFollowing().onSuccess { response ->
//        println(response)
//    }
//    kurozoraKit.user().getAccessTokens().onSuccess { response ->
//        println(response)
//    }
//    kurozoraKit.user().getAccessToken("22624").onSuccess {
//        println(it)
//    }
//    kurozoraKit.user().getAccessTokens().onSuccess { response ->
//        val tokens = response.data.take(20)
//        tokens.forEach { token ->
//            val result = kurozoraKit.user().deleteAccessToken(token.id)
//            result.onSuccess {
//                println("Token silindi: ${token.id}")
//            }.onError {
//                println("Silme başarısız: ${token.id} - ${it}")
//            }
//        }
//    }
//    kurozoraKit.user().getMyFavorites(KKLibrary.Kind.GAMES).onSuccess {
//        println(it)
//    }
    //  ÇALIŞMADI
//    kurozoraKit.user().updateMyFavorites(KKLibrary.Kind.SHOWS, "3").onSuccess {
//        println(it)
//    }
//    kurozoraKit.user().getMyFeedMessages().onSuccess {
//        println(it)
//    }
//    kurozoraKit.user().getMyLibrary(
//        libraryKind = KKLibrary.Kind.GAMES,
//        libraryStatus = KKLibrary.Status.INTERESTED,
//        sortType = KKLibrary.SortType.ALPHABETICALLY,
//        sortOption = KKLibrary.Option.ASCENDING
//    ).onSuccess {
//        println(it)
//    }
//    kurozoraKit.user().addToLibrary(KKLibrary.Kind.SHOWS, KKLibrary.Status.INPROGRESS, "213").onSuccess {
//        println(it)
//    }
//    kurozoraKit.user().addToLibrary(KKLibrary.Kind.SHOWS, KKLibrary.Status.INPROGRESS, "213").onSuccess {
//        println(it)
//    }
//    val file = File("""C:\Users\selman\Downloads\malanimelist.xml""")
//    kurozoraKit.user().importToLibrary(KKLibrary.Kind.SHOWS,LibraryImport.Service.MAL,LibraryImport.Behavior.MERGE,file.toURI().toURL()).onSuccess {
//        println(it)
//    }
//    kurozoraKit.user().getMyNotifications().onSuccess {
//        println(it)
//    }
//    kurozoraKit.user().updateMyNotification("301d08b1-c825-4604-85bc-4ed80fda26b0", ReadStatus.read).onSuccess {
//        println(it)
//    }
//    kurozoraKit.user().getMyNotification("301d08b1-c825-4604-85bc-4ed80fda26b0").onSuccess {
//        println(it)
//    }
//    kurozoraKit.user().getMyNotifications().onSuccess { response ->
//        val notifications = response.data.take(40)
//        notifications.forEach { n ->
//            val result = kurozoraKit.user().deleteMyNotification(n.id)
//            result.onSuccess {
//                println("Notification silindi: ${n.id}")
//            }.onError {
//                println("Silme başarısız: ${n.id} - ${it}")
//            }
//        }
//    }
//    kurozoraKit.user().getMyRecaps().onSuccess {
//        println(it)
//    }
//    kurozoraKit.user().getMyRecapDetails("2025", "8").onSuccess {
//        println(it)
//    }
//    kurozoraKit.user().getMySessions().onSuccess {
//        println(it)
//    }
//    kurozoraKit.user().getMySession("session_id").onSuccess {
//        println(it)
//    }
//-------------------------------------SHOWS-------------------------------------------
//    filter ile çalışmıyor
//    kurozoraKit.show().getShows(filter = ShowFilter(isNSFW = true)).onSuccess {
//        println(it.data)
//    }
    // not implemented
//    kurozoraKit.show().getShows(listOf("2", "4", "22"),listOf("genres", "episodes")).onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().getShow("22598", listOf("seasons")).onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().getUpcomingShows().onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().getShowCast("22598").onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().getShowCharacters("22598").onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().getShowPeople("166303").onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().getShowReviews("22598").onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().getShowSeasons("22598").onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().getShowStudios("22598").onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().getMoreByStudio("22598").onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().getRelatedShows("22598").onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().getRelatedLiteratures("22598").onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().getRelatedGames("22598").onSuccess {
//        println(it)
//    }
//    kurozoraKit.show().rateShow("22598", 5.0,"Master Piece").onSuccess {
//        println(it)
//    }

//-------------------------------------LITERATURES-------------------------------------------
//    filter ile çalışmıyor
//    kurozoraKit.literature().getLiteratures().onSuccess {
//        println(it.data)
//    }
//    kurozoraKit.literature().getLiterature("9", listOf("cast")).onSuccess {
//        println(it)
//    }
//    kurozoraKit.literature().getUpcomingLiteratures().onSuccess {
//        println(it)
//    }
//    kurozoraKit.literature().getLiteratureCast("9").onSuccess {
//        println(it)
//    }
//    kurozoraKit.literature().getLiteratureCharacters("9").onSuccess {
//        println(it)
//    }
//    kurozoraKit.literature().getLiteraturePeople("9").onSuccess {
//        println(it)
//    }
//    kurozoraKit.literature().getLiteratureReviews("9").onSuccess {
//        println(it)
//    }
//    kurozoraKit.literature().getLiteratureStudios("9").onSuccess {
//        println(it)
//    }
//    kurozoraKit.literature().getMoreByStudio("9").onSuccess {
//        println(it)
//    }
//    kurozoraKit.literature().getRelatedShows("9").onSuccess {
//        println(it)
//    }
//    kurozoraKit.literature().getRelatedLiteratures("9").onSuccess {
//        println(it)
//    }
//    kurozoraKit.literature().getRelatedGames("9").onSuccess {
//        println(it)
//    }
//    kurozoraKit.literature().rateLiterature("9", 5.0,"Master Piece").onSuccess {
//        println(it)
//    }
//-------------------------------------GAMES-------------------------------------------
//    filter ile çalışmıyor
//    kurozoraKit.game().getGames().onSuccess {
//        println(it.data)
//    }
//    kurozoraKit.game().getGame("1", listOf("cast")).onSuccess {
//        println(it)
//    }
//    kurozoraKit.game().getUpcomingGames().onSuccess {
//        println(it)
//    }
//    kurozoraKit.game().getGameCast("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.game().getGameCharacters("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.game().getGameStaff("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.game().getGameReviews("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.game().getGameStudios("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.game().getMoreByStudio("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.game().getRelatedShows("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.game().getRelatedLiteratures("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.game().getRelatedGames("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.game().rateGame("1", 4.0).onSuccess {
//        println(it)
//    }

//-------------------------------------AUTH-------------------------------------------
//    kurozoraKit.auth().getFollowList("13995", UsersListType.following).onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().getUserFollowers("13995").onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().getUserFollowing("13995").onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().updateFollowStatus("13995").onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().updateBlockStatus("13995").onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().getBlockedUsers("13995").onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().getUserFavorites("13995", KKLibrary.Kind.SHOWS).onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().getUserLibrary("13395", KKLibrary.Kind.SHOWS, KKLibrary.Status.INPROGRESS).onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().getUserFeedMessages("13395").onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().getUserReviews("13395").onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().searchUsers("luffy").onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().signUp("anotherforv1", "anotherforv1@gmail.com", "C3yA0]h4Rjqa").onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().signIn("anotherforv1@gmail.com", "DcVUYhl=K]4").onSuccess {
//        println(it)
//    }
//    kurozoraKit.auth().resetPassword("anotherforv1@gmail.com").onSuccess {
//        println("Reset password email sent")
//    }
    //
//    kurozoraKit.auth().deleteUser("DcVUYhl=K]4").onSuccess {
//        println("Deleted User")
//    }
//-------------------------------------CHARACTERS-------------------------------------------
//    filter ile çalışmıyor
//    kurozoraKit.character().getCharacters().onSuccess {
//        println(it.data)
//    }
//    kurozoraKit.character().getCharacter("1", listOf("people")).onSuccess {
//        println(it)
//    }
//    kurozoraKit.character().getCharacterPeople("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.character().getCharacterShows("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.character().getCharacterLiteratures("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.character().getCharacterGames("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.character().getCharacterReviews("1").onSuccess {
//        println(it)
//    }
//    kurozoraKit.character().rateCharacter("1", 4.0).onSuccess {
//        println(it)
//    }
//    kurozoraKit.character().deleteCharacterRating("1").onSuccess {
//        println(it)
//    }
//-------------------------------------SEASONS-------------------------------------------
//    kurozoraKit.season().getDetails("20").onSuccess {
//        println(it)
//    }
//    kurozoraKit.season().getEpisodes("20").onSuccess {
//        println(it)
//    }
//    kurozoraKit.season().updateWatchStatus("20").onSuccess {
//        println(it)
//    }
//-------------------------------------EPISODES-------------------------------------------
//    kurozoraKit.episode().getEpisode("892").onSuccess {
//        println(it)
//    }
//    kurozoraKit.episode().getEpisodeSuggestions("892").onSuccess {
//        println(it)
//    }
//    kurozoraKit.episode().updateEpisodeWatchStatus("892").onSuccess {
//        println("Episode watch status: $it")
//    }
//-------------------------------------GENRE-------------------------------------------
//    kurozoraKit.genre().getGenres().onSuccess {
//        println(it)
//    }
//    kurozoraKit.genre().getGenre("5").onSuccess {
//        println(it)
//    }
//-------------------------------------THEME-------------------------------------------
//    kurozoraKit.theme().getThemes().onSuccess {
//        println(it)
//    }
//    kurozoraKit.theme().getTheme("19").onSuccess {
//        println(it)
//    }
//-------------------------------------EXPLORE-------------------------------------------
//    kurozoraKit.explore().getExplore().onSuccess {
//        println(it)
//    }
//    kurozoraKit.explore().getExplore("27").onSuccess {
//        println(it)
//    }
//    kurozoraKit.explore().getExplore(genreId = "1", "19").onSuccess {
//        println(it)
//    }
//-------------------------------------SEARCH-------------------------------------------
//    kurozoraKit.search().search(KKSearchScope.kurozora, listOf(KKSearchType.shows, KKSearchType.literatures), "One Punch Man").onSuccess {
//        println(it)
//    }
//    kurozoraKit.search().getSearchSuggestions(KKSearchScope.kurozora, listOf(KKSearchType.shows, KKSearchType.literatures), "One Punch Man").onSuccess {
//        println(it)
//    }
//-------------------------------------FEED-------------------------------------------645
//    kurozoraKit.feed().getHomeFeed().onSuccess {
//        println(it)
//    }
//    kurozoraKit.feed().getExploreFeed().onSuccess {
//        println(it)
//    }
//    kurozoraKit.feed().getFeedMessage("645").onSuccess {
//        println(it)
//    }
//    kurozoraKit.feed().getFeedMessageReplies("556").onSuccess {
//        println(it)
//    }
//    kurozoraKit.feed().heartFeedMessage("645").onSuccess {
//        println(it)
//    }
//    kurozoraKit.feed().pinFeedMessage("645").onSuccess {
//        println(it)
//    }
//    kurozoraKit.feed().updateFeedMessage("645", FeedMessageRequest("bla bla", isNSFW = false, isSpoiler = false)).onSuccess {
//        println(it)
//    }
//-------------------------------------IMAGE-------------------------------------------
//    kurozoraKit.image().getRandomImages(MediaKind.shows, MediaCollection.poster).onSuccess {
//        println(it)
//    }
//-------------------------------------LEGAL-------------------------------------------
//    kurozoraKit.legal().getPrivacyPolicy().onSuccess {
//        println(it)
//    }
//    kurozoraKit.legal().getTermsOfUse().onSuccess {
//        println(it)
//    }
//-------------------------------------MISC (NOT WORKING FOR NOW)-------------------------------------------
//-------------------------------------PERSON (NOT TESTED, TOO LAZY, STUPID ERRORS)-------------------------------------------
//    kurozoraKit.people().getPeople().onSuccess {
//        println(it)
//    }
//    kurozoraKit.people().getPerson("176385").onSuccess {
//        println(it)
//    }
//    kurozoraKit.people().getPersonGames("2334").onSuccess {
//        println(it)
//    }
//    kurozoraKit.people().getPersonLiteratures("2334").onSuccess {
//        println(it)
//    }
//    kurozoraKit.people().getPersonShows("2334").onSuccess {
//        println(it)
//    }
//    kurozoraKit.people().getPersonReviews("2334").onSuccess {
//        println(it)
//    }
//    kurozoraKit.people().ratePerrson("2334", rating = 4.2, review = "Very talented team!").onSuccess {
//        println(it)
//    }
//-------------------------------------REVIEW-------------------------------------------
//    kurozoraKit.review().deleteReview("26128")
//-------------------------------------SCHEDULE-------------------------------------------
//    kurozoraKit.schedule().getSchedule(KKScheduleType.shows, "").onSuccess {
//        println(it)
//    }
//-------------------------------------SONG-------------------------------------------
//    kurozoraKit.song().getSongs().onSuccess {
//        println(it)
//    }
    // doesnt working with relationships (backend side)
//    kurozoraKit.song().getSong("3", listOf("games")).onSuccess {
//        println(it)
//    }
//    kurozoraKit.song().getSongShows("3").onSuccess {
//        println(it)
//    }
//    kurozoraKit.song().getSongGames("3").onSuccess {
//        println(it)
//    }
//    kurozoraKit.song().rateSong("3", rating = 4.5, review = "Amazing!").onSuccess {
//        println(it)
//    }
//    kurozoraKit.song().getSongReviews("3").onSuccess {
//        println(it)
//    }
//-------------------------------------STORE (NOT TESTED)-------------------------------------------
//-------------------------------------STUDIO-------------------------------------------
//    kurozoraKit.studio().getStudios().onSuccess {
//        println(it)
//    }
//    kurozoraKit.studio().getStudio("2334").onSuccess {
//        println(it)
//    }
//    kurozoraKit.studio().getStudioGames("2334").onSuccess {
//        println(it)
//    }
//    kurozoraKit.studio().getStudioLiteratures("2334").onSuccess {
//        println(it)
//    }
//    kurozoraKit.studio().getStudioShows("2334").onSuccess {
//        println(it)
//    }
//    kurozoraKit.studio().getStudioReviews("2334").onSuccess {
//        println(it)
//    }
//    kurozoraKit.studio().rateStudio("2334", rating = 4.2, review = "Very talented team!").onSuccess {
//        println(it)
//    }
//-------------------------------------THEME STORE-------------------------------------------
//    kurozoraKit.themeStore().getThemeStore().onSuccess {
//        println(it)
//    }
//    kurozoraKit.themeStore().getThemeStoreItem("1").onSuccess {
//        println(it)
//    }



}
