package kurozorakit.shared

class UserAgent (
    private val name: String = "",
    private val version: String = "",
) {
    fun getName(): String{
        return name
    }

    fun getVersion(): String {
        return version
    }
}