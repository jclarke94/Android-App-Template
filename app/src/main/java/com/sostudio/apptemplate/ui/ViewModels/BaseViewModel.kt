package com.sostudio.apptemplate.ui.ViewModels

import cafe.adriel.voyager.core.model.screenModelScope
import com.sostudio.apptemplate.nav.BaseScreenModel
import com.sostudio.apptemplate.ui.components.dialogs.BannerType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UIState {
    data object Loading : UIState()
    data object Loaded : UIState()
    data class Error(val message: String? = null) : UIState()
}

data class Banner(val text: String, val type: BannerType)

data class AlertMessage(val title: String, val message: String, val onDismiss: () -> Unit)

/**
 * Android analog of the iOS template's `BaseViewModel` protocol — every screen model gets
 * loading/loaded/error state plus a self-dismissing banner and a blocking alert for free,
 * instead of hand-rolling its own ad hoc UI-state type per screen.
 */
abstract class BaseViewModel : BaseScreenModel {
    private val _state = MutableStateFlow<UIState>(UIState.Loaded)
    val state: StateFlow<UIState> = _state.asStateFlow()

    private val _banner = MutableStateFlow<Banner?>(null)
    val banner: StateFlow<Banner?> = _banner.asStateFlow()

    private val _alertMessage = MutableStateFlow<AlertMessage?>(null)
    val alertMessage: StateFlow<AlertMessage?> = _alertMessage.asStateFlow()

    protected fun setState(state: UIState) {
        _state.value = state
    }

    protected fun show(text: String, type: BannerType = BannerType.Success) {
        _banner.value = Banner(text, type)
        screenModelScope.launch {
            delay(5000)
            _banner.value = null
        }
    }

    fun dismissBanner() {
        _banner.value = null
    }

    protected fun showAlert(title: String = "Error", message: String, onDismiss: () -> Unit = {}) {
        _alertMessage.value = AlertMessage(title, message) {
            _alertMessage.value = null
            onDismiss()
        }
    }

    fun hideAlert() {
        _alertMessage.value = null
    }

    /** Shows an error message as a banner — the common case for a failed API call. */
    protected fun showError(message: String) {
        show(message, BannerType.Error)
    }
}
