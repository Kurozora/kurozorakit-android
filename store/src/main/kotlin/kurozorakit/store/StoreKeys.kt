package kurozorakit.store

/**
 * Sealed hierarchy representing all possible Store keys for offline-first data access.
 * 
 * These keys are used to identify specific data queries in the Store5 system,
 * enabling proper caching, fetching, and source-of-truth management.
 */
sealed class StoreKey {
    /** Entity type name for cache metadata */
    abstract val entityType: String
    
    /** Unique cache key string */
    abstract val cacheKey: String
}

// =============================================================================
// SHOW KEYS
// =============================================================================

sealed class ShowKey : StoreKey() {
    override val entityType: String = "shows"
    
    /** Fetch a single show by ID */
    data class ById(val id: String, val relationships: List<String> = emptyList()) : ShowKey() {
        override val cacheKey: String = "show:$id:${relationships.sorted().joinToString(",")}"
    }
    
    /** Fetch shows by multiple IDs */
    data class ByIds(val ids: List<String>) : ShowKey() {
        override val cacheKey: String = "shows:${ids.sorted().joinToString(",")}"
    }
    
    /** Fetch paginated list of all shows */
    data class All(val page: Int = 1, val limit: Int = 20) : ShowKey() {
        override val cacheKey: String = "shows:all:$page:$limit"
    }
    
    /** Fetch upcoming shows */
    data class Upcoming(val page: Int = 1, val limit: Int = 20) : ShowKey() {
        override val cacheKey: String = "shows:upcoming:$page:$limit"
    }
    
    /** Fetch shows by status */
    data class ByStatus(val status: String, val page: Int = 1, val limit: Int = 20) : ShowKey() {
        override val cacheKey: String = "shows:status:$status:$page:$limit"
    }
    
    /** Fetch shows by air season */
    data class ByAirSeason(val season: String, val page: Int = 1, val limit: Int = 20) : ShowKey() {
        override val cacheKey: String = "shows:season:$season:$page:$limit"
    }
    
    /** Fetch related shows */
    data class Related(val showId: String) : ShowKey() {
        override val cacheKey: String = "shows:related:$showId"
    }
    
    /** Fetch shows from same studio */
    data class MoreByStudio(val showId: String, val page: Int = 1, val limit: Int = 20) : ShowKey() {
        override val cacheKey: String = "shows:studio:$showId:$page:$limit"
    }
    
    /** Search shows */
    data class Search(val query: String, val limit: Int = 20) : ShowKey() {
        override val cacheKey: String = "shows:search:$query:$limit"
    }
}

// =============================================================================
// LITERATURE KEYS
// =============================================================================

sealed class LiteratureKey : StoreKey() {
    override val entityType: String = "literatures"

    /** Tekil kitap/manga detayları */
    data class ById(
        val id: String,
        val relationships: List<String> = emptyList()
    ) : LiteratureKey() {
        override val cacheKey: String = "literature:$id:${relationships.sorted().joinToString(",")}"
    }

    /** Liste sorguları (ID listesi ile) */
    data class ByIds(val ids: List<String>) : LiteratureKey() {
        override val cacheKey: String = "literatures:${ids.sorted().joinToString(",")}"
    }
}

// =============================================================================
// GAME KEYS
// =============================================================================

sealed class GameKey : StoreKey() {
    override val entityType: String = "games"

    /** Tekil oyun detayı */
    data class ById(
        val id: String,
        val relationships: List<String> = emptyList()
    ) : GameKey() {
        override val cacheKey: String = "game:$id:${relationships.sorted().joinToString(",")}"
    }

    /** ID listesine göre oyunlar */
    data class ByIds(val ids: List<String>) : GameKey() {
        override val cacheKey: String = "games:${ids.sorted().joinToString(",")}"
    }
}

// =============================================================================
// CHARACTER KEYS
// =============================================================================

sealed class CharacterKey : StoreKey() {
    override val entityType: String = "characters"

    /** Tekil karakter detayı */
    data class ById(
        val id: String,
        val relationships: List<String> = emptyList()
    ) : CharacterKey() {
        override val cacheKey: String = "character:$id:${relationships.sorted().joinToString(",")}"
    }

    /** ID listesine göre karakterler */
    data class ByIds(val ids: List<String>) : CharacterKey() {
        override val cacheKey: String = "characters:${ids.sorted().joinToString(",")}"
    }
}

// =============================================================================
// PERSON KEYS
// =============================================================================

sealed class PersonKey : StoreKey() {
    override val entityType: String = "people"

    /** Tekil kişi detayı */
    data class ById(
        val id: String,
        val relationships: List<String> = emptyList()
    ) : PersonKey() {
        override val cacheKey: String = "person:$id:${relationships.sorted().joinToString(",")}"
    }

    /** ID listesine göre kişiler */
    data class ByIds(val ids: List<String>) : PersonKey() {
        override val cacheKey: String = "people:${ids.sorted().joinToString(",")}"
    }
}

// =============================================================================
// STUDIO KEYS
// =============================================================================

sealed class StudioKey : StoreKey() {
    override val entityType: String = "studios"

    /** Tekil stüdyo detayı */
    data class ById(
        val id: String,
        val relationships: List<String> = emptyList()
    ) : StudioKey() {
        override val cacheKey: String = "studio:$id:${relationships.sorted().joinToString(",")}"
    }

    /** Tüm stüdyolar veya liste */
    data object All : StudioKey() {
        override val cacheKey: String = "studios:all"
    }
}

// =============================================================================
// EPISODE KEYS
// =============================================================================

sealed class EpisodeKey : StoreKey() {
    override val entityType: String = "episodes"

    /** Tekil bölüm detayı */
    data class ById(
        val id: String,
        val relationships: List<String> = emptyList()
    ) : EpisodeKey() {
        override val cacheKey: String = "episode:$id:${relationships.sorted().joinToString(",")}"
    }

    /** Bir şova veya sezona ait bölümler */
    data class ByIds(val ids: List<String>) : EpisodeKey() {
        override val cacheKey: String = "episodes:${ids.sorted().joinToString(",")}"
    }
}

// =============================================================================
// SEASON KEYS
// =============================================================================

sealed class SeasonKey : StoreKey() {
    override val entityType: String = "seasons"

    /** Tekil sezon detayı */
    data class ById(val id: String) : SeasonKey() {
        override val cacheKey: String = "season:$id"
    }

    /** Bir şova ait tüm sezonlar */
    data class ByShowId(val showId: String) : SeasonKey() {
        override val cacheKey: String = "seasons:show:$showId"
    }
}

// =============================================================================
// GENRE KEYS
// =============================================================================

sealed class GenreKey : StoreKey() {
    override val entityType: String = "genres"
    
    /** Fetch all genres */
    data object All : GenreKey() {
        override val cacheKey: String = "genres:all"
    }
    
    /** Fetch a single genre by ID */
    data class ById(val id: String) : GenreKey() {
        override val cacheKey: String = "genre:$id"
    }
}

// =============================================================================
// THEME KEYS
// =============================================================================

sealed class ThemeKey : StoreKey() {
    override val entityType: String = "themes"

    /** Tüm temaları getirir */
    data object All : ThemeKey() {
        override val cacheKey: String = "themes:all"
    }

    /** ID bazlı tek bir tema getirir */
    data class ById(val id: String) : ThemeKey() {
        override val cacheKey: String = "theme:$id"
    }
}

// =============================================================================
// USER KEYS
// =============================================================================

sealed class UserKey : StoreKey() {
    override val entityType: String = "users"
    
    /** Fetch current authenticated user */
    data object Me : UserKey() {
        override val cacheKey: String = "user:me"
    }
    
    /** Fetch a user by ID */
    data class ById(val id: String) : UserKey() {
        override val cacheKey: String = "user:$id"
    }
    
    /** Fetch a user by slug */
    data class BySlug(val slug: String) : UserKey() {
        override val cacheKey: String = "user:slug:$slug"
    }
    
    /** Fetch user's followers */
    data class Followers(val userId: String, val page: Int = 1, val limit: Int = 20) : UserKey() {
        override val cacheKey: String = "users:followers:$userId:$page:$limit"
    }
    
    /** Fetch user's following */
    data class Following(val userId: String, val page: Int = 1, val limit: Int = 20) : UserKey() {
        override val cacheKey: String = "users:following:$userId:$page:$limit"
    }
}

// =============================================================================
// EXPLORE KEYS
// =============================================================================

sealed class ExploreKey : StoreKey() {
    override val entityType: String = "explore"

    /** Tüm keşfet kategorilerini (feed) getirir */
    data object All : ExploreKey() {
        override val cacheKey: String = "explore:all"
    }

    /** Belirli bir kategorinin detayını (veya içindeki itemları) getirir */
    data class ById(val id: String) : ExploreKey() {
        override val cacheKey: String = "explore:$id"
    }
}