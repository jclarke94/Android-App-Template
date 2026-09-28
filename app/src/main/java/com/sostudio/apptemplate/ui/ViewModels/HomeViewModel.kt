package com.sostudio.apptemplate.ui.ViewModels

import cafe.adriel.voyager.core.model.screenModelScope
import com.sostudio.apptemplate.data.remote.model.response.UserResponse
import com.sostudio.apptemplate.data.repo.AuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val authRepo: AuthRepo) : BaseViewModel() {
    private val _user = MutableStateFlow<UserResponse?>(null)
    val user: StateFlow<UserResponse?> = _user.asStateFlow()

    fun loadUser() {
        screenModelScope.launch {
            _user.value = authRepo.getCachedUser()
        }
    }
}
