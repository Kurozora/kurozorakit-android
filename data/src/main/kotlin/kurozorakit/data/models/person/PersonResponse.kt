package kurozorakit.data.models.person

@kotlinx.serialization.Serializable
data class PersonResponse(
    val data: List<Person>,
    val next: String?
)