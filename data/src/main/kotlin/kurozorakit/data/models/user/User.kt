package kurozorakit.data.models.user

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozorakit.data.enums.ActivityStatus
import kurozorakit.data.enums.BlockStatus
import kurozorakit.data.enums.FollowStatus
import kurozorakit.data.enums.UserRole
import kurozorakit.data.models.IdentityResource
import kurozorakit.data.models.LocalDateSerializer
import kurozorakit.data.models.media.Media
import kurozorakit.data.models.user.achievement.AchievementResponse
import kurozorakit.data.models.user.receipt.Receipt
import kurozorakit.data.models.user.tokens.AccessTokenResponse
import kurozorakit.data.models.user.update.UserUpdate
import kurozorakit.data.models.user.update.block.BlockUpdate
import kurozorakit.data.models.user.update.follow.FollowUpdate

/**
 * Represents a Kurozora user account.
 * * This model encapsulates user identity, profile details, social status (following/blocking),
 * and platform-specific perks like Pro or Staff status. It also acts as the central
 * session controller via the [current] singleton.
 *
 * @property id The unique numeric ID of the user.
 * @property uuid The universally unique identifier for the user.
 * @property type The resource type, typically "users".
 * @property href The canonical API link to the user's profile.
 * @property attributes Detailed profile data, preferences, and permissions.
 * @property relationships Connections to achievements and authentication tokens.
 */
@Serializable
data class User(
    override val id: String,
    val uuid: String,
    override val type: String,
    override val href: String,
    val attributes: Attributes,
    val relationships: Relationships? = null
) : IdentityResource {

    companion object {
        /** The currently authenticated user instance. */
        var current: User? = null

        /** Checks if a user session is active. */
        val isSignedIn: Boolean get() = current != null

        /** Checks if the current user has 'Pro' status. */
        val isPro: Boolean get() = current?.attributes?.isPro ?: false

        /** Checks if the current user has an active subscription. */
        val isSubscribed: Boolean get() = current?.attributes?.isSubscribed ?: false
    }

    /** Updates the core visual details of the current user instance. */
    fun updateDetails(with: User) {
        attributes.profile = with.attributes.profile
        attributes.banner = with.attributes.banner
        attributes.biography = with.attributes.biography
    }

    /**
     * User profile attributes and metadata.
     * * @property slug URL-friendly identifier for the user.
     * @property username Display name of the user.
     * @property email User's email address (usually private/null for others).
     * @property siwaIsEnabled Indicates if 'Sign in with Apple' is active.
     * @property biography Short bio written by the user.
     * @property activityStatus The user's online presence (e.g., Online, Away).
     * @property profile Avatar image resource.
     * @property banner Profile background image resource.
     * @property isStaff True if the user is a Kurozora staff member.
     * @property isPro True if the user has purchased a Pro membership.
     * @property isSubscribed True if the user has an active recurring subscription.
     * @property joinedAt The date the account was created.
     * @property followerCount Number of users following this person.
     * @property followStatus Social relationship status relative to the current user.
     */
    @Serializable
    data class Attributes(
        var slug: String,
        var username: String,
        val email: String? = null,
        val siwaIsEnabled: Boolean? = null,
        var biography: String? = null,
        var biographyHTML: String? = null,
        var biographyMarkdown: String? = null,
        val activityStatus: String,
        var profile: Media? = null,
        var banner: Media? = null,
        var isDeveloper: Boolean,
        var isStaff: Boolean,
        var isEarlySupporter: Boolean,
        var isPro: Boolean,
        var isSubscribed: Boolean,
        var isVerified: Boolean,
        @Serializable(with = LocalDateSerializer::class)
        val joinedAt: LocalDate?,
        @Serializable(with = LocalDateSerializer::class)
        val subscribedAt: LocalDate? = null,
        var followerCount: Int,
        var followingCount: Int,
        val reputationCount: Int,
        val ratingsCount: Int,
        var preferredLanguage: String? = null,
        var preferredTVRating: Int? = null,
        var preferredTimezone: String? = null,
        val canChangeUsername: Boolean? = null,
        val role: UserRole? = null,
        @SerialName("isBlocked") private val isBlocked: Boolean? = null,
        @SerialName("_blockStatus") private var _blockStatus: BlockStatus? = null,
        @SerialName("isFollowed") private val isFollowed: Boolean? = null,
        @SerialName("_followStatus") private var _followStatus: FollowStatus? = null
    ) {
        /** Safe accessor for the block status, reconciling boolean and enum fields. */
        var blockStatus: BlockStatus
            get() = _blockStatus ?: BlockStatus.fromBoolean(isBlocked)
            set(value) { _blockStatus = value }

        /** Safe accessor for the follow status, reconciling boolean and enum fields. */
        var followStatus: FollowStatus
            get() = _followStatus ?: FollowStatus.fromBoolean(isFollowed)
            set(value) { _followStatus = value }

    }

    @Serializable
    data class Relationships(
        val accessTokens: AccessTokenResponse? = null,
        val achievements: AchievementResponse? = null
    )
}