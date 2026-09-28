package com.sostudio.apptemplate.ui.components.media

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.service.BiometricLockManager
import com.sostudio.apptemplate.ui.components.permission.RuntimePermissionState
import com.sostudio.apptemplate.ui.components.permission.rememberRuntimePermission
import org.koin.compose.koinInject

/**
 * Photo Library / Take Photo chooser. Modern Photo Picker (`PickVisualMedia`, no storage
 * permission) + `TakePicturePreview` — matches iOS's `PhotosPicker`/`UIImagePickerController`
 * behind a `confirmationDialog`.
 *
 * `TakePicturePreview` normally delegates to the system Camera app without needing our own
 * `CAMERA` permission, BUT once an app holds that permission at all (declared in the manifest
 * for other features), Android requires it to be *granted* before it will start any camera
 * intent, including this implicit one — so this needs the same request-on-demand flow as any
 * other CAMERA-gated feature.
 */
class PhotoPickerHandle internal constructor(val show: () -> Unit)

@Composable
fun rememberPhotoPicker(onImagePicked: (Bitmap) -> Unit): PhotoPickerHandle {
    val context = LocalContext.current
    // koinInject needs the real Application's startKoin() call, which Compose Preview's
    // sandboxed renderer never runs — skip it there rather than crashing every preview that
    // reaches this composable.
    val biometricLockManager = if (LocalInspectionMode.current) null else koinInject<BiometricLockManager>()
    var showOptions by remember { mutableStateOf(false) }
    val cameraPermission = rememberRuntimePermission(Manifest.permission.CAMERA)
    var pendingCameraLaunch by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let(onImagePicked)
    }

    LaunchedEffect(cameraPermission.state) {
        if (pendingCameraLaunch && cameraPermission.state == RuntimePermissionState.Granted) {
            pendingCameraLaunch = false
            cameraLauncher.launch(null)
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        onImagePicked(bitmap)
    }

    if (showOptions) {
        AlertDialog(
            onDismissRequest = { showOptions = false },
            title = { Text(stringResource(R.string.profile_image_choose_photo)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(onClick = {
                        showOptions = false
                        // Photo Picker is a separate-process Activity — suppress the spurious
                        // re-lock that would otherwise fire on return.
                        biometricLockManager?.expectTrustedReturn()
                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }) {
                        Text(stringResource(R.string.profile_image_photo_library))
                    }
                    TextButton(onClick = {
                        showOptions = false
                        biometricLockManager?.expectTrustedReturn()
                        when (cameraPermission.state) {
                            RuntimePermissionState.Granted -> cameraLauncher.launch(null)
                            RuntimePermissionState.NotGranted -> {
                                pendingCameraLaunch = true
                                cameraPermission.request()
                            }
                            RuntimePermissionState.PermanentlyDenied -> cameraPermission.openSettings()
                        }
                    }) {
                        Text(stringResource(R.string.profile_image_take_photo))
                    }
                }
            },
            confirmButton = {}
        )
    }

    return remember { PhotoPickerHandle(show = { showOptions = true }) }
}
