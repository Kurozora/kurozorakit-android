package kurozorakit.data.models.user.update.block

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozorakit.data.enums.BlockStatus
import kurozorakit.data.models.BlockedUnionSerializer
import kurozorakit.data.models.LocalDateSerializer

/**
 * Represents a request or update payload for a user blocking action.
 *
 * This class uses a flexible structure to handle various API response formats,
 * potentially transitioning from a simple boolean toggle to a full information object.
 *
 * @property blockInfo A union-type property that can contain either a [Boolean] or
 * a [BlockInfo] object. Handled by [BlockedUnionSerializer].
 * @property _blockStatus Internal backing field for the status of the block.
 * @property blockStatus Public accessor that calculates the [BlockStatus]. It prioritizes
 * the explicit [_blockStatus] if available, otherwise infers it from [blockInfo].
 */
@Serializable
data class BlockUpdate(
    @Serializable(with = BlockedUnionSerializer::class)
    @SerialName("isBlocked")
    val blockInfo: BlockInfoOrBoolean? = null,

    @SerialName("_blockStatus")
    private val _blockStatus: BlockStatus? = null,

    @SerialName("isBlockedBy")
    val isBlockedBy: Boolean? = null
) {
    /**
     * Resolves the current block state.
     * * If the server provides an explicit status, it is used. Otherwise, it defaults
     * to checking if [blockInfo] represents a blocked state.
     */
    val blockStatus: BlockStatus
        get() = _blockStatus ?: BlockStatus.fromBoolean(blockInfo?.isBlocked == true)
}

/**
 * A sealed class representing a polymorphic union for block information.
 * * This allows the API to return either a detailed metadata object ([Info]) or
 * a simple presence flag ([Bool]) without breaking the type system.
 */
sealed class BlockInfoOrBoolean {
    /** Represents a detailed block state containing metadata. */
    data class Info(val info: BlockInfo) : BlockInfoOrBoolean()

    /** Represents a simple primitive toggle. */
    data class Bool(val value: Boolean) : BlockInfoOrBoolean()

    /** Returns true if the state represents an active block. */
    val isBlocked: Boolean
        get() = when (this) {
            is Info -> true
            is Bool -> value
        }

    /** Convenience accessor to safely retrieve [BlockInfo] if this is an [Info] instance. */
    val blockInfoOrNull: BlockInfo?
        get() = (this as? Info)?.info
}

/**
 * Detailed metadata concerning a blocking relationship between two users.
 *
 * @property userId The ID of the user who initiated the block.
 * @property blockedUserId The ID of the user who has been blocked.
 * @property updatedAt The date when the block status was last modified.
 * @property createdAt The date when the block was initially created.
 * @property id The unique database identifier for the block record.
 */
@Serializable
data class BlockInfo(
    @SerialName("user_id") val userId: Long,
    @SerialName("blocked_user_id") val blockedUserId: Long,
    @Serializable(with = LocalDateSerializer::class) val updatedAt: LocalDate?,
    @Serializable(with = LocalDateSerializer::class) val createdAt: LocalDate?,
    @SerialName("id") val id: Long
)