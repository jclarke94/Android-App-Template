package com.sostudio.apptemplate.data.remote.model.response

import com.sostudio.apptemplate.util.NoConnectivityException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/** A generic class that holds a value with its loading status. */
sealed class APIResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : APIResult<T>()

    data class Error(
        val responseCode: Int,
        val message: String,
        val throwable: Throwable? = null
    ) : APIResult<Nothing>() {
        constructor(message: String, throwable: Throwable? = null) :
            this(responseCode = -1, message = message, throwable = throwable)

        val exception: Throwable
            get() = throwable ?: APIError(message)

        fun isNetworkError(): Boolean = throwable is NoConnectivityException
    }

    override fun toString(): String = when (this) {
        is Success<*> -> "Success[data=$data]"
        is Error -> "Error[message=\"$message\",exception=$exception]"
    }
}

class APIError(message: String) : Exception(message)

@OptIn(ExperimentalContracts::class)
fun <T : Any> APIResult<T>?.isSuccess(): Boolean {
    contract { returns(true) implies (this@isSuccess is APIResult.Success) }
    return this is APIResult.Success
}

@OptIn(ExperimentalContracts::class)
fun <T : Any> APIResult<T>?.isError(): Boolean {
    contract { returns(true) implies (this@isError is APIResult.Error) }
    return this is APIResult.Error
}

@OptIn(ExperimentalContracts::class)
inline fun <T : Any> APIResult<T>.onSuccess(action: (T) -> Unit): APIResult<T> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (this is APIResult.Success) action(data)
    return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T : Any> APIResult<T>.onError(action: (APIResult.Error) -> Unit): APIResult<T> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (this is APIResult.Error) action(this)
    return this
}
