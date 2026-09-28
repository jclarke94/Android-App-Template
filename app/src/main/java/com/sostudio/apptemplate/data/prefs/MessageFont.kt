package com.sostudio.apptemplate.data.prefs

/** Device-local reader-style font preference for long-form content — mirrors iOS's `MessageFont`. */
enum class MessageFont(val label: String) {
    HANDLEE("Handlee"), PATRICK_HAND("Patrick Hand"), CAVEAT("Caveat"), QUICKSAND("Quicksand");

    companion object {
        fun fromStored(value: String?): MessageFont = entries.find { it.name == value } ?: HANDLEE
    }
}
