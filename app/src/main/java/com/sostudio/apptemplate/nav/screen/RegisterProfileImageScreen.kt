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
import com.sostudio.apptemplate.ui.ViewModels.Auth.RegisterProfileImageViewModel
import com.sostudio.apptemplate.ui.components.ScreenOverlay
import com.sostudio.apptemplate.ui.content.auth.RegisterProfileImageContent
import com.sostudio.apptemplate.util.openCustomTab

class RegisterProfileImageScreen(private val token: String) : BaseScreen() {

    @Composable
    override fun Content() {
        val viewModel: RegisterProfileImageViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val image by viewModel.image.collectAsStateWithLifecycle()
        val done by viewModel.done.collectAsStateWithLifecycle()

        LaunchedEffect(done) {
            if (done) navigator.replaceAll(MainTabScreen())
        }

        ScreenOverlay(viewModel) {
            RegisterProfileImageContent(
                image = image,
                onImagePicked = viewModel::setImage,
                onUpload = { viewModel.uploadImage(token) },
                onSkip = { viewModel.skip(token) },
                onBack = { navigator.pop() },
                onHelp = { context.openCustomTab(BuildConfig.API_HOST + "faq") }
            )
        }
    }
}
