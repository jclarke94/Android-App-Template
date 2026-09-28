package com.sostudio.apptemplate.ui.components.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.ui.components.text.BodyText
import com.sostudio.apptemplate.ui.components.text.HeadlineText
import com.sostudio.apptemplate.ui.theme.FailureRed
import com.sostudio.apptemplate.ui.theme.SuccessGreen
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme
import com.sostudio.apptemplate.ui.theme.WarningYellow

// Ported from iOS's UI/Components/Dialogs/BannerType.swift + BannerContent.swift. Success/
// Warning/Error colors are non-adaptive (same value in light and dark), so BannerType uses the
// raw SuccessGreen/WarningYellow/FailureRed constants directly rather than theme-derived colors.

enum class BannerType(val icon: ImageVector, val backgroundColor: Color) {
    Success(Icons.Filled.CheckCircle, SuccessGreen),
    Warning(Icons.Filled.Warning, WarningYellow),
    Error(Icons.Filled.Error, FailureRed)
}

@Composable
fun BannerContent(text: String, type: BannerType, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(10.dp),
        color = type.backgroundColor,
        shadowElevation = 10.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Icon(type.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Text(text, color = Color.White, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun DismissableAlert(
    title: String,
    text: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
            onClick = onDismiss
        ) {}

        Surface(
            modifier = Modifier.align(Alignment.Center).padding(horizontal = 40.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = 10.dp
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                HeadlineText(title)
                BodyText(text)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.generic_close), color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun BannerGalleryLightPreview() = BannerGalleryPreview()

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun BannerGalleryDarkPreview() = BannerGalleryPreview()

@Composable
private fun BannerGalleryPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                BannerContent(text = "Hello, World!", type = BannerType.Success)
                BannerContent(text = "Hello, World!", type = BannerType.Warning)
                BannerContent(text = "Hello, World!", type = BannerType.Error)
            }
        }
    }
}

@Preview(name = "Dismissable Alert", showBackground = true)
@Composable
private fun DismissableAlertPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DismissableAlert(title = "Error", text = "Test alert", onDismiss = {})
        }
    }
}
