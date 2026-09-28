package com.sostudio.apptemplate.ui.components.biometric

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.ui.components.text.BodyText
import com.sostudio.apptemplate.ui.components.text.TitleText

/**
 * Full-screen cover blocking all content until authenticated — matches iOS's `BiometricLockView`.
 * Auto-fires the biometric prompt immediately on appearing, no manual "Unlock" tap needed for
 * the first attempt, same as iOS. A plain spinner stands in for the first attempt while no
 * Lottie asset is bundled (see README).
 */
@Composable
fun BiometricLockScreen(onAuthenticated: () -> Unit, onLogout: () -> Unit, modifier: Modifier = Modifier) {
    var showRetry by remember { mutableStateOf(false) }
    val biometricAuth = rememberBiometricAuth(onResult = { success ->
        if (success) onAuthenticated() else showRetry = true
    })

    LaunchedEffect(Unit) {
        biometricAuth.authenticate()
    }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showRetry) {
                Icon(
                    Icons.Filled.Fingerprint,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.height(16.dp))
                TitleText(
                    text = stringResource(R.string.biometric_lock_tap_to_retry),
                    modifier = Modifier.clickable {
                        showRetry = false
                        biometricAuth.authenticate()
                    }
                )
            } else {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(40.dp))
            TextButton(onClick = onLogout) {
                BodyText(stringResource(R.string.biometric_lock_log_out), color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}
