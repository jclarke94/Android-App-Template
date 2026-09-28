package com.sostudio.apptemplate.ui.components.buttons

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.ui.components.text.BoldCaptionText
import com.sostudio.apptemplate.ui.components.text.SubtitleText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

private fun pillShape() = RoundedCornerShape(40.dp)

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter? = null,
    iconLeft: Boolean = false,
    height: Dp = 60.dp,
    onClick: () -> Unit
) {
    FilledPillButton(
        text = text,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        iconLeft = iconLeft,
        height = height,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledOverlayColor = MaterialTheme.colorScheme.tertiary,
        onClick = onClick
    )
}

@Composable
fun SecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter? = null,
    iconLeft: Boolean = false,
    height: Dp = 60.dp,
    onClick: () -> Unit
) {
    FilledPillButton(
        text = text,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        iconLeft = iconLeft,
        height = height,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledOverlayColor = MaterialTheme.colorScheme.tertiary,
        onClick = onClick
    )
}

@Composable
fun InvertedButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 60.dp,
    onClick: () -> Unit
) {
    FilledPillButton(
        text = text,
        modifier = modifier,
        enabled = enabled,
        icon = null,
        iconLeft = false,
        height = height,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        disabledOverlayColor = MaterialTheme.colorScheme.tertiary,
        onClick = onClick
    )
}

@Composable
private fun FilledPillButton(
    text: String,
    modifier: Modifier,
    enabled: Boolean,
    icon: Painter?,
    iconLeft: Boolean,
    height: Dp,
    containerColor: Color,
    contentColor: Color,
    disabledOverlayColor: Color,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Surface(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        modifier = modifier.fillMaxWidth().heightIn(min = height),
        enabled = enabled,
        shape = pillShape(),
        color = if (isPressed) containerColor.copy(alpha = 0.3f) else containerColor,
        shadowElevation = 4.dp,
        interactionSource = interactionSource
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null && iconLeft) {
                    Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.padding(end = 8.dp))
                }
                // Centred so a label that wraps to two lines reads evenly rather than left-ragged.
                SubtitleText(text, modifier = Modifier.weight(1f), color = contentColor, align = TextAlign.Center)
                if (icon != null && !iconLeft) {
                    Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.padding(start = 8.dp))
                }
            }
            if (!enabled) {
                Box(modifier = Modifier.matchParentSize().clip(pillShape()).background(disabledOverlayColor.copy(alpha = 0.5f)))
            }
        }
    }
}

@Composable
fun PrimaryIconButton(
    icon: Painter,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.size(size),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun SecondaryIconButton(
    icon: Painter,
    modifier: Modifier = Modifier,
    size: Dp = 60.dp,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.size(size),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun LargeIconButton(
    icon: Painter,
    text: String,
    modifier: Modifier = Modifier,
    size: Dp = 90.dp,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.size(size),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 20.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            BoldCaptionText(text, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun ButtonsGalleryLightPreview() = ButtonsGalleryPreview()

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ButtonsGalleryDarkPreview() = ButtonsGalleryPreview()

@Composable
private fun ButtonsGalleryPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PrimaryButton(text = "Primary Button", onClick = {})
                PrimaryButton(text = "Disabled", enabled = false, onClick = {})
                SecondaryButton(text = "Secondary Button", onClick = {})
                Surface(color = MaterialTheme.colorScheme.primary) {
                    InvertedButton(text = "Inverted Button", modifier = Modifier.padding(12.dp), onClick = {})
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PrimaryIconButton(icon = painterResource(R.drawable.ic_mail), onClick = {})
                    SecondaryIconButton(icon = painterResource(R.drawable.ic_mail), onClick = {})
                    LargeIconButton(icon = painterResource(R.drawable.ic_image), text = "Add Image", onClick = {})
                }
            }
        }
    }
}
