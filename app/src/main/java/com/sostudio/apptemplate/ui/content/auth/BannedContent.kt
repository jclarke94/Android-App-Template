package com.sostudio.apptemplate.ui.content.auth

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.ui.components.buttons.PrimaryButton
import com.sostudio.apptemplate.ui.components.inputs.BasicLargeInput
import com.sostudio.apptemplate.ui.components.text.BodyText
import com.sostudio.apptemplate.ui.components.text.BoldCaptionText
import com.sostudio.apptemplate.ui.components.text.BoldBodyText
import com.sostudio.apptemplate.ui.components.text.TitleText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

// Ported from iOS's UI/Contents/Auth/BannedContent.swift.

@Composable
fun BannedContent(
    appealSubmitted: Boolean,
    canSubmit: Boolean,
    onAppealTextChanged: (String) -> Unit,
    onSubmitAppeal: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Icon(
            Icons.Filled.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TitleText(stringResource(R.string.banned_title), modifier = Modifier.padding(horizontal = 30.dp))

        Spacer(modifier = Modifier.height(12.dp))

        BodyText(
            stringResource(R.string.banned_message),
            color = MaterialTheme.colorScheme.secondary,
            align = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 30.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        if (appealSubmitted) {
            Icon(
                Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            BoldBodyText(stringResource(R.string.banned_appeal_success_title))
            Spacer(modifier = Modifier.height(4.dp))
            BodyText(
                stringResource(R.string.banned_appeal_success_message),
                color = MaterialTheme.colorScheme.secondary,
                align = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 30.dp)
            )
        } else {
            var appealText by remember { mutableStateOf("") }
            BasicLargeInput(
                value = appealText,
                onValueChange = {
                    appealText = it
                    onAppealTextChanged(it)
                },
                title = stringResource(R.string.banned_appeal_label),
                hint = stringResource(R.string.banned_appeal_hint),
                modifier = Modifier.padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = stringResource(R.string.banned_appeal_submit),
                enabled = canSubmit,
                onClick = onSubmitAppeal,
                modifier = Modifier.padding(horizontal = 30.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        TextButton(onClick = onLogout) {
            BoldCaptionText(stringResource(R.string.banned_logout), color = MaterialTheme.colorScheme.secondary)
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun BannedContentLightPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            BannedContent(appealSubmitted = false, canSubmit = false, onAppealTextChanged = {}, onSubmitAppeal = {}, onLogout = {})
        }
    }
}

@Preview(name = "Dark - Submitted", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun BannedContentSubmittedDarkPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            BannedContent(appealSubmitted = true, canSubmit = false, onAppealTextChanged = {}, onSubmitAppeal = {}, onLogout = {})
        }
    }
}
