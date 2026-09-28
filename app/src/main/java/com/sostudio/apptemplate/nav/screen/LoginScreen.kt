package com.sostudio.apptemplate.nav.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sostudio.apptemplate.BuildConfig
import com.sostudio.apptemplate.nav.BaseScreen
import com.sostudio.apptemplate.ui.ViewModels.Auth.LoginViewModel
import com.sostudio.apptemplate.ui.components.ScreenOverlay
import com.sostudio.apptemplate.ui.content.auth.LoginContent
import com.sostudio.apptemplate.util.openCustomTab

class LoginScreen : BaseScreen() {

    @Composable
    override fun Content() {
        val viewModel: LoginViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

        LaunchedEffect(isLoggedIn) {
            if (isLoggedIn) navigator.replaceAll(MainTabScreen())
        }

        ScreenOverlay(viewModel) {
            LoginContent(
                onLogin = viewModel::login,
                onBack = { navigator.pop() },
                onForgotPassword = { navigator.push(PasswordResetScreen()) },
                onHelp = { context.openCustomTab(BuildConfig.API_HOST + "faq") }
            )
        }
    }
}
