package kurozorakit.data.models.user.update.block

import kotlinx.serialization.Serializable

/**
 * A top-level data transfer object (DTO) returned after a blocking or unblocking operation.
 * * This response serves to confirm the new state of the relationship between two users,
 * returning the updated blocking metadata wrapped in a consistent `data` field.
 * * @property data The updated [BlockUpdate] object, containing either the new [BlockInfo]
 * details or the simplified boolean status of the block.
 * @see BlockUpdate For details on how the block state is resolved and the union type logic.
 */
@Serializable
data class BlockUpdateResponse(
    val data: BlockUpdate
)