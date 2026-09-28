package com.sostudio.apptemplate.ui.ViewModels.Settings

import cafe.adriel.voyager.core.model.screenModelScope
import com.sostudio.apptemplate.data.db.AppDatabase
import com.sostudio.apptemplate.data.db.performLogout
import com.sostudio.apptemplate.data.prefs.PrefsHelper
import com.sostudio.apptemplate.ui.ViewModels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Smallest example of the View -> ViewModel -> Content pattern alongside Home — add your own
 *  account-management screens here as you build them out. */
class SettingsViewModel(
    private val prefsHelper: PrefsHelper,
    private val appDatabase: AppDatabase
) : BaseViewModel() {
    private val _loggedOut = MutableStateFlow(false)
    val loggedOut: StateFlow<Boolean> = _loggedOut.asStateFlow()

    fun logout() {
        screenModelScope.launch {
            performLogout(appDatabase, prefsHelper)
            _loggedOut.value = true
        }
    }
}
