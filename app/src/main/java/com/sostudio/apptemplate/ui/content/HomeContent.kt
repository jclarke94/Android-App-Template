package com.sostudio.apptemplate.ui.content

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.data.remote.model.response.UserResponse
import com.sostudio.apptemplate.ui.components.buttons.PrimaryButton
import com.sostudio.apptemplate.ui.components.text.BodyText
import com.sostudio.apptemplate.ui.components.text.TitleText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

/**
 * Ported from iOS's `HomeContent.swift` — the smallest example of the View -> ViewModel ->
 * Content pattern, alongside Settings. [onOpenDetail] demonstrates a pushed destination
 * (see `HomeScreen.kt`'s nested Navigator) — replace with your own screens.
 */
@Composable
fun HomeContent(user: UserResponse?, onOpenDetail: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(top = 80.dp, start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText(stringResource(R.string.home_title))

        if (user != null) {
            Spacer(modifier = Modifier.height(8.dp))
            BodyText("Signed in as ${user.name}", color = MaterialTheme.colorScheme.secondary, align = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(text = stringResource(R.string.home_open_detail), onClick = onOpenDetail)
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun HomeContentLightPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeContent(user = UserResponse.dummy(), onOpenDetail = {})
        }
    }
}

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun HomeContentDarkPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeContent(user = UserResponse.dummy(), onOpenDetail = {})
        }
    }
}
