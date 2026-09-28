package com.sostudio.apptemplate.nav.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.sostudio.apptemplate.nav.BaseScreen
import com.sostudio.apptemplate.nav.rootNavigator
import com.sostudio.apptemplate.ui.ViewModels.Auth.BannedViewModel
import com.sostudio.apptemplate.ui.components.ScreenOverlay
import com.sostudio.apptemplate.ui.content.auth.BannedContent

/** Ported from iOS's `BannedView`/`BannedContent` — a suspended-account screen with an appeal
 *  form (posts through the developer-feedback endpoint as a stand-in, swap for a real appeals
 *  API). Nothing pushes this today — wire it up wherever your app checks account standing
 *  (e.g. a `getUser().bannedAt != null` check on foreground/return-from-background). */
class BannedScreen : BaseScreen() {

    @Composable
    override fun Content() {
        val viewModel: BannedViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val appealSubmitted by viewModel.appealSubmitted.collectAsStateWithLifecycle()
        val appealText by viewModel.appealText.collectAsStateWithLifecycle()
        val loggedOut by viewModel.loggedOut.collectAsStateWithLifecycle()

        LaunchedEffect(loggedOut) {
            if (loggedOut) navigator.rootNavigator().replaceAll(WelcomeScreen())
        }

        ScreenOverlay(viewModel) {
            BannedContent(
                appealSubmitted = appealSubmitted,
                canSubmit = appealText.isNotBlank(),
                onAppealTextChanged = viewModel::onAppealTextChanged,
                onSubmitAppeal = viewModel::submitAppeal,
                onLogout = viewModel::logout
            )
        }
    }
}
