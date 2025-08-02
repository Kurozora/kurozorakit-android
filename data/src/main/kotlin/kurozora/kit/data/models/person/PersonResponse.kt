package kurozora.kit.data.models.person

@kotlinx.serialization.Serializable
data class PersonResponse(
    val data: List<Person>,
    val next: String?
)