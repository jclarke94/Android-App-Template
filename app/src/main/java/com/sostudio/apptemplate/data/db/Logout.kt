package com.sostudio.apptemplate.data.db

import com.sostudio.apptemplate.data.prefs.PrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * The one funnel every logout path (sign-out, account deletion, biometric-lock log out, the
 * 401 auth-failure interceptor) must call — wipes Room and the per-account prefs together so
 * no path can forget one half. Always hops onto [Dispatchers.IO]: `clearAllTables()` is a
 * blocking Room call, and this also lets the 401 interceptor fire it without blocking OkHttp's
 * dispatcher thread. Add your own per-feature local-cache clearing here as you build new
 * features that cache per-account state.
 */
suspend fun performLogout(appDatabase: AppDatabase, prefsHelper: PrefsHelper) {
    withContext(Dispatchers.IO) {
        appDatabase.clearAllTables()
        prefsHelper.onLogout()
    }
}
