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

class LoginViewModel(
    private val authRepo: AuthRepo,
    private val prefsHelper: PrefsHelper
) : BaseViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun login(login: String, password: String) {
        if (login.isBlank() || password.isBlank()) {
            showAlert(message = "Please enter your email or username and password.")
            return
        }
        screenModelScope.launch {
            setState(UIState.Loading)
            when (val result = authRepo.login(login, password)) {
                is APIResult.Success -> {
                    prefsHelper.authToken = result.data.token
                    setState(UIState.Loaded)
                    _isLoggedIn.value = true
                }

                is APIResult.Error -> {
                    setState(UIState.Loaded)
                    showAlert(message = result.message)
                }
            }
        }
    }
}
