package com.sostudio.apptemplate.ui.content.auth

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
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
import com.sostudio.apptemplate.ui.components.inputs.BasicInput
import com.sostudio.apptemplate.ui.components.inputs.PasswordBasicInput
import com.sostudio.apptemplate.ui.components.navigation.TopBanner
import com.sostudio.apptemplate.ui.components.text.BodyText
import com.sostudio.apptemplate.ui.components.text.HeadlineText
import com.sostudio.apptemplate.ui.components.text.SubtitleText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

// Ported from iOS's UI/Contents/Auth/RegisterContent.swift. iOS splits First/Last Name as
// separate fields (not one combined Name field) though the API still only takes a single
// `name` string, so the two are joined client-side before calling onRegister.

@Composable
fun RegisterContent(
    onRegister: (name: String, email: String, password: String, confirmPassword: String, ageConfirmed: Boolean) -> Unit,
    onBack: () -> Unit,
    onLoginInstead: () -> Unit,
    onHelp: () -> Unit = {}
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var ageConfirmed by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        TopBanner(showBackButton = true, showLogo = true, showHelpButton = true, onBack = onBack, onAccountOrHelp = onHelp)

        HeadlineText(stringResource(R.string.register_title), modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(4.dp))

        SubtitleText(
            stringResource(R.string.register_subtitle),
            align = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(horizontal = 20.dp)) {
            BasicInput(
                value = firstName,
                onValueChange = { firstName = it },
                title = stringResource(R.string.register_first_name),
                modifier = Modifier.weight(1f)
            )
            BasicInput(
                value = lastName,
                onValueChange = { lastName = it },
                title = stringResource(R.string.register_last_name),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        BasicInput(
            value = email,
            onValueChange = { email = it },
            title = stringResource(R.string.register_email),
            hint = stringResource(R.string.login_email_placeholder),
            keyboardType = KeyboardType.Email,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp).clickable { ageConfirmed = !ageConfirmed }
        ) {
            Checkbox(checked = ageConfirmed, onCheckedChange = { ageConfirmed = it })
            BodyText(stringResource(R.string.register_age_confirm))
        }

        Spacer(modifier = Modifier.height(12.dp))

        PasswordBasicInput(
            value = password,
            onValueChange = { password = it },
            title = stringResource(R.string.register_password),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordBasicInput(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            title = stringResource(R.string.register_password_confirm),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            text = stringResource(R.string.register_submit),
            onClick = { onRegister("$firstName $lastName".trim(), email, password, confirmPassword, ageConfirmed) },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        BodyText(
            stringResource(R.string.register_login_instead),
            modifier = Modifier.align(Alignment.CenterHorizontally).clickable(onClick = onLoginInstead)
        )

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun RegisterContentLightPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RegisterContent(onRegister = { _, _, _, _, _ -> }, onBack = {}, onLoginInstead = {})
        }
    }
}

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun RegisterContentDarkPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RegisterContent(onRegister = { _, _, _, _, _ -> }, onBack = {}, onLoginInstead = {})
        }
    }
}
