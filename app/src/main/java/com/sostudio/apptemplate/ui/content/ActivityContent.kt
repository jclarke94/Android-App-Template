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
import com.sostudio.apptemplate.ui.components.text.BodyText
import com.sostudio.apptemplate.ui.components.text.TitleText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

/** Ported from iOS's `ActivityContent.swift` — the template's second tab, an empty-state
 *  placeholder to build a real feed/list on top of. */
@Composable
fun ActivityContent() {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(top = 80.dp, start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText(stringResource(R.string.activity_title))
        Spacer(modifier = Modifier.height(8.dp))
        BodyText(stringResource(R.string.activity_empty), color = MaterialTheme.colorScheme.secondary, align = TextAlign.Center)
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun ActivityContentLightPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ActivityContent()
        }
    }
}

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ActivityContentDarkPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ActivityContent()
        }
    }
}
