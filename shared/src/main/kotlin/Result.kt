package kurozorakit.shared

/**
 * A discriminated union representing the outcome of an operation.
 *
 * This sealed class is the standard wrapper for all API and repository responses within
 * KurozoraKit. It forces the handling of both successful data and potential [KurozoraError]
 * states, ensuring type safety and preventing unhandled exceptions.
 *
 * @param T The type of data returned on a successful operation.
 */
sealed class Result<out T> {
    /**
     * Represents a successful operation containing the resulting data.
     *
     * @property data The payload returned by the successful operation.
     */
    data class Success<T>(val data: T) : Result<T>()

    /**
     * Represents a failed operation containing a [KurozoraError].
     *
     * @property error The specific error details explaining why the operation failed.
     */
    data class Error(val error: KurozoraError) : Result<Nothing>()

    /**
     * `true` if the result is an instance of [Success], `false` otherwise.
     */
    val isSuccess: Boolean get() = this is Success

    /**
     * `true` if the result is an instance of [Error], `false` otherwise.
     */
    val isError: Boolean get() = this is Error

    /**
     * Returns the encapsulated data if this is a [Success], or `null` if this is an [Error].
     *
     * @return The data value or null.
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
    }

    /**
     * Returns the encapsulated [KurozoraError] if this is an [Error], or `null` if this is a [Success].
     *
     * @return The error details or null.
     */
    fun errorOrNull(): KurozoraError? = when (this) {
        is Success -> null
        is Error -> error
    }

    /**
     * Transforms the data of a [Success] result using the provided [transform] function.
     * If the result is an [Error], the error is propagated unchanged.
     *
     * @param R The type of the newly transformed result.
     * @param transform A function to apply to the successful data.
     * @return A new [Result] containing the transformed value or the original error.
     */
    fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(error)
    }

    /**
     * Performs the given [block] action if the result is a [Success].
     *
     * @param block A callback that receives the successful data.
     * @return The original [Result] instance for chaining.
     */
    inline fun onSuccess(block: (T) -> Unit): Result<T> {
        if (this is Success) block(data)
        return this
    }

    /**
     * Performs the given [block] action if the result is an [Error].
     *
     * @param block A callback that receives the [KurozoraError].
     * @return The original [Result] instance for chaining.
     */
    inline fun onError(block: (KurozoraError) -> Unit): Result<T> {
        if (this is Error) block(error)
        return this
    }
}