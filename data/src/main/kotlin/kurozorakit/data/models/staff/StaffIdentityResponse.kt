package kurozorakit.data.models.staff

import kotlinx.serialization.Serializable

/**
 * A paginated response container for [StaffIdentity] objects.
 *
 * This structure is used by the Kurozora API to return a collection of staff
 * identification references. It provides a lightweight way to process staff
 * listings without the overhead of full metadata, which is useful for
 * initial synchronization or quick-look grids.
 *
 * @property data A collection of [StaffIdentity] references representing the staff members.
 * @property next A pagination cursor or URL to fetch the next set of staff identities.
 * Returns `null` if the current page is the last one.
 */
@Serializable
data class StaffIdentityResponse(
    val data: List<StaffIdentity>,
    val next: String? = null,
)