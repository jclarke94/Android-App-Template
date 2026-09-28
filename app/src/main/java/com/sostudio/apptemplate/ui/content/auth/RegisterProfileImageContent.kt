package com.sostudio.apptemplate.ui.content.auth

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.ui.components.buttons.PrimaryButton
import com.sostudio.apptemplate.ui.components.buttons.SecondaryButton
import com.sostudio.apptemplate.ui.components.media.rememberPhotoPicker
import com.sostudio.apptemplate.ui.components.navigation.TopBanner
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

// Ported from iOS's UI/Contents/Auth/RegisterProfileImageContent.swift.

@Composable
fun RegisterProfileImageContent(
    image: Bitmap?,
    onImagePicked: (Bitmap) -> Unit,
    onUpload: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit,
    onHelp: () -> Unit = {}
) {
    val photoPicker = rememberPhotoPicker(onImagePicked)

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        TopBanner(showBackButton = true, showLogo = true, showHelpButton = true, onBack = onBack, onAccountOrHelp = onHelp)

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(280.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                .clickable { photoPicker.show() },
            contentAlignment = Alignment.Center
        ) {
            if (image != null) {
                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(Icons.Filled.CameraAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            text = stringResource(R.string.profile_image_upload_continue),
            enabled = image != null,
            onClick = onUpload,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        SecondaryButton(
            text = stringResource(R.string.profile_image_skip),
            onClick = onSkip,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun RegisterProfileImageContentLightPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RegisterProfileImageContent(image = null, onImagePicked = {}, onUpload = {}, onSkip = {}, onBack = {})
        }
    }
}

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun RegisterProfileImageContentDarkPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RegisterProfileImageContent(image = null, onImagePicked = {}, onUpload = {}, onSkip = {}, onBack = {})
        }
    }
}
