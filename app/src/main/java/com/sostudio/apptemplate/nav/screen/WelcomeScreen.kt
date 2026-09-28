package com.sostudio.apptemplate.nav.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sostudio.apptemplate.nav.BaseScreen
import com.sostudio.apptemplate.ui.ViewModels.Auth.WelcomeViewModel
import com.sostudio.apptemplate.ui.components.ScreenOverlay
import com.sostudio.apptemplate.ui.content.auth.WelcomeContent

class WelcomeScreen : BaseScreen() {

    @Composable
    override fun Content() {
        val viewModel: WelcomeViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow

        ScreenOverlay(viewModel) {
            WelcomeContent(
                onLogin = { navigator.push(LoginScreen()) },
                onRegister = { navigator.push(RegisterScreen()) }
            )
        }
    }
}
