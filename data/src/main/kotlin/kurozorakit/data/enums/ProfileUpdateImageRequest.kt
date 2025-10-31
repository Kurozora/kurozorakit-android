package kurozorakit.data.enums

sealed class ProfileUpdateImageRequest {
    data class update(val url: String?) : ProfileUpdateImageRequest()
    object delete : ProfileUpdateImageRequest()
}
