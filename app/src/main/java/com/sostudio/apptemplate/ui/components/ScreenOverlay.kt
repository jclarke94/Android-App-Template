package com.sostudio.apptemplate.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sostudio.apptemplate.ui.ViewModels.BaseViewModel
import com.sostudio.apptemplate.ui.ViewModels.UIState
import com.sostudio.apptemplate.ui.components.dialogs.BannerContent
import com.sostudio.apptemplate.ui.components.dialogs.DismissableAlert
import com.sostudio.apptemplate.ui.components.loading.LoadingView

/**
 * Wraps a screen's Content with the [BaseViewModel] banner/alert/loading overlay every screen
 * gets for free — the Android analog of iOS's per-View `.overlay(banner)` +
 * `if case .loading = viewModel.state { LoadingView() }` pattern (see e.g. `LoginView.swift`).
 */
@Composable
fun ScreenOverlay(viewModel: BaseViewModel, content: @Composable BoxScope.() -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val banner by viewModel.banner.collectAsStateWithLifecycle()
    val alertMessage by viewModel.alertMessage.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        content()

        banner?.let {
            Box(modifier = Modifier.fillMaxSize().padding(bottom = 24.dp), contentAlignment = Alignment.BottomCenter) {
                BannerContent(text = it.text, type = it.type)
            }
        }

        alertMessage?.let {
            DismissableAlert(title = it.title, text = it.message, onDismiss = it.onDismiss)
        }

        if (state is UIState.Loading) {
            LoadingView()
        }
    }
}
