package com.sostudio.apptemplate.nav.screen

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sostudio.apptemplate.BuildConfig
import com.sostudio.apptemplate.nav.BaseScreen
import com.sostudio.apptemplate.ui.ViewModels.Auth.PasswordResetViewModel
import com.sostudio.apptemplate.ui.components.ScreenOverlay
import com.sostudio.apptemplate.ui.content.auth.PasswordResetContent
import com.sostudio.apptemplate.util.openCustomTab

class PasswordResetScreen : BaseScreen() {

    @Composable
    override fun Content() {
        val viewModel: PasswordResetViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val emailSent by viewModel.emailSent.collectAsStateWithLifecycle()

        ScreenOverlay(viewModel) {
            PasswordResetContent(
                emailSent = emailSent,
                onSendReset = viewModel::sendPasswordReset,
                onBack = { navigator.pop() },
                onOpenEmail = {
                    // Android's equivalent of iOS's per-scheme mail-app probing —
                    // CATEGORY_APP_EMAIL asks the system for whatever mail app is installed
                    // rather than guessing specific package/scheme names.
                    val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_EMAIL)
                    runCatching { context.startActivity(intent) }
                },
                onHelp = { context.openCustomTab(BuildConfig.API_HOST + "faq") }
            )
        }
    }
}
