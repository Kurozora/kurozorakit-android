package kurozora.kit.data.models.user.update.block

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozora.kit.data.enums.BlockStatus
import kurozora.kit.data.models.BlockedUnionSerializer
import kurozora.kit.data.models.LocalDateSerializer

@Serializable
data class BlockUpdate(
    @Serializable(with = BlockedUnionSerializer::class)
    @SerialName("isBlocked")
    val blockInfo: BlockInfoOrBoolean? = null,

    @SerialName("_blockStatus")
    private val _blockStatus: BlockStatus? = null
) {
    val blockStatus: BlockStatus
        get() = _blockStatus ?: BlockStatus.fromBoolean(blockInfo?.isBlocked == true)
}

sealed class BlockInfoOrBoolean {
    data class Info(val info: BlockInfo) : BlockInfoOrBoolean()
    data class Bool(val value: Boolean) : BlockInfoOrBoolean()

    val isBlocked: Boolean
        get() = when (this) {
            is Info -> true
            is Bool -> value
        }

    val blockInfoOrNull: BlockInfo?
        get() = (this as? Info)?.info
}

@Serializable
data class BlockInfo(
    @SerialName("user_id") val userId: Long,
    @SerialName("blocked_user_id") val blockedUserId: Long,
    @Serializable(with = LocalDateSerializer::class) val updatedAt: LocalDate?,
    @Serializable(with = LocalDateSerializer::class) val createdAt: LocalDate?,
    @SerialName("id") val id: Long
)
