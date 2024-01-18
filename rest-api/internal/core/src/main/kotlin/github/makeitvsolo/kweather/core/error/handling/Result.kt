package github.makeitvsolo.kweather.core.error.handling

import github.makeitvsolo.kweather.core.mapping.Into

class UnwrapException internal constructor(message: String) : RuntimeException(message)

interface IntoThrowable : Into<Throwable> {

    fun intoThrowable(): Throwable
}

sealed class Result<T, E : IntoThrowable> {

    abstract val isOk: Boolean
    abstract val isError: Boolean

    abstract fun <U> andThen(map: (T) -> Result<U, E>): Result<U, E>

    abstract fun ifOk(action: (T) -> Unit): Result<T, E>
    abstract fun ifError(action: (E) -> Unit): Result<T, E>

    abstract fun <U> map(map: (T) -> U): Result<U, E>
    abstract fun <O : IntoThrowable> mapError(map: (E) -> O): Result<T, O>

    abstract fun unwrap(): T
    abstract fun unwrapError(): E
    abstract fun unwrapOr(default: T): T
    abstract fun unwrapOrElse(map: (E) -> T): T
    abstract fun unwrapOrElseThrow(map: (E) -> Throwable): T

    companion object {

        fun <U, O : IntoThrowable> ok(value: U): Result<U, O> = Ok(value)
        fun <U, O : IntoThrowable> error(error: O): Result<U, O> = Error(error)
    }
}

internal class Ok<T, E : IntoThrowable> internal constructor(
    private val value: T
) : Result<T, E>() {

    override val isOk: Boolean = true
    override val isError: Boolean = false

    override fun unwrap(): T = value

    override fun unwrapError(): E = throw UnwrapException(
        "unwrap error when value is ok (value: $value)"
    )

    override fun unwrapOr(default: T): T = value

    override fun unwrapOrElse(map: (E) -> T): T = value

    override fun unwrapOrElseThrow(map: (E) -> Throwable): T = value

    override fun <U> map(map: (T) -> U): Result<U, E> = Ok(map(value))

    override fun <O : IntoThrowable> mapError(map: (E) -> O): Result<T, O> = Ok(value)

    override fun <U> andThen(map: (T) -> Result<U, E>): Result<U, E> = map(value)

    override fun ifOk(action: (T) -> Unit): Result<T, E> {
        action(value)
        return this
    }

    override fun ifError(action: (E) -> Unit): Result<T, E> = this
}

internal class Error<T, E : IntoThrowable> internal constructor(
    private val error: E
) : Result<T, E>() {

    override val isOk: Boolean = false
    override val isError: Boolean = true

    override fun unwrap(): T = throw UnwrapException(
        "unwrap when value is error (error: $error)"
    )

    override fun unwrapError(): E = error

    override fun unwrapOr(default: T): T = default

    override fun unwrapOrElse(map: (E) -> T): T = map(error)

    override fun unwrapOrElseThrow(map: (E) -> Throwable): T = throw map(error)

    override fun <U> map(map: (T) -> U): Result<U, E> = Error(error)

    override fun <O : IntoThrowable> mapError(map: (E) -> O): Result<T, O> = Error(map(error))

    override fun <U> andThen(map: (T) -> Result<U, E>): Result<U, E> = Error(error)

    override fun ifOk(action: (T) -> Unit): Result<T, E> = this

    override fun ifError(action: (E) -> Unit): Result<T, E> {
        action(error)
        return this
    }
}
