package com.sostudio.apptemplate.ui.components.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sostudio.apptemplate.util.findActivity

/** Shared dangerous-permission request flow — callers decide when to call [RuntimePermission.request]
 *  rather than requesting the moment a screen loads. */
enum class RuntimePermissionState { Granted, NotGranted, PermanentlyDenied }

class RuntimePermission internal constructor(
    val state: RuntimePermissionState,
    val request: () -> Unit,
    val openSettings: () -> Unit
)

@Composable
fun rememberRuntimePermission(permission: String): RuntimePermission {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    var state by remember {
        mutableStateOf(if (context.hasPermission(permission)) RuntimePermissionState.Granted else RuntimePermissionState.NotGranted)
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        state = when {
            granted -> RuntimePermissionState.Granted
            activity != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> RuntimePermissionState.NotGranted
            else -> RuntimePermissionState.PermanentlyDenied
        }
    }

    return remember(state) {
        RuntimePermission(
            state = state,
            request = { launcher.launch(permission) },
            openSettings = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
                context.startActivity(intent)
            }
        )
    }
}

private fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
