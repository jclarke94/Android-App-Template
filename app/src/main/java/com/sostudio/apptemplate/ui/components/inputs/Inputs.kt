package com.sostudio.apptemplate.ui.components.inputs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.ui.components.text.SubtitleText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme

// Ported from iOS's UI/Components/Text/InputOptions.swift — the "Basic" family is a filled
// rounded box (matches iOS's solid-background + shadow look), "Bordered" is an outline, which
// maps directly onto Compose's OutlinedTextField border support.

@Composable
fun BasicInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    hint: String = "",
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    keyboardType: KeyboardType = KeyboardType.Text,
    required: Boolean = false,
    icon: Painter? = null,
    onIconClick: (() -> Unit)? = null,
    fieldTestTag: String? = null
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        InputTitle(title, required, titleColor)
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(hint, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(10.dp),
            trailingIcon = icon?.let { painter -> { IconButton(onClick = { onIconClick?.invoke() }) { Icon(painter, contentDescription = null, tint = titleColor) } } },
            colors = filledColors(backgroundColor, titleColor),
            modifier = Modifier.fillMaxWidth().shadow(5.dp, RoundedCornerShape(10.dp))
                .let { if (fieldTestTag != null) it.testTag(fieldTestTag) else it }
        )
    }
}

/**
 * [characterLimit] mirrors iOS's `BasicLargeInput(characterLimit:)` — doesn't block typing past
 * the limit, just shows the remaining count (turning red once negative) and lets the caller
 * decide whether that disables submitting.
 */
@Composable
fun BasicLargeInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    hint: String = "",
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    required: Boolean = false,
    characterLimit: Int? = null
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        InputTitle(title, required, titleColor)
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(hint) },
            shape = RoundedCornerShape(10.dp),
            colors = filledColors(backgroundColor, titleColor),
            modifier = Modifier.fillMaxWidth().heightIn(min = 160.dp).shadow(5.dp, RoundedCornerShape(10.dp))
        )
        if (characterLimit != null) {
            val remaining = characterLimit - value.length
            Text(
                stringResource(R.string.generic_character_limit, remaining),
                color = if (remaining < 0) MaterialTheme.colorScheme.error else titleColor.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
    }
}

@Composable
fun BorderedInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    hint: String = "",
    borderColor: Color = MaterialTheme.colorScheme.tertiary,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    keyboardType: KeyboardType = KeyboardType.Text,
    required: Boolean = false
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        InputTitle(title, required, titleColor)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(hint) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(10.dp),
            colors = borderedColors(borderColor, titleColor),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PasswordBorderedInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    hint: String = "",
    borderColor: Color = MaterialTheme.colorScheme.tertiary,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    required: Boolean = false
) {
    var showPassword by remember { mutableStateOf(false) }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        InputTitle(title, required, titleColor)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(hint) },
            singleLine = true,
            visualTransformation = passwordTransformation(showPassword),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = { PasswordVisibilityToggle(showPassword, titleColor) { showPassword = !showPassword } },
            shape = RoundedCornerShape(10.dp),
            colors = borderedColors(borderColor, titleColor),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PasswordBasicInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    hint: String = "",
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    required: Boolean = false,
    fieldTestTag: String? = null
) {
    var showPassword by remember { mutableStateOf(false) }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        InputTitle(title, required, titleColor)
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(hint) },
            singleLine = true,
            visualTransformation = passwordTransformation(showPassword),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = { PasswordVisibilityToggle(showPassword, titleColor) { showPassword = !showPassword } },
            shape = RoundedCornerShape(10.dp),
            colors = filledColors(backgroundColor, titleColor),
            modifier = Modifier.fillMaxWidth().shadow(5.dp, RoundedCornerShape(10.dp))
                .let { if (fieldTestTag != null) it.testTag(fieldTestTag) else it }
        )
    }
}

@Composable
private fun InputTitle(title: String?, required: Boolean, titleColor: Color) {
    if (title == null) return
    Row(modifier = Modifier.padding(horizontal = 5.dp)) {
        SubtitleText(title, color = titleColor)
        if (required) SubtitleText(" *", color = titleColor)
    }
}

@Composable
private fun PasswordVisibilityToggle(showPassword: Boolean, tint: Color, onToggle: () -> Unit) {
    IconButton(onClick = onToggle) {
        Icon(
            painterResource(if (showPassword) R.drawable.ic_hide else R.drawable.ic_eye),
            contentDescription = null,
            tint = tint
        )
    }
}

private fun passwordTransformation(showPassword: Boolean): VisualTransformation =
    if (showPassword) VisualTransformation.None else PasswordVisualTransformation()

@Composable
private fun filledColors(backgroundColor: Color, textColor: Color) = TextFieldDefaults.colors(
    focusedContainerColor = backgroundColor,
    unfocusedContainerColor = backgroundColor,
    disabledContainerColor = backgroundColor,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    focusedTextColor = textColor,
    unfocusedTextColor = textColor
)

@Composable
private fun borderedColors(borderColor: Color, textColor: Color) = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.background,
    unfocusedContainerColor = MaterialTheme.colorScheme.background,
    focusedBorderColor = borderColor,
    unfocusedBorderColor = borderColor,
    focusedTextColor = textColor,
    unfocusedTextColor = textColor
)

@Preview(name = "Light", showBackground = true)
@Composable
private fun InputsGalleryLightPreview() = InputsGalleryPreview()

@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun InputsGalleryDarkPreview() = InputsGalleryPreview()

@Composable
private fun InputsGalleryPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                BasicInput(value = "", onValueChange = {}, title = "Basic Input", hint = "Enter text here")
                BasicInput(
                    value = "",
                    onValueChange = {},
                    title = "Input with Icon",
                    hint = "Enter text here",
                    icon = painterResource(R.drawable.ic_image)
                )
                BorderedInput(value = "", onValueChange = {}, title = "Bordered Input", hint = "Enter text here")
                PasswordBorderedInput(value = "", onValueChange = {}, title = "Password Bordered Input", hint = "Enter password here")
                PasswordBasicInput(value = "", onValueChange = {}, title = "Password Basic Input", hint = "Enter password here")
                BasicLargeInput(value = "", onValueChange = {}, hint = "Write something")
            }
        }
    }
}
