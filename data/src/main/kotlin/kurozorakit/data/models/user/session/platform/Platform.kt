package kurozorakit.data.models.user.session.platform

import kotlinx.serialization.Serializable

/**
 * Represents the technical environment and device specifications of a user session.
 * * This model provides a breakdown of the hardware and software environment from which
 * the user is accessing the service, allowing for platform-specific optimizations or analytics.
 * * @property type The broad classification of the platform (e.g., "Mobile", "Desktop", "Web").
 * @property attributes The specific technical details and metadata of the underlying hardware and OS.
 */
@Serializable
data class Platform(
    val type: String,
    val attributes: Attributes
) {
    /**
     * Contains detailed system and hardware identifiers for a [Platform].
     * * These properties help identify the specific operating system and physical device
     * characteristics. All fields are nullable to accommodate different levels of
     * client-side reporting capabilities.
     * * @property description A human-readable summary of the platform (e.g., "iPhone 13 Pro running iOS 17.1").
     * @property systemName The name of the operating system (e.g., "iOS", "Android", "Windows").
     * @property systemVersion The version of the operating system (e.g., "14.0.1", "API 33").
     * @property deviceVendor The manufacturer of the device (e.g., "Apple", "Samsung", "Google").
     * @property deviceModel The specific model identifier of the device (e.g., "iPhone14,2", "Pixel 7 Pro").
     */
    @Serializable
    data class Attributes(
        val description: String?,
        val systemName: String?,
        val systemVersion: String?,
        val deviceVendor: String?,
        val deviceModel: String?
    )
}