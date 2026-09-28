package com.sostudio.apptemplate.ui.components.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sostudio.apptemplate.data.prefs.TextSizeStage
import com.sostudio.apptemplate.ui.theme.LocalMessageFont
import com.sostudio.apptemplate.ui.theme.LocalMessageFontScale
import com.sostudio.apptemplate.ui.theme.LocalTextSizeStage
import com.sostudio.apptemplate.ui.theme.fontFamily
import com.sostudio.apptemplate.ui.theme.QuicksandFontFamily
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

// Ported from iOS's UI/Components/Text/TextOptions.swift — same names, sizes and weights, so a
// screen built against one platform's spec reads the same on the other.

@Composable
fun AppNameText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    weight: FontWeight = FontWeight.Bold,
    align: TextAlign = TextAlign.Start
) = Text(text, modifier = modifier, color = color, textAlign = align, style = TextStyle(fontFamily = QuicksandFontFamily, fontWeight = weight, fontSize = TextSizeStage.shiftedSp(44f, LocalTextSizeStage.current.steps).sp))

@Composable
fun HeadlineText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    weight: FontWeight = FontWeight.Bold,
    align: TextAlign = TextAlign.Start
) = Text(text, modifier = modifier, color = color, textAlign = align, style = TextStyle(fontFamily = QuicksandFontFamily, fontWeight = weight, fontSize = TextSizeStage.shiftedSp(35f, LocalTextSizeStage.current.steps).sp))

@Composable
fun SmallHeadlineText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    weight: FontWeight = FontWeight.Bold,
    align: TextAlign = TextAlign.Start
) = Text(text, modifier = modifier, color = color, textAlign = align, style = TextStyle(fontFamily = QuicksandFontFamily, fontWeight = weight, fontSize = TextSizeStage.shiftedSp(28f, LocalTextSizeStage.current.steps).sp))

@Composable
fun TitleText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    align: TextAlign = TextAlign.Start
) = Text(text, modifier = modifier, color = color, textAlign = align, style = TextStyle(fontFamily = QuicksandFontFamily, fontWeight = FontWeight.Bold, fontSize = TextSizeStage.shiftedSp(24f, LocalTextSizeStage.current.steps).sp))

@Composable
fun SubtitleText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    weight: FontWeight = FontWeight.SemiBold,
    align: TextAlign = TextAlign.Start
) = Text(text, modifier = modifier, color = color, textAlign = align, style = TextStyle(fontFamily = QuicksandFontFamily, fontWeight = weight, fontSize = TextSizeStage.shiftedSp(20f, LocalTextSizeStage.current.steps).sp))

@Composable
fun BoldBodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    weight: FontWeight = FontWeight.Bold,
    align: TextAlign = TextAlign.Start
) = Text(text, modifier = modifier, color = color, textAlign = align, style = TextStyle(fontFamily = QuicksandFontFamily, fontWeight = weight, fontSize = TextSizeStage.shiftedSp(16f, LocalTextSizeStage.current.steps).sp))

@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    weight: FontWeight = FontWeight.Medium,
    align: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) = Text(
    text,
    modifier = modifier,
    color = color,
    textAlign = align,
    maxLines = maxLines,
    overflow = overflow,
    style = TextStyle(fontFamily = QuicksandFontFamily, fontWeight = weight, fontSize = TextSizeStage.shiftedSp(16f, LocalTextSizeStage.current.steps).sp)
)

@Composable
fun BoldCaptionText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    weight: FontWeight = FontWeight.Bold,
    align: TextAlign = TextAlign.Start
) = Text(text, modifier = modifier, color = color, textAlign = align, style = TextStyle(fontFamily = QuicksandFontFamily, fontWeight = weight, fontSize = TextSizeStage.shiftedSp(12f, LocalTextSizeStage.current.steps).sp))

@Composable
fun CaptionText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    weight: FontWeight = FontWeight.Normal,
    align: TextAlign = TextAlign.Start
) = Text(text, modifier = modifier, color = color, textAlign = align, style = TextStyle(fontFamily = QuicksandFontFamily, fontWeight = weight, fontSize = TextSizeStage.shiftedSp(12f, LocalTextSizeStage.current.steps).sp))

@Composable
fun SmallText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    weight: FontWeight = FontWeight.Light,
    align: TextAlign = TextAlign.Start
) = Text(text, modifier = modifier, color = color, textAlign = align, style = TextStyle(fontFamily = QuicksandFontFamily, fontWeight = weight, fontSize = TextSizeStage.shiftedSp(10f, LocalTextSizeStage.current.steps).sp))

// Long-form/reader-style content follows the user's reading preference; decorative labels above
// keep the brand font.

@Composable
fun NoteMessageText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    align: TextAlign = TextAlign.Start
) = Text(text, modifier = modifier, color = color, textAlign = align, style = TextStyle(fontFamily = LocalMessageFont.current.fontFamily, fontSize = 20.sp * LocalMessageFontScale.current, lineHeight = 28.sp * LocalMessageFontScale.current))

@Composable
fun NotePreviewText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary,
    align: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) = Text(text, modifier = modifier, color = color, textAlign = align, maxLines = maxLines, overflow = overflow, style = TextStyle(fontFamily = LocalMessageFont.current.fontFamily, fontSize = 14.sp * LocalMessageFontScale.current, lineHeight = 22.sp * LocalMessageFontScale.current))

@Preview(name = "Light", showBackground = true)
@Composable
private fun TextStylesGalleryPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AppNameText("AppTemplate")
                HeadlineText("Headline Text")
                SmallHeadlineText("Small Headline Text")
                TitleText("Title Text")
                SubtitleText("Subtitle Text")
                BoldBodyText("Bold Body Text")
                BodyText("Body Text")
                BoldCaptionText("Bold Caption Text")
                CaptionText("Caption Text")
                SmallText("Small Text")
                NoteMessageText("Note Message Text")
                NotePreviewText("Note Preview Text")
            }
        }
    }
}
