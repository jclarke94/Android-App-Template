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
import com.sostudio.apptemplate.ui.ViewModels.Auth.RegisterViewModel
import com.sostudio.apptemplate.ui.components.ScreenOverlay
import com.sostudio.apptemplate.ui.content.auth.RegisterContent
import com.sostudio.apptemplate.util.openCustomTab

class RegisterScreen : BaseScreen() {

    @Composable
    override fun Content() {
        val viewModel: RegisterViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val navigateToProfileImage by viewModel.navigateToProfileImage.collectAsStateWithLifecycle()

        LaunchedEffect(navigateToProfileImage) {
            if (navigateToProfileImage) navigator.push(RegisterProfileImageScreen(viewModel.authToken))
        }

        ScreenOverlay(viewModel) {
            RegisterContent(
                onRegister = viewModel::register,
                onBack = { navigator.pop() },
                onLoginInstead = { navigator.push(LoginScreen()) },
                onHelp = { context.openCustomTab(BuildConfig.API_HOST + "faq") }
            )
        }
    }
}
