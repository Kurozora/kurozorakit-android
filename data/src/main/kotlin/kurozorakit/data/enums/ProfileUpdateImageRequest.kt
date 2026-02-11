package kurozorakit.data.enums

/**
 * Represents a request to modify a user's profile imagery (e.g., Avatar or Banner).
 *
 * This sealed class handles the three primary states of an image update:
 * 1. Setting a new image via a URL.
 * 2. Keeping the existing image (by not sending a request).
 * 3. Explicitly removing the image to revert to a default placeholder.
 */
sealed class ProfileUpdateImageRequest {

    /**
     * Updates the profile image with a new resource.
     *
     * @property url The remote storage URL of the new image.
     * If `null`, it may be treated as a no-op depending on API implementation.
     */
    data class update(val url: String?) : ProfileUpdateImageRequest()

    /**
     * Signals the server to delete the current image and reset the field
     * to the system's default state.
     */
    data object delete : ProfileUpdateImageRequest()
}