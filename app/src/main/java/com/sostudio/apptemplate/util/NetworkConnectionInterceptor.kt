package com.sostudio.apptemplate.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkConnectionInterceptor(context: Context) : Interceptor {
    private val appContext: Context = context.applicationContext

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable(appContext)) {
            Log.v("NetworkConnectionInterceptor", "No internet connection")
            throw NoConnectivityException()
        }
        return chain.proceed(chain.request())
    }

    companion object {
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }
}

/** Thrown by [NetworkConnectionInterceptor] when there is no internet connection. */
class NoConnectivityException : IOException {
    constructor() : super()
    constructor(cause: Throwable) : super(cause)

    override val message: String
        get() = "No Internet Connection"
}

fun Throwable.asNoConnectivityException(): NoConnectivityException =
    if (this is NoConnectivityException) this else NoConnectivityException(this)
