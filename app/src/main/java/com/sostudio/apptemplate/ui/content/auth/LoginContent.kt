package com.sostudio.apptemplate.ui.content.auth

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.ui.components.buttons.PrimaryButton
import com.sostudio.apptemplate.ui.components.inputs.BasicInput
import com.sostudio.apptemplate.ui.components.inputs.PasswordBasicInput
import com.sostudio.apptemplate.ui.components.navigation.TopBanner
import com.sostudio.apptemplate.ui.components.text.BodyText
import com.sostudio.apptemplate.ui.components.text.HeadlineText
import com.sostudio.apptemplate.ui.components.text.SubtitleText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

// Ported from iOS's UI/Contents/Auth/LoginContent.swift. TopBanner's back arrow is the only
// way back — matches iOS, no separate "Back" button.

@Composable
fun LoginContent(
    onLogin: (login: String, password: String) -> Unit,
    onBack: () -> Unit,
    onForgotPassword: () -> Unit = {},
    onHelp: () -> Unit = {}
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        TopBanner(showBackButton = true, showLogo = true, showHelpButton = true, onBack = onBack, onAccountOrHelp = onHelp)

        HeadlineText(
            stringResource(R.string.login_title),
            modifier = Modifier.align(Alignment.CenterHorizontally).testTag("loginScreenRoot")
        )

        Spacer(modifier = Modifier.height(10.dp))

        SubtitleText(
            stringResource(R.string.login_subtitle),
            align = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        BasicInput(
            value = login,
            onValueChange = { login = it },
            title = stringResource(R.string.login_email),
            hint = stringResource(R.string.login_email_placeholder),
            keyboardType = KeyboardType.Email,
            modifier = Modifier.padding(horizontal = 20.dp),
            fieldTestTag = "loginEmailField"
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordBasicInput(
            value = password,
            onValueChange = { password = it },
            title = stringResource(R.string.login_password),
            modifier = Modifier.padding(horizontal = 20.dp),
            fieldTestTag = "loginPasswordField"
        )

        BodyText(
            stringResource(R.string.login_forgot_password),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
                .clickable(onClick = onForgotPassword)
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = stringResource(R.string.login_submit),
            onClick = { onLogin(login, password) },
            modifier = Modifier.padding(horizontal = 20.dp).testTag("loginSubmitButton")
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun LoginContentLightPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoginContent(onLogin = { _, _ -> }, onBack = {})
        }
    }
}

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun LoginContentDarkPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoginContent(onLogin = { _, _ -> }, onBack = {})
        }
    }
}
