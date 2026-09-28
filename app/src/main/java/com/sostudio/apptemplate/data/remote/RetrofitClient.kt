package com.sostudio.apptemplate.data.remote

import android.content.Context
import android.os.Build
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.sostudio.apptemplate.BuildConfig
import com.sostudio.apptemplate.data.db.AppDatabase
import com.sostudio.apptemplate.data.db.performLogout
import com.sostudio.apptemplate.data.prefs.PrefsHelper
import com.sostudio.apptemplate.util.NetworkConnectionInterceptor
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Single shared Retrofit instance for the app, wrapped as a Koin single so it's
 * constructor-injected — makes RetrofitClient/BaseService mockable in tests.
 */
class RetrofitClient(
    private val context: Context,
    private val prefsHelper: PrefsHelper,
    private val appDatabase: AppDatabase
) {
    @Volatile
    private var retrofit: Retrofit? = null
    private val okHttpClient by lazy { OkHttpClient() }
    private val logoutScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val headerInterceptor: Interceptor
        get() = Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder().apply {
                    val authToken = prefsHelper.authToken
                    if (!authToken.isNullOrBlank()) {
                        addHeader("Authorization", "Bearer $authToken")
                    }
                    addHeader("Accept", "application/json")
                    addHeader("Content-Type", "application/json")
                    addHeader("App-Platform", "Android")
                    addHeader("App-Version", BuildConfig.VERSION_NAME)
                    addHeader("App-OS-Version", Build.VERSION.RELEASE)
                }.build()
            )
        }

    private fun getRetrofitClient(): Retrofit =
        retrofit ?: synchronized(this) {
            retrofit ?: buildRetrofitClient().also { retrofit = it }
        }

    private fun buildRetrofitClient(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_HOST)
            .addConverterFactory(json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .client(buildOkHttpClient())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()

    // Runs after headerInterceptor so `chain.request()` here already carries the Authorization
    // header — that's what distinguishes an expired/invalid session token (401 on an
    // authenticated call, should log out) from a login attempt's "Invalid credentials" 401
    // (no Authorization header sent, must NOT log out since there's no session to end).
    private val authFailureInterceptor: Interceptor
        get() = Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            if (response.code == 401 &&
                request.header("Authorization") != null &&
                prefsHelper.authToken != null
            ) {
                prefsHelper.logoutReason = "You were signed out because your session expired."
                // Off the OkHttp dispatcher thread: clearAllTables() is a blocking Room call,
                // and this thread pool is shared across every in-flight request.
                logoutScope.launch { performLogout(appDatabase, prefsHelper) }
            }
            response
        }

    private fun buildOkHttpClient(): OkHttpClient =
        okHttpClient.newBuilder().apply {
            // OkHttp's per-stage defaults are 10s and there's no overall cap — a request
            // that trickles bytes slowly would otherwise never fail. callTimeout bounds the
            // whole call (connect + write + response + redirects).
            callTimeout(30, TimeUnit.SECONDS)
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            addInterceptor(NetworkConnectionInterceptor(context))
            addInterceptor(headerInterceptor)
            addInterceptor(authFailureInterceptor)
            if (BuildConfig.DEBUG) {
                addInterceptor(OkHttpProfilerInterceptor())
            }
        }.build()

    fun <T> createService(tClass: Class<T>): T = getRetrofitClient().create(tClass)

    companion object {
        val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            encodeDefaults = true
            isLenient = true
            prettyPrint = BuildConfig.DEBUG
        }
    }
}
