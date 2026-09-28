package com.sostudio.apptemplate.ui.components.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.util.findActivity

/**
 * Wraps `androidx.biometric.BiometricPrompt` — the direct Android analog of iOS's `LAContext`.
 * A single prompt configured with `BIOMETRIC_STRONG or DEVICE_CREDENTIAL` is used everywhere:
 * Android's system dialog already offers a "use PIN" option natively as part of one interaction,
 * so there's no need to manage a retry-count-gated fallback the way iOS's two-stage
 * biometric-then-passcode escalation does.
 */
class BiometricAuthHandle internal constructor(
    val canUseBiometrics: Boolean,
    val authenticate: () -> Unit
)

@Composable
fun rememberBiometricAuth(onResult: (success: Boolean) -> Unit): BiometricAuthHandle {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() as? FragmentActivity }
    // BiometricManager.from(context) calls getSystemService(BIOMETRIC_SERVICE), which Compose
    // Preview's layoutlib Context doesn't implement — skip it there rather than crashing every
    // preview that reaches this composable.
    val isPreview = LocalInspectionMode.current
    val canUseBiometrics = remember(isPreview) {
        !isPreview && BiometricManager.from(context).canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }
    val currentOnResult = rememberUpdatedState(onResult)

    val prompt = remember(activity) {
        activity?.let { fragmentActivity ->
            BiometricPrompt(
                fragmentActivity,
                ContextCompat.getMainExecutor(context),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        currentOnResult.value(true)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        currentOnResult.value(false)
                    }
                    // onAuthenticationFailed (a single wrong fingerprint/face) is recoverable —
                    // the system dialog stays open and retries on its own, no action needed here.
                }
            )
        }
    }

    val promptTitle = stringResource(R.string.biometric_prompt_title)
    val promptInfo = remember(promptTitle) {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(promptTitle)
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()
    }

    return remember(prompt, canUseBiometrics) {
        BiometricAuthHandle(
            canUseBiometrics = canUseBiometrics,
            authenticate = { prompt?.authenticate(promptInfo) }
        )
    }
}
