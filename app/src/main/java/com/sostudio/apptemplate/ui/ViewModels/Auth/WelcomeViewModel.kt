package com.sostudio.apptemplate.ui.ViewModels.Auth

import com.sostudio.apptemplate.data.prefs.PrefsHelper
import com.sostudio.apptemplate.ui.ViewModels.BaseViewModel
import com.sostudio.apptemplate.ui.components.dialogs.BannerType

/**
 * Mirrors iOS's `WelcomeViewModel.checkForLogoutReason()` — reads [PrefsHelper.logoutReason]
 * once on init (Welcome is pushed fresh via `replaceAll` on every logout, so init-time is
 * equivalent to iOS's `.onAppear`) and clears it immediately so it never resurfaces on a later
 * visit to this screen.
 */
class WelcomeViewModel(
    prefsHelper: PrefsHelper
) : BaseViewModel() {
    init {
        prefsHelper.logoutReason?.let { reason ->
            prefsHelper.logoutReason = null
            show(reason, BannerType.Error)
        }
    }
}
