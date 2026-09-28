package com.sostudio.apptemplate.ui.content.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.ui.components.buttons.SecondaryButton
import com.sostudio.apptemplate.ui.components.navigation.TopBanner
import com.sostudio.apptemplate.ui.components.text.TitleText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

/** Smallest example of the pattern, alongside Home — add your own account-management rows
 *  here as you build them out (edit profile, notifications, plan, etc). */
@Composable
fun SettingsContent(onBack: () -> Unit, onLogout: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        TopBanner(showBackButton = true, showLogo = true, onBack = onBack)

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TitleText(stringResource(R.string.settings_title))
            SecondaryButton(text = stringResource(R.string.settings_logout), onClick = onLogout, modifier = Modifier.padding(top = 20.dp))
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun SettingsContentLightPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SettingsContent(onBack = {}, onLogout = {})
        }
    }
}

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SettingsContentDarkPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SettingsContent(onBack = {}, onLogout = {})
        }
    }
}
