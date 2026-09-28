package com.sostudio.apptemplate.nav.screen

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.nav.BaseScreen
import com.sostudio.apptemplate.service.NotificationTokenManager
import com.sostudio.apptemplate.ui.ViewModels.Auth.SplashViewModel
import com.sostudio.apptemplate.ui.components.permission.rememberRuntimePermission
import com.sostudio.apptemplate.ui.components.text.AppNameText
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

/** Ported from iOS's `SplashView` — a looping Lottie animation over the brand background, with
 *  the app name underneath. See README's Animations section for the (intentionally unbundled)
 *  Lottie asset. */
class SplashScreen : BaseScreen() {

    @Composable
    override fun Content() {
        val viewModel: SplashViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow

        // Requesting the POST_NOTIFICATIONS system dialog immediately on cold start clashes with
        // Android 12+'s Starting Window/SplashScreen handoff — deferring both calls until after
        // the splash delay avoids a visible double-flash.
        val notificationTokenManager = if (LocalInspectionMode.current) null else koinInject<NotificationTokenManager>()
        val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberRuntimePermission(Manifest.permission.POST_NOTIFICATIONS)
        } else null

        LaunchedEffect(Unit) {
            delay(2000)
            notificationPermission?.request()
            notificationTokenManager?.registerToken()
            navigator.replaceAll(if (viewModel.isLoggedIn) MainTabScreen() else WelcomeScreen())
        }

        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val composition by rememberLottieComposition(LottieCompositionSpec.Asset("apptemplate_logo_animation.json"))
                val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
                LottieAnimation(composition = composition, progress = { progress }, modifier = Modifier.size(300.dp))

                AppNameText(stringResource(R.string.app_name))
            }
        }
    }
}
