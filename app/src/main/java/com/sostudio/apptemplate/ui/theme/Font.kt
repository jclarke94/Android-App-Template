package com.sostudio.apptemplate.ui.theme

import androidx.compose.ui.text.font.FontFamily

// No real font files are bundled here (licensing) — see README. Both fall back to the system
// font until you add your own `.ttf`/`.otf` files under res/font/ and wire them in here the
// same way, e.g.:
//   val QuicksandFontFamily = FontFamily(
//       Font(R.font.quicksand_regular, FontWeight.Normal),
//       Font(R.font.quicksand_bold, FontWeight.Bold)
//   )
// Nothing crashes in the meantime — Compose just renders with FontFamily.Default.
val QuicksandFontFamily = FontFamily.Default
