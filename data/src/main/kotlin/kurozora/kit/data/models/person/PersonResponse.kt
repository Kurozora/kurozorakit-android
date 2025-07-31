package kurozora.kit.data.models.person

@kotlinx.serialization.Serializable
data class PersonResponse(
    val data: Person,
    val next: String?
)