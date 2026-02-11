package kurozorakit.data.enums

import kotlinx.serialization.Serializable

/**
 * Defines the administrative privilege levels for authenticated users.
 *
 * This enum is used to gate access to protected dashboard features,
 * content management tools, and sensitive system configurations.
 * Permissions are additive, with higher roles inheriting the capabilities
 * of those below them.
 *
 * @property value The unique integer identifier used for authorization checks.
 */
@Serializable
enum class UserRole(val value: Int) {
    /** * The highest level of authority.
     * Can manage system settings, billing, and other administrative accounts.
     */
    superAdmin(1),

    /** * Global moderator and staff.
     * Can manage users, process reports, and oversee the entire content library.
     */
    admin(2),

    /** * Content contributor.
     * Focused on database accuracy; can add or edit media, characters, and studio metadata.
     */
    editor(3);
}