package kurozora.kit.data.models.user

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozora.kit.data.enums.ActivityStatus
import kurozora.kit.data.enums.BlockStatus
import kurozora.kit.data.enums.FollowStatus
import kurozora.kit.data.enums.UserRole
import kurozora.kit.data.models.IdentityResource
import kurozora.kit.data.models.LocalDateSerializer
import kurozora.kit.data.models.media.Media
import kurozora.kit.data.models.user.achievement.AchievementResponse
import kurozora.kit.data.models.user.receipt.Receipt
import kurozora.kit.data.models.user.tokens.AccessTokenResponse
import kurozora.kit.data.models.user.update.UserUpdate
import kurozora.kit.data.models.user.update.block.BlockUpdate
import kurozora.kit.data.models.user.update.follow.FollowUpdate

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
        var current: User? = null
        val isSignedIn: Boolean
            get() = current != null
        val isPro: Boolean
            get() = current?.attributes?.isPro ?: false
        val isSubscribed: Boolean
            get() = current?.attributes?.isSubscribed ?: false
    }

    fun updateDetails(with: User) {
        attributes.profile = with.attributes.profile
        attributes.banner = with.attributes.banner
        attributes.biography = with.attributes.biography
    }

    @Serializable
    data class Attributes(
        var slug: String,
        var username: String,
        val email: String? = null,
        val siwaIsEnabled: Boolean? = null,
        var biography: String? = null,
        var biographyHTML: String? = null,
        var biographyMarkdown: String? = null,
        val activityStatus: ActivityStatus,
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
        @SerialName("isBlocked")
        private val isBlocked: Boolean? = null,
        @SerialName("_blockStatus")
        private var _blockStatus: BlockStatus? = null,
        @SerialName("isFollowed")
        private val isFollowed: Boolean? = null,
        @SerialName("_followStatus")
        private var _followStatus: FollowStatus? = null
    ) {
        var blockStatus: BlockStatus
            get() = _blockStatus ?: BlockStatus.fromBoolean(isBlocked)
            set(value) { _blockStatus = value }

        var followStatus: FollowStatus
            get() = _followStatus ?: FollowStatus.fromBoolean(isFollowed)
            set(value) { _followStatus = value }

        fun update(using: BlockUpdate) {
            blockStatus = using.blockStatus
        }

        fun updated(using: BlockUpdate): Attributes =
            copy().also { it.blockStatus = using.blockStatus }

        fun update(using: FollowUpdate) {
            followStatus = using.followStatus
        }

        fun updated(using: FollowUpdate): Attributes =
            copy().also { it.followStatus = using.followStatus }

        fun update(using: UserUpdate) {
            slug = using.username.toString()
            username = using.nickname.toString()
            biography = using.biography
            profile = using.profile
            banner = using.banner
            preferredLanguage = using.preferredLanguage
            preferredTVRating = using.preferredTVRating
            preferredTimezone = using.preferredTimezone
        }

        fun updated(using: UserUpdate): Attributes =
            copy().also {
                it.slug = using.username.toString()
                it.username = using.nickname.toString()
                it.biography = using.biography
                it.profile = using.profile
                it.banner = using.banner
                it.preferredLanguage = using.preferredLanguage
                it.preferredTVRating = using.preferredTVRating
                it.preferredTimezone = using.preferredTimezone
            }

        fun updateSubscription(from: Receipt) {
            val valid = from.attributes.isValid
            isSubscribed = valid
            isPro = valid
        }
    }

    @Serializable
    data class Relationships(
        val accessTokens: AccessTokenResponse? = null,
        val achievements: AchievementResponse? = null
    )
}
