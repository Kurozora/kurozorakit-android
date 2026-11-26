package kurozorakit.shared

import kotlinx.serialization.Serializable

/**
 * A standardized response envelope used by the Kurozora API for metadata and error reporting.
 *
 * This class serves as a wrapper for most API responses, providing context about the request's
 * status, pagination, and any non-critical or critical errors that may have occurred
 * during processing.
 *
 * @property meta An object containing request-level metadata such as status codes,
 * execution time, and pagination details.
 * @property errors A list of [ApiError] objects. If the request was successful,
 * this list is typically empty. If issues occurred, it provides detailed information
 * about what went wrong.
 */
@Serializable
data class MetaResponse(
    val meta: Meta,
    val errors: List<ApiError> = emptyList(),
)