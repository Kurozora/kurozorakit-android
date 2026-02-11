package kurozorakit.shared

import kotlinx.serialization.Serializable

/**
 * The top-level data structure returned by the Kurozora API when a request fails.
 * * This class maps the JSON body of an error response into a type-safe object.
 * It is typically used by the internal interceptors to convert raw HTTP failures
 * into a [KurozoraError.ApiError].
 *
 * @property error The specific [ApiError] details provided by the server.
 */
@Serializable
data class ErrorResponse(
    val error: ApiError
)

/**
 * Detailed information about a specific server-side error.
 * * This model follows a structure similar to the JSON:API error spec, providing
 * both machine-readable identifiers (id, status) and human-readable descriptions
 * (title, detail).
 *
 * @property id A unique internal code for the specific error type.
 * @property status The HTTP status code associated with this error (e.g., 400, 403, 500).
 * @property title A short, human-readable summary of the problem that SHOULD NOT change from occurrence to occurrence.
 * @property detail A human-readable explanation specific to this occurrence of the problem, often providing context on how to fix it.
 */
@Serializable
data class ApiError(
    val id: Int,
    val status: Int,
    val title: String,
    val detail: String
)