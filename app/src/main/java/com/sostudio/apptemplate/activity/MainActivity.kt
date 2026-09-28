package com.sostudio.apptemplate.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.sostudio.apptemplate.data.prefs.PrefsHelper
import com.sostudio.apptemplate.nav.screen.SplashScreen
import com.sostudio.apptemplate.nav.screen.WelcomeScreen
import com.sostudio.apptemplate.service.BiometricLockManager
import com.sostudio.apptemplate.service.NotificationTokenManager
import com.sostudio.apptemplate.ui.components.biometric.BiometricLockScreen
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme
import kotlinx.coroutines.flow.drop
import org.koin.android.ext.android.inject

// FragmentActivity (not plain ComponentActivity) is required by androidx.biometric.BiometricPrompt,
// which only accepts a FragmentActivity/Fragment host.
class MainActivity : FragmentActivity() {

    private val prefsHelper: PrefsHelper by inject()
    private val notificationTokenManager: NotificationTokenManager by inject()
    private val biometricLockManager: BiometricLockManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Takes over the OS-mandated Android 12+ Starting Window (Theme.App.Starting in the
        // manifest) — with no keep-on-screen condition set, it dismisses as soon as this
        // Activity's first frame is drawn, handing off to SplashScreen.kt's own Lottie animation
        // as fast as possible instead of lingering as a second, separate-feeling splash.
        installSplashScreen()
        super.onCreate(savedInstanceState)
        handleDeepLink(intent)
        enableEdgeToEdge()
        setContent {
            val themeMode by prefsHelper.themeModeFlow.collectAsStateWithLifecycle()
            val messageFont by prefsHelper.messageFontFlow.collectAsStateWithLifecycle()
            val messageFontScale by prefsHelper.messageFontScaleFlow.collectAsStateWithLifecycle()
            val textSizeStage by prefsHelper.textSizeStageFlow.collectAsStateWithLifecycle()
            val isLoggedIn by prefsHelper.isLoggedInFlow.collectAsStateWithLifecycle()
            val isLocked by biometricLockManager.isLocked.collectAsStateWithLifecycle()

            // Mirrors iOS's `uploadLatestTokenIfNeeded()` call once `isLoggedIn` flips true, to
            // flush a token that was fetched (and cached) before the user was authenticated.
            LaunchedEffect(isLoggedIn) {
                if (isLoggedIn) notificationTokenManager.uploadLatestTokenIfNeeded()
            }

            AppTemplateTheme(
                themeMode = themeMode,
                messageFont = messageFont,
                messageFontScale = messageFontScale,
                textSizeStage = textSizeStage
            ) {
                // Screens don't paint their own background, so without this the static XML
                // windowBackground (light/dark only via system config, not our ThemeMode
                // override) would show through instead of the resolved MaterialTheme color.
                Surface(
                    modifier = Modifier.fillMaxSize().semantics { testTagsAsResourceId = true },
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Navigator(SplashScreen()) { navigator ->
                            CurrentScreen()

                            // `drop(1)` skips the flow's initial value so this never fires on
                            // cold start (SplashScreen already owns that decision) — it only
                            // reacts to a real true->false transition, e.g. the auth interceptor
                            // logging out an expired session mid-use from RetrofitClient, where
                            // no screen-level code is otherwise around to redirect.
                            LaunchedEffect(Unit) {
                                prefsHelper.isLoggedInFlow.drop(1).collect { loggedIn ->
                                    if (!loggedIn) navigator.replaceAll(WelcomeScreen())
                                }
                            }

                            // Full-screen cover on top of whatever's current, matching iOS's
                            // blocking BiometricLockView — Log Out routes back to Welcome the
                            // same way Settings' sign-out does, via the root Navigator.
                            if (isLocked) {
                                BiometricLockScreen(
                                    onAuthenticated = biometricLockManager::unlock,
                                    onLogout = {
                                        biometricLockManager.logout()
                                        navigator.replaceAll(WelcomeScreen())
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // launchMode="singleTop" routes a warm-launch tap on a deep link here instead of a fresh
    // onCreate — without overriding this, that second tap would be silently ignored.
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleDeepLink(intent)
    }

    /**
     * Example deep-link handler for `apptemplate://...` links — the Android analog of iOS's
     * `AppCoordinator.handle(deepLink:)`. Rename the scheme to your own product's (see README
     * and AndroidManifest.xml), and add your own hosts here — e.g. a pending-invite token
     * captured before the user is registered, stashed via [PrefsHelper.pendingInviteToken] for
     * [com.sostudio.apptemplate.ui.ViewModels.Auth.RegisterViewModel] to submit alongside the
     * new account, the same shape as a real invite/referral link would need.
     */
    private fun handleDeepLink(intent: Intent?) {
        val uri = intent?.data ?: return
        if (uri.scheme != "apptemplate") return
        val token = uri.getQueryParameter("token")?.takeIf { it.isNotBlank() } ?: return
        if (!prefsHelper.isLoggedInFlow.value) {
            prefsHelper.pendingInviteToken = token
        }
    }
}
