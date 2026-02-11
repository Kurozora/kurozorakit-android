package kurozorakit.shared

/**
 * Base sealed class for all errors encountered within the KurozoraKit library.
 *
 * This class extends [Exception], allowing it to be thrown if necessary, though it is
 * primarily intended to be wrapped within a [Result.Error] for type-safe error handling.
 */
sealed class KurozoraError : Exception() {
    /**
     * Represents a failure in the underlying network transport layer.
     *
     * @property code The HTTP status code (e.g., 500, 503).
     * @property message A human-readable description of the network failure.
     */
    data class NetworkError(val code: Int, override val message: String) : KurozoraError()

    /**
     * Represents a domain-specific error returned by the Kurozora API.
     *
     * Unlike [NetworkError], this occurs when the server is reached but the request
     * cannot be fulfilled due to business logic.
     *
     * @property code The specific API error code string defined by the Kurozora backend.
     * @property message The error message returned by the API.
     */
    data class ApiError(val code: String, override val message: String) : KurozoraError()

    /**
     * Occurs when an operation fails due to invalid or expired credentials.
     *
     * This may happen during login, token refresh, or when accessing protected resources
     * without a valid session.
     */
    data class AuthenticationError(override val message: String) : KurozoraError()

    /**
     * Indicates a failure when interacting with local persistence.
     *
     * This is commonly triggered by issues with the [kurozorakit.api.TokenProvider] or
     * the [kurozorakit.cache.CacheManager].
     */
    data class StorageError(override val message: String) : KurozoraError()

    /**
     * Triggered when client-side data fails to meet the required format or constraints
     * before being sent to the server.
     */
    data class ValidationError(override val message: String) : KurozoraError()

    /**
     * Represents a 404-equivalent error where the requested resource (Show, User, etc.)
     * does not exist on the server.
     */
    data class NotFoundError(override val message: String) : KurozoraError()

    /**
     * Occurs when the client has sent too many requests in a given amount of time.
     *
     * @property retryAfter The number of seconds to wait before attempting the request again, if provided by the server.
     */
    data class RateLimitError(override val message: String, val retryAfter: Int? = null) : KurozoraError()

    /**
     * A fallback error for unexpected exceptions that do not fit into specific categories.
     *
     * @property message A description of the unexpected behavior.
     * @property cause The original [Throwable] that triggered this error, if available.
     */
    data class UnknownError(override val message: String, override val cause: Throwable? = null) : KurozoraError()
}