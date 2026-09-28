package com.sostudio.apptemplate.ui.ViewModels.Auth

import cafe.adriel.voyager.core.model.screenModelScope
import com.sostudio.apptemplate.data.remote.model.response.APIResult
import com.sostudio.apptemplate.data.repo.AuthRepo
import com.sostudio.apptemplate.ui.ViewModels.BaseViewModel
import com.sostudio.apptemplate.ui.ViewModels.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PasswordResetViewModel(
    private val authRepo: AuthRepo
) : BaseViewModel() {
    /** Separate from [state] because a successful send reveals the "Open Email" button without
     *  navigating away — the user stays on this screen. */
    private val _emailSent = MutableStateFlow(false)
    val emailSent: StateFlow<Boolean> = _emailSent.asStateFlow()

    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            showAlert(message = "Please enter your email address.")
            return
        }
        screenModelScope.launch {
            setState(UIState.Loading)
            when (val result = authRepo.forgotPassword(email)) {
                is APIResult.Success -> {
                    setState(UIState.Loaded)
                    _emailSent.value = true
                }

                is APIResult.Error -> {
                    setState(UIState.Loaded)
                    showAlert(message = result.message)
                }
            }
        }
    }
}
