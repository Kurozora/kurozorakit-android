package kurozorakit.data.models.staff

import kotlinx.serialization.Serializable

/**
 * A paginated response container for full [Staff] entities.
 *
 * This class is the primary wrapper for API endpoints that return comprehensive
 * staff information. Each entry in the [data] list provides detailed insights into
 * the staff member's specific role and their personal metadata through nested relationships.
 *
 * @property data A collection of fully populated [Staff] objects, including their roles and linked person data.
 * @property next A pagination link or cursor to retrieve the next page of staff members.
 * Returns `null` if no further pages are available.
 */
@Serializable
data class StaffResponse(
    val data: List<Staff>,
    val next: String? = null,
)