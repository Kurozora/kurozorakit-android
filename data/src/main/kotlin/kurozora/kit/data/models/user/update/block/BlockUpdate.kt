package kurozora.kit.data.models.user.update.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kurozora.kit.data.enums.BlockStatus

@Serializable
data class BlockUpdate(
    @SerialName("isBlocked")
    private var isBlocked: Boolean? = null,
    @SerialName("_blockStatus")
    private var _blockStatus: BlockStatus? = null
) {
    val blockStatus: BlockStatus
        get() = _blockStatus ?: BlockStatus.fromBoolean(isBlocked)
}