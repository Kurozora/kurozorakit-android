package kurozora.kit.core

/**
 * A sealed class representing errors that can occur in the KurozoraKit library.
 */
sealed class KurozoraError : Exception() {
    /**
     * An error that occurred during network communication.
     */
    data class NetworkError(val code: Int, override val message: String) : KurozoraError()

    /**
     * An error returned by the Kurozora API.
     */
    data class ApiError(val code: String, override val message: String) : KurozoraError()

    /**
     * An error that occurred during authentication.
     */
    data class AuthenticationError(override val message: String) : KurozoraError()

    /**
     * An error that occurred during data storage or retrieval.
     */
    data class StorageError(override val message: String) : KurozoraError()

    /**
     * An error that occurred during input validation.
     */
    data class ValidationError(override val message: String) : KurozoraError()

    /**
     * An error that occurred when a resource was not found.
     */
    data class NotFoundError(override val message: String) : KurozoraError()

    /**
     * An error that occurred when a request was rate-limited.
     */
    data class RateLimitError(override val message: String, val retryAfter: Int? = null) : KurozoraError()

    /**
     * An unexpected error.
     */
    data class UnknownError(override val message: String, override val cause: Throwable? = null) : KurozoraError()
}
