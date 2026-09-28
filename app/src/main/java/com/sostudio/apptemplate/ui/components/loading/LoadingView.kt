package com.sostudio.apptemplate.ui.components.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sostudio.apptemplate.ui.components.text.BodyText
import com.sostudio.apptemplate.ui.theme.AppTemplateTheme
import kotlinx.coroutines.delay

/**
 * Ported from iOS's `LoadingView.swift` — a looping Lottie animation inside a translucent
 * circular card, with an animated "Loading..." dot-cycle underneath, over a dimmed full-screen
 * background. No animation file is bundled (see README's Animations section) — loads by
 * filename from `assets/`, which resolves at runtime rather than compile time, so a missing
 * file renders nothing instead of failing the build (Compose's LottieCompositionSpec.RawRes
 * would require the resource to exist at compile time, unlike this).
 *
 * The real `LottieAnimation` is skipped under `LocalInspectionMode` — its infinite
 * `IterateForever` composition crashes Compose Preview's sandboxed renderer.
 */
@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    var dotCount by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            dotCount = dotCount % 4 + 1
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
            contentAlignment = Alignment.Center
        ) {
            if (LocalInspectionMode.current) {
                Box(modifier = Modifier.size(300.dp))
            } else {
                val composition by rememberLottieComposition(LottieCompositionSpec.Asset("apptemplate_logo_animation.json"))
                val progress by animateLottieCompositionAsState(
                    composition,
                    iterations = LottieConstants.IterateForever
                )
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(300.dp)
                )
            }

            BodyText(
                text = "Loading" + ".".repeat(dotCount),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 160.dp)
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun LoadingViewPreview() {
    AppTemplateTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoadingView()
        }
    }
}
