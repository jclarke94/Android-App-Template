package com.sostudio.apptemplate.data.prefs

// App-wide UI text size (Settings, buttons, captions, etc.) — separate from MessageFont's
// per-message scale. Shifts every ui/components/text/Text.kt style N steps up a fixed size
// ladder shared by both hierarchies (e.g. LARGE moves Caption up to Body's size). Mirrors
// iOS's `TextSizeStage`.
enum class TextSizeStage(val steps: Int, val label: String) {
    NORMAL(0, "Normal"), LARGE(1, "Large"), EXTRA_LARGE(2, "Extra large");

    companion object {
        fun fromStored(value: String?): TextSizeStage = entries.find { it.name == value } ?: NORMAL

        private val sizeLadder = listOf(10f, 12f, 16f, 20f, 24f, 28f, 35f, 44f)

        fun shiftedSp(base: Float, steps: Int): Float {
            val index = sizeLadder.indexOf(base)
            if (index == -1) return base
            return sizeLadder[minOf(index + steps, sizeLadder.lastIndex)]
        }
    }
}
