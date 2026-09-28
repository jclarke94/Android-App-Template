package com.sostudio.apptemplate.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.annotation.WorkerThread
import com.sostudio.apptemplate.data.remote.RetrofitClient
import okhttp3.ResponseBody
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/** Shared by the runtime-permission and biometric-prompt hooks — both need the hosting Activity,
 *  not just a themed Context, to launch a system permission/biometric dialog. */
tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

inline fun <C : CharSequence> C?.ifNullOrBlank(defaultValue: () -> C): C =
    if (isNullOrBlank()) defaultValue() else this

fun <C : CharSequence> firstNotNullOrBlank(vararg options: () -> C?, fallback: () -> C): C {
    for (option in options) {
        val value = try {
            option()
        } catch (e: Exception) {
            Log.d("firstNotNullOrBlank", "Exception thrown in option", e)
            null
        }
        if (!value.isNullOrBlank()) {
            return value
        }
    }
    return fallback()
}

@WorkerThread
inline fun <reified T> ResponseBody?.parseBody(): T? = this?.let {
    val source = source()
    source.request(Long.MAX_VALUE) // Buffer the entire body.
    val buffer = source.buffer
    val contentType = contentType()
    val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
    val stringBody = buffer.clone().readString(charset)
    runCatching { RetrofitClient.json.decodeFromString<T>(stringBody) }.getOrNull()
}
