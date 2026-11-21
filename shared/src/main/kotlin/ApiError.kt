package kurozorakit.shared

import kotlinx.serialization.Serializable

/**
 * Detailed information about a specific server-side error.
 *
 * This model follows a structure similar to the JSON:API error spec, providing
 * both machine-readable identifiers (id, status) and human-readable descriptions
 * (title, detail). It is embedded in [MetaResponse.errors] for error responses
 * returned by the Kurozora API.
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
    val detail: String,
)
