package kurozora.kit.data.models.user.update.block

import enums.BlockStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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