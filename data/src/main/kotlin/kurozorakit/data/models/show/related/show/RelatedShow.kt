package kurozorakit.data.models.show.related

import kotlinx.serialization.Serializable
import kurozorakit.data.models.media.MediaRelation
import kurozorakit.data.models.show.Show
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class RelatedShow @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid = Uuid.random(),
    val show: Show,
    var attributes: Attributes,
) {
    @OptIn(ExperimentalUuidApi::class)
    override fun equals(other: Any?): Boolean {
        return (other is RelatedShow) && this.id == other.id
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun hashCode(): Int {
        return id.hashCode()
    }

    @Serializable
    data class Attributes(
        val relation: MediaRelation,
    )
}
