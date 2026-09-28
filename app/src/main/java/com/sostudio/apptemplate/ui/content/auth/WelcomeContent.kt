package com.sostudio.apptemplate.ui.content.auth

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.ui.components.buttons.PrimaryButton
import com.sostudio.apptemplate.ui.components.buttons.SecondaryButton
import com.sostudio.apptemplate.ui.components.text.BodyText
import com.sostudio.apptemplate.ui.components.text.CaptionText
import com.sostudio.apptemplate.ui.components.text.SmallHeadlineText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

// Ported from iOS's UI/Contents/Auth/WelcomeContent.swift — a placeholder hero icon in place of
// a real illustration; swap for your own art.

@Composable
fun WelcomeContent(onLogin: () -> Unit, onRegister: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 60.dp).size(120.dp)
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SmallHeadlineText(
                    stringResource(R.string.welcome_title),
                    align = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                BodyText(
                    stringResource(R.string.welcome_desc),
                    weight = FontWeight.SemiBold,
                    align = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                PrimaryButton(
                    text = stringResource(R.string.welcome_register),
                    onClick = onRegister,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(5.dp))

                SecondaryButton(
                    text = stringResource(R.string.welcome_login),
                    onClick = onLogin,
                    modifier = Modifier.padding(horizontal = 16.dp).testTag("welcomeLoginButton")
                )

                Spacer(modifier = Modifier.height(5.dp))

                CaptionText(
                    stringResource(R.string.welcome_privacy),
                    color = MaterialTheme.colorScheme.secondary,
                    align = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun WelcomeContentLightPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            WelcomeContent(onLogin = {}, onRegister = {})
        }
    }
}

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun WelcomeContentDarkPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            WelcomeContent(onLogin = {}, onRegister = {})
        }
    }
}
