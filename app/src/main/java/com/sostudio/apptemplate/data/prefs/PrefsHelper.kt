package com.sostudio.apptemplate.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Encrypted-at-rest token storage — the Android analog of iOS's Valet-backed Keychain
 *  `AuthManager`. Everything session/device-preference related lives in this one file. */
class PrefsHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = run {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            "apptemplate_secret_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val _isLoggedInFlow = MutableStateFlow(readAuthToken() != null)
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedInFlow.asStateFlow()

    /** In-memory, non-persisted — set by the 401 auth-failure interceptor so WelcomeViewModel can
     *  show a one-time "session expired" banner. Read once and cleared on Welcome's init, not
     *  observed reactively. */
    var logoutReason: String? = null

    var authToken: String?
        get() = readAuthToken()
        set(value) {
            sharedPreferences.edit {
                if (value.isNullOrBlank()) remove(AUTH_TOKEN) else putString(AUTH_TOKEN, value)
            }
            _isLoggedInFlow.value = value != null
        }

    private fun readAuthToken(): String? = sharedPreferences.getString(AUTH_TOKEN, null)

    private val _themeModeFlow = MutableStateFlow(readThemeMode())
    val themeModeFlow: StateFlow<ThemeMode> = _themeModeFlow.asStateFlow()

    var themeMode: ThemeMode
        get() = readThemeMode()
        set(value) {
            sharedPreferences.edit { putString(THEME_MODE, value.name) }
            _themeModeFlow.value = value
        }

    private fun readThemeMode(): ThemeMode =
        sharedPreferences.getString(THEME_MODE, null)
            ?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() }
            ?: ThemeMode.AUTO

    private val _messageFontFlow = MutableStateFlow(readMessageFont())
    val messageFontFlow: StateFlow<MessageFont> = _messageFontFlow.asStateFlow()

    // Device-level reading preference, retained on logout like the theme.
    var messageFont: MessageFont
        get() = readMessageFont()
        set(value) {
            sharedPreferences.edit { putString(MESSAGE_FONT, value.name) }
            _messageFontFlow.value = value
        }

    private fun readMessageFont() = MessageFont.fromStored(sharedPreferences.getString(MESSAGE_FONT, null))

    private val _messageFontScaleFlow = MutableStateFlow(readMessageFontScale())
    val messageFontScaleFlow: StateFlow<Float> = _messageFontScaleFlow.asStateFlow()

    var messageFontScale: Float
        get() = readMessageFontScale()
        set(value) {
            sharedPreferences.edit { putFloat(MESSAGE_FONT_SCALE, value) }
            _messageFontScaleFlow.value = value
        }

    private fun readMessageFontScale() = sharedPreferences.getFloat(MESSAGE_FONT_SCALE, DEFAULT_MESSAGE_FONT_SCALE)

    private val _textSizeStageFlow = MutableStateFlow(readTextSizeStage())
    val textSizeStageFlow: StateFlow<TextSizeStage> = _textSizeStageFlow.asStateFlow()

    var textSizeStage: TextSizeStage
        get() = readTextSizeStage()
        set(value) {
            sharedPreferences.edit { putString(TEXT_SIZE_STAGE, value.name) }
            _textSizeStageFlow.value = value
        }

    private fun readTextSizeStage() = TextSizeStage.fromStored(sharedPreferences.getString(TEXT_SIZE_STAGE, null))

    /** Plain key-value cache, not Room — matches iOS's `NotificationSettingsStorageService`.
     *  Defaults match iOS's `@Published var ...Enabled: Bool = true`. */
    var notificationSettings: NotificationSettings
        get() = NotificationSettings(
            general = sharedPreferences.getBoolean(NOTIF_GENERAL, true),
            connections = sharedPreferences.getBoolean(NOTIF_CONNECTIONS, true),
            messages = sharedPreferences.getBoolean(NOTIF_MESSAGES, true)
        )
        set(value) {
            sharedPreferences.edit {
                putBoolean(NOTIF_GENERAL, value.general)
                putBoolean(NOTIF_CONNECTIONS, value.connections)
                putBoolean(NOTIF_MESSAGES, value.messages)
            }
        }

    /** Mirrors iOS's `BiometricService.isEnabled` — off by default, only flips on after a
     *  successful biometric prompt. */
    var biometricsEnabled: Boolean
        get() = sharedPreferences.getBoolean(BIOMETRICS_ENABLED, false)
        set(value) = sharedPreferences.edit { putBoolean(BIOMETRICS_ENABLED, value) }

    /** Captured from a deep link tapped before the user is registered, so it survives through
     *  Welcome -> Register. Single-use — cleared once submitted with a registration attempt. */
    var pendingInviteToken: String?
        get() = sharedPreferences.getString(PENDING_INVITE_TOKEN, null)
        set(value) {
            sharedPreferences.edit {
                if (value.isNullOrBlank()) remove(PENDING_INVITE_TOKEN) else putString(PENDING_INVITE_TOKEN, value)
            }
        }

    /**
     * Called from every logout path (sign-out, account deletion, biometric-lock log out, the
     * 401 auth-failure interceptor — always paired with [com.sostudio.apptemplate.data.db.performLogout]'s
     * `AppDatabase.clearAllTables()`) — clears the auth token and every per-account pref so the
     * next user on this device doesn't inherit the previous user's notification settings.
     * Deliberately keeps device-level prefs (`themeMode`, `messageFont`/`messageFontScale`,
     * `textSizeStage`, `biometricsEnabled`, a pre-registration `pendingInviteToken`). Add your
     * own per-feature local-cache clearing here as you build new features — this is the one
     * funnel every logout path is expected to go through.
     */
    fun onLogout() {
        authToken = null
        sharedPreferences.edit {
            remove(NOTIF_GENERAL)
            remove(NOTIF_CONNECTIONS)
            remove(NOTIF_MESSAGES)
        }
    }

    companion object {
        private const val AUTH_TOKEN = "authToken"
        private const val THEME_MODE = "themeMode"
        private const val MESSAGE_FONT = "message_font"
        private const val MESSAGE_FONT_SCALE = "message_font_scale"
        const val DEFAULT_MESSAGE_FONT_SCALE = 1.0f
        const val MIN_MESSAGE_FONT_SCALE = 0.85f
        const val MAX_MESSAGE_FONT_SCALE = 1.35f
        private const val TEXT_SIZE_STAGE = "text_size_stage"
        private const val NOTIF_GENERAL = "notif_general_enabled"
        private const val NOTIF_CONNECTIONS = "notif_connections_enabled"
        private const val NOTIF_MESSAGES = "notif_messages_enabled"
        private const val BIOMETRICS_ENABLED = "biometrics_enabled"
        private const val PENDING_INVITE_TOKEN = "pendingInviteToken"
    }
}

data class NotificationSettings(
    val general: Boolean = true,
    val connections: Boolean = true,
    val messages: Boolean = true
)
