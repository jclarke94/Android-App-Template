package com.sostudio.apptemplate.nav.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sostudio.apptemplate.nav.BaseScreen
import com.sostudio.apptemplate.nav.rootNavigator
import com.sostudio.apptemplate.nav.screen.WelcomeScreen
import com.sostudio.apptemplate.ui.ViewModels.Settings.SettingsViewModel
import com.sostudio.apptemplate.ui.components.ScreenOverlay
import com.sostudio.apptemplate.ui.content.settings.SettingsContent

/**
 * Pushed onto the app's outer root Navigator (via `navigator.rootNavigator()`), mirroring iOS's
 * full-screen-cover `.show(.settings)`. A plain `navigator.parent` from inside a tab only
 * reaches `TabNavigator`'s internal Navigator, not this true root — see
 * [com.sostudio.apptemplate.nav.rootNavigator]. Nothing pushes this today (see README) — wire
 * it up from wherever you add an account/settings entry point.
 */
class SettingsScreen : BaseScreen() {

    @Composable
    override fun Content() {
        val viewModel: SettingsViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val loggedOut by viewModel.loggedOut.collectAsStateWithLifecycle()

        LaunchedEffect(loggedOut) {
            if (loggedOut) navigator.rootNavigator().replaceAll(WelcomeScreen())
        }

        ScreenOverlay(viewModel) {
            SettingsContent(onBack = { navigator.pop() }, onLogout = viewModel::logout)
        }
    }
}
