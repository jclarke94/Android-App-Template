package com.sostudio.apptemplate.ui.ViewModels.Auth

import com.sostudio.apptemplate.data.prefs.PrefsHelper
import com.sostudio.apptemplate.nav.BaseScreenModel

class SplashViewModel(private val prefsHelper: PrefsHelper) : BaseScreenModel {
    val isLoggedIn: Boolean get() = prefsHelper.isLoggedInFlow.value
}
