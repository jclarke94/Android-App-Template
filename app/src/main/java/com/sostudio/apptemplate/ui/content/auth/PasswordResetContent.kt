package com.sostudio.apptemplate.ui.content.auth

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.ui.components.buttons.PrimaryButton
import com.sostudio.apptemplate.ui.components.buttons.SecondaryButton
import com.sostudio.apptemplate.ui.components.inputs.BasicInput
import com.sostudio.apptemplate.ui.components.navigation.TopBanner
import com.sostudio.apptemplate.ui.components.text.SubtitleText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

// Ported from iOS's UI/Contents/Auth/PasswordResetContent.swift.

@Composable
fun PasswordResetContent(
    emailSent: Boolean,
    onSendReset: (email: String) -> Unit,
    onBack: () -> Unit,
    onOpenEmail: () -> Unit,
    onHelp: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        TopBanner(showBackButton = true, showLogo = true, showHelpButton = true, onBack = onBack, onAccountOrHelp = onHelp)

        SubtitleText(
            stringResource(R.string.forgot_subtitle),
            align = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        BasicInput(
            value = email,
            onValueChange = { email = it },
            title = stringResource(R.string.login_email),
            hint = stringResource(R.string.login_email_placeholder),
            keyboardType = KeyboardType.Email,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        if (emailSent) {
            SecondaryButton(
                text = stringResource(R.string.forgot_open_email),
                onClick = onOpenEmail,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        PrimaryButton(
            text = stringResource(R.string.forgot_reset),
            onClick = { onSendReset(email) },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun PasswordResetContentLightPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PasswordResetContent(emailSent = false, onSendReset = {}, onBack = {}, onOpenEmail = {})
        }
    }
}

@Preview(name = "Dark - Email Sent", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PasswordResetContentSentDarkPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PasswordResetContent(emailSent = true, onSendReset = {}, onBack = {}, onOpenEmail = {})
        }
    }
}
