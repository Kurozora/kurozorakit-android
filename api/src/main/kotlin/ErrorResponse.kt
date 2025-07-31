package kurozora.kit.api

import kotlinx.serialization.Serializable

/**
 * Response returned by the API when an error occurs.
 */
@Serializable
data class ErrorResponse(
    val error: ApiError
)

/**
 * Error returned by the API.
 */
@Serializable
data class ApiError(
    val code: String,
    val message: String
)
