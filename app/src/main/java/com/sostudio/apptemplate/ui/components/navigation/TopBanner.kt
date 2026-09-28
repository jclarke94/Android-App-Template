package com.sostudio.apptemplate.ui.components.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.ui.components.text.HeadlineText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

// Ported from iOS's UI/Components/UI-Elements/TopBanner.swift. Icons stay laid out (alpha 0,
// not removed) when hidden so the centered title doesn't shift.

@Composable
fun TopBanner(
    modifier: Modifier = Modifier,
    title: String? = null,
    showBackButton: Boolean = false,
    showLogo: Boolean = false,
    showAccount: Boolean = false,
    showHelpButton: Boolean = false,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    onBack: () -> Unit = {},
    onAccountOrHelp: () -> Unit = {},
    titleTestTag: String? = null
) {
    Surface(modifier = modifier.fillMaxWidth().height(120.dp), color = backgroundColor) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().height(80.dp)
        ) {
            IconButton(
                onClick = { if (showBackButton) onBack() },
                modifier = Modifier.size(50.dp).alpha(if (showBackButton) 1f else 0f).testTag("topBannerBackButton")
            ) {
                Icon(painterResource(R.drawable.ic_left_arrow), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }

            HeadlineText(
                title ?: stringResource(R.string.app_name),
                modifier = Modifier.alpha(if (showLogo) 1f else 0f)
                    .let { if (titleTestTag != null) it.testTag(titleTestTag) else it }
            )

            // Only one of these is ever rendered at a time — stacking both IconButtons in the
            // same Box (even with alpha 0 on the unused one) would let the invisible one
            // intercept touches, since alpha doesn't disable hit-testing.
            Box(modifier = Modifier.size(50.dp)) {
                when {
                    showAccount -> IconButton(onClick = onAccountOrHelp, modifier = Modifier.size(50.dp).testTag("accountButton")) {
                        Icon(painterResource(R.drawable.ic_user), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                    showHelpButton -> IconButton(onClick = onAccountOrHelp, modifier = Modifier.size(50.dp)) {
                        Icon(painterResource(R.drawable.ic_question), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun TopBannerLightPreview() = TopBannerPreview()

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun TopBannerDarkPreview() = TopBannerPreview()

@Composable
private fun TopBannerPreview() {
    AppTemplateTheme {
        TopBanner(showBackButton = true, showLogo = true, showHelpButton = true)
    }
}
