package kurozora.kit.data.enums

enum class UsersListType {
    followers,
    following;

    val stringValue: String
        get() = when (this) {
            followers -> "Followers"
            following -> "Following"
        }
}
