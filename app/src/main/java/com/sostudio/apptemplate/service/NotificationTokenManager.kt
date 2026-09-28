package com.sostudio.apptemplate.service

import com.sostudio.apptemplate.data.prefs.PrefsHelper
import com.sostudio.apptemplate.data.repo.AuthRepo
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Mirrors iOS's `NotificationService` singleton: fetch/refresh the FCM token, and only upload
 * it to `POST /api/auth/deviceToken` while logged in — otherwise cache it in memory and flush
 * once login completes ([uploadLatestTokenIfNeeded]). No server-side token deletion on logout
 * either, matching iOS.
 */
class NotificationTokenManager(
    private val authRepo: AuthRepo,
    private val prefsHelper: PrefsHelper
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var latestToken: String? = null

    fun registerToken() {
        scope.launch {
            val token = runCatching { FirebaseMessaging.getInstance().token.await() }.getOrNull() ?: return@launch
            uploadOrCache(token)
        }
    }

    fun onNewToken(token: String) {
        scope.launch { uploadOrCache(token) }
    }

    fun uploadLatestTokenIfNeeded() {
        val token = latestToken ?: return
        scope.launch { uploadOrCache(token) }
    }

    private suspend fun uploadOrCache(token: String) {
        if (prefsHelper.isLoggedInFlow.value) {
            authRepo.deviceToken(token)
            latestToken = null
        } else {
            latestToken = token
        }
    }
}

private suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T =
    suspendCancellableCoroutine { continuation ->
        addOnCompleteListener { task ->
            val exception = task.exception
            when {
                exception != null -> continuation.resumeWithException(exception)
                task.isCanceled -> continuation.cancel()
                else -> continuation.resume(task.result)
            }
        }
    }
