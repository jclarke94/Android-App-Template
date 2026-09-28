package com.sostudio.apptemplate.service

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.sostudio.apptemplate.data.db.AppDatabase
import com.sostudio.apptemplate.data.db.performLogout
import com.sostudio.apptemplate.data.prefs.PrefsHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Mirrors iOS's `BiometricService` trigger logic, but the two platforms' lock-trigger
 * *mechanism* differs: iOS wires a cold-start `.task` plus an `onChange(of: scenePhase)`
 * observer separately; [ProcessLifecycleOwner.onStart] unifies both into one callback — it
 * fires once at genuine app launch and again on every return from the background.
 *
 * [expectTrustedReturn] exists because Android, unlike iOS, launches Photo Picker/Camera/Custom
 * Tabs as separate-process Activities that trigger a real onStop/onStart cycle — without this,
 * picking an avatar photo or opening a legal page would spuriously re-lock the app, which iOS
 * never does (those are in-app sheets there). Callers must invoke it immediately before
 * launching one of those trusted external Activities; it's consumed by the very next [onStart].
 */
class BiometricLockManager(
    private val prefsHelper: PrefsHelper,
    private val appDatabase: AppDatabase
) : DefaultLifecycleObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val _isLocked = MutableStateFlow(false)
    val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    private var suppressNextRelock = false

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        if (suppressNextRelock) {
            suppressNextRelock = false
            return
        }
        if (prefsHelper.isLoggedInFlow.value && prefsHelper.biometricsEnabled) {
            _isLocked.value = true
        }
    }

    fun expectTrustedReturn() {
        suppressNextRelock = true
    }

    fun unlock() {
        _isLocked.value = false
    }

    /** Matches iOS's `logout()` on a failed/cancelled unlock — clears Room too (unlike a
     *  token-only logout), same central funnel as [performLogout]'s other call sites. */
    fun logout() {
        _isLocked.value = false
        scope.launch { performLogout(appDatabase, prefsHelper) }
    }
}
