package com.sostudio.apptemplate.data.repo.base

import android.util.Log
import com.sostudio.apptemplate.data.remote.BaseService
import com.sostudio.apptemplate.data.remote.ServiceManager
import com.sostudio.apptemplate.data.remote.model.response.APIResult
import com.sostudio.apptemplate.data.remote.model.response.ErrorBody
import com.sostudio.apptemplate.data.remote.model.response.GenericResponse
import com.sostudio.apptemplate.util.NoConnectivityException
import com.sostudio.apptemplate.util.asNoConnectivityException
import com.sostudio.apptemplate.util.firstNotNullOrBlank
import com.sostudio.apptemplate.util.ifNullOrBlank
import com.sostudio.apptemplate.util.parseBody
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.retrofit.statusCode
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
abstract class BaseRepo {
    // Public rather than protected: the `lazy {}` builder below isn't itself inline, so its
    // lambda compiles to a real class outside BaseRepo — JVM `protected` access from that
    // synthetic class throws IllegalAccessError at runtime even though it compiles cleanly.
    abstract val serviceManager: ServiceManager

    companion object {
        const val RESPONSE_EXCEPTION_CODE = -1
    }

    protected inline fun <reified T : BaseService.API> lazyService(): Lazy<BaseService<T>> =
        lazy { serviceManager.getService() }

    protected fun errorMessageFrom(response: ApiResponse.Failure<*>): String = when (response) {
        is ApiResponse.Failure.Error -> {
            val body = response.errorBody.parseBody<ErrorBody>()
            firstNotNullOrBlank(
                { body?.error },
                { body?.message },
                { body?.errors?.values?.firstOrNull()?.firstOrNull() },
                { response.message() },
                fallback = { "Unknown error" }
            )
        }

        is ApiResponse.Failure.Exception -> firstNotNullOrBlank(
            { response.throwable.message },
            { response.message() },
            fallback = { "Unknown error" }
        )
    }

    protected inline fun <T : Any> doAPICall(
        logDescription: String,
        call: () -> ApiResponse<GenericResponse<T>>
    ): APIResult<T> {
        contract { callsInPlace(call, kotlin.contracts.InvocationKind.EXACTLY_ONCE) }
        return when (val response = call()) {
            is ApiResponse.Success -> {
                val result = response.data
                if ((result.success ?: true) && result.data != null) {
                    APIResult.Success(result.data)
                } else {
                    val message = result.error.ifNullOrBlank { "Unknown error" }
                    Log.e("BASE_REPO", "$logDescription failed! message:\"$message\"")
                    APIResult.Error(response.statusCode.code, message)
                }
            }

            is ApiResponse.Failure.Error -> {
                val message = errorMessageFrom(response)
                Log.e("BASE_REPO", "$logDescription failed! code:${response.statusCode.code}, message:\"$message\"")
                APIResult.Error(response.statusCode.code, message)
            }

            is ApiResponse.Failure.Exception -> handleException(logDescription, response)
        }
    }

    protected inline fun doAPICallWithEmptyResponse(
        logDescription: String,
        call: () -> ApiResponse<GenericResponse<*>>
    ): APIResult<Unit> {
        contract { callsInPlace(call, kotlin.contracts.InvocationKind.EXACTLY_ONCE) }
        return when (val response = call()) {
            is ApiResponse.Success -> APIResult.Success(Unit)

            is ApiResponse.Failure.Error -> {
                val message = errorMessageFrom(response)
                Log.e("BASE_REPO", "$logDescription failed! code:${response.statusCode.code}, message:\"$message\"")
                APIResult.Error(response.statusCode.code, message)
            }

            is ApiResponse.Failure.Exception -> handleException(logDescription, response)
        }
    }

    protected fun handleException(
        logDescription: String,
        response: ApiResponse.Failure.Exception
    ): APIResult.Error = when (response.throwable) {
        is NoConnectivityException,
        is UnknownHostException,
        is ConnectException,
        is SocketException,
        is SocketTimeoutException -> {
            Log.w("BASE_REPO", "$logDescription failed due to no connection!", response.throwable)
            APIResult.Error(RESPONSE_EXCEPTION_CODE, "No Internet Connection", response.throwable.asNoConnectivityException())
        }

        else -> {
            val message = firstNotNullOrBlank(
                { response.throwable.message },
                { response.message() },
                fallback = { "Unknown error" }
            )
            Log.e("BASE_REPO", "$logDescription failed with exception! message:\"$message\"", response.throwable)
            APIResult.Error(RESPONSE_EXCEPTION_CODE, message, response.throwable)
        }
    }
}
