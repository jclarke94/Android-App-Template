package com.sostudio.apptemplate.ui.ViewModels.Auth

import cafe.adriel.voyager.core.model.screenModelScope
import com.sostudio.apptemplate.data.db.AppDatabase
import com.sostudio.apptemplate.data.db.performLogout
import com.sostudio.apptemplate.data.prefs.PrefsHelper
import com.sostudio.apptemplate.data.remote.model.response.APIResult
import com.sostudio.apptemplate.data.repo.AuthRepo
import com.sostudio.apptemplate.ui.ViewModels.BaseViewModel
import com.sostudio.apptemplate.ui.ViewModels.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Ported from iOS's `BannedViewModel`. */
class BannedViewModel(
    private val authRepo: AuthRepo,
    private val prefsHelper: PrefsHelper,
    private val appDatabase: AppDatabase
) : BaseViewModel() {
    private val _appealText = MutableStateFlow("")
    val appealText: StateFlow<String> = _appealText.asStateFlow()

    private val _appealSubmitted = MutableStateFlow(false)
    val appealSubmitted: StateFlow<Boolean> = _appealSubmitted.asStateFlow()

    private val _loggedOut = MutableStateFlow(false)
    val loggedOut: StateFlow<Boolean> = _loggedOut.asStateFlow()

    val canSubmit: Boolean get() = _appealText.value.isNotBlank()

    fun onAppealTextChanged(text: String) {
        _appealText.value = text
    }

    fun submitAppeal() {
        if (!canSubmit) return
        screenModelScope.launch {
            setState(UIState.Loading)
            // Reuses the developer-feedback endpoint as a stand-in for a real appeals API —
            // swap this for your own endpoint once you have one.
            when (authRepo.developerFeedback(_appealText.value)) {
                is APIResult.Success -> {
                    setState(UIState.Loaded)
                    _appealSubmitted.value = true
                }

                is APIResult.Error -> {
                    setState(UIState.Loaded)
                    showError("Something went wrong submitting your appeal. Please try again.")
                }
            }
        }
    }

    fun logout() {
        screenModelScope.launch {
            performLogout(appDatabase, prefsHelper)
            _loggedOut.value = true
        }
    }
}
