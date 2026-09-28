package com.sostudio.apptemplate.util

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

/** In-app tab (not the system browser) for legal pages (FAQ/Privacy/Terms) served from your
 *  backend. Android equivalent of iOS's `SFSafariViewController` wrapper. */
fun Context.openCustomTab(url: String) {
    CustomTabsIntent.Builder().build().launchUrl(this, url.toUri())
}
