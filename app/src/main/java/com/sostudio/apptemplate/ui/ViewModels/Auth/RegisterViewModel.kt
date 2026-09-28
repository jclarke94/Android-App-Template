package com.sostudio.apptemplate.ui.ViewModels.Auth

import cafe.adriel.voyager.core.model.screenModelScope
import com.sostudio.apptemplate.data.prefs.PrefsHelper
import com.sostudio.apptemplate.data.remote.model.response.APIResult
import com.sostudio.apptemplate.data.repo.AuthRepo
import com.sostudio.apptemplate.ui.ViewModels.BaseViewModel
import com.sostudio.apptemplate.ui.ViewModels.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepo: AuthRepo,
    private val prefsHelper: PrefsHelper
) : BaseViewModel() {
    private val _navigateToProfileImage = MutableStateFlow(false)
    val navigateToProfileImage: StateFlow<Boolean> = _navigateToProfileImage.asStateFlow()

    /** Set on success, read once by the Screen to hand off to `RegisterProfileImageScreen`.
     *  Deliberately NOT persisted to `PrefsHelper` here — the token isn't stored (i.e. the user
     *  isn't considered logged in) until they skip or complete the avatar step, so abandoning
     *  registration mid-flow doesn't silently log them in. */
    var authToken: String = ""
        private set

    fun register(name: String, email: String, password: String, confirmPassword: String, ageConfirmed: Boolean) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            showAlert(message = "Please fill in every field.")
            return
        }
        if (password != confirmPassword) {
            showAlert(message = "Passwords do not match.")
            return
        }
        if (!ageConfirmed) {
            showAlert(message = "Please confirm you're 13 years of age or older.")
            return
        }
        screenModelScope.launch {
            setState(UIState.Loading)
            val inviteToken = prefsHelper.pendingInviteToken
            when (val result = authRepo.register(name, email, password, ageConfirmed, inviteToken)) {
                is APIResult.Success -> {
                    // Single-use from the client's perspective — the server has already read it.
                    prefsHelper.pendingInviteToken = null
                    authToken = result.data.token
                    setState(UIState.Loaded)
                    _navigateToProfileImage.value = true
                }

                is APIResult.Error -> {
                    setState(UIState.Loaded)
                    showAlert(message = result.message)
                }
            }
        }
    }
}
