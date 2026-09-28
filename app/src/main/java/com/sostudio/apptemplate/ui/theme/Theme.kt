package com.sostudio.apptemplate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.sostudio.apptemplate.data.prefs.MessageFont
import com.sostudio.apptemplate.data.prefs.PrefsHelper
import com.sostudio.apptemplate.data.prefs.ThemeMode
import com.sostudio.apptemplate.data.prefs.TextSizeStage

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = BackgroundLight,
    background = BackgroundLight,
    onBackground = PrimaryLight,
    surface = HighlightLight,
    onSurface = PrimaryLight,
    secondary = AccentLight,
    onSecondary = BackgroundLight,
    tertiary = HighlightLight,
    onTertiary = PrimaryLight,
    error = FailureRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = PrimaryLight,
    background = BackgroundDark,
    onBackground = PrimaryDark,
    surface = HighlightDark,
    onSurface = PrimaryDark,
    secondary = AccentDark,
    onSecondary = PrimaryLight,
    tertiary = HighlightDark,
    onTertiary = PrimaryDark,
    error = FailureRed,
    onError = Color.White
)

// No real font files bundled (see Font.kt) — every MessageFont maps to the system font for now.
val MessageFont.fontFamily: FontFamily
    get() = FontFamily.Default

val LocalMessageFont = staticCompositionLocalOf { MessageFont.HANDLEE }
val LocalMessageFontScale = staticCompositionLocalOf { PrefsHelper.DEFAULT_MESSAGE_FONT_SCALE }
val LocalTextSizeStage = staticCompositionLocalOf { TextSizeStage.NORMAL }
val LocalIsDarkTheme = staticCompositionLocalOf { false }

@Composable
fun AppTemplateTheme(
    themeMode: ThemeMode = ThemeMode.AUTO,
    messageFont: MessageFont = MessageFont.HANDLEE,
    messageFontScale: Float = PrefsHelper.DEFAULT_MESSAGE_FONT_SCALE,
    textSizeStage: TextSizeStage = TextSizeStage.NORMAL,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.AUTO -> isSystemInDarkTheme()
    }

    CompositionLocalProvider(
        LocalIsDarkTheme provides darkTheme,
        LocalMessageFont provides messageFont,
        LocalMessageFontScale provides messageFontScale,
        LocalTextSizeStage provides textSizeStage
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            typography = Typography,
            content = content
        )
    }
}
