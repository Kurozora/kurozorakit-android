package kurozorakit.shared

/**
 * A sealed class representing the result of an operation that can either succeed or fail.
 */
sealed class Result<out T> {
    /**
     * Represents a successful result with a value.
     */
    data class Success<T>(val data: T) : Result<T>()

    /**
     * Represents a failed result with an error.
     */
    data class Error(val error: KurozoraError) : Result<Nothing>()

    /**
     * Returns true if this result is a success.
     */
    val isSuccess: Boolean get() = this is Success

    /**
     * Returns true if this result is an error.
     */
    val isError: Boolean get() = this is Error

    /**
     * Returns the value if this result is a success, or null if it is an error.
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
    }

    /**
     * Returns the error if this result is an error, or null if it is a success.
     */
    fun errorOrNull(): KurozoraError? = when (this) {
        is Success -> null
        is Error -> error
    }

    /**
     * Maps the value of this result if it is a success.
     */
    fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(error)
    }

    /**
     * Executes the given block if this result is a success.
     */
    inline fun onSuccess(block: (T) -> Unit): Result<T> {
        if (this is Success) block(data)
        return this
    }

    /**
     * Executes the given block if this result is an error.
     */
    inline fun onError(block: (KurozoraError) -> Unit): Result<T> {
        if (this is Error) block(error)
        return this
    }
}