package com.sostudio.apptemplate.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.sostudio.apptemplate.BuildConfig
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.di.biometricModule
import com.sostudio.apptemplate.di.databaseModule
import com.sostudio.apptemplate.di.navModule
import com.sostudio.apptemplate.di.networkModule
import com.sostudio.apptemplate.di.notificationModule
import com.sostudio.apptemplate.di.prefsModule
import com.sostudio.apptemplate.di.repoModule
import com.sostudio.apptemplate.di.viewModelModule
import com.sostudio.apptemplate.service.AppTemplateFirebaseMessagingService
import io.sentry.android.core.SentryAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppTemplateApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initSentry()
        startKoin {
            androidContext(this@AppTemplateApp)
            modules(networkModule, prefsModule, repoModule, viewModelModule, databaseModule, notificationModule, biometricModule, navModule)
        }
        createNotificationChannel()
    }

    /**
     * Crash/error reporting. Manual init (auto-init disabled in the manifest) so the
     * environment/release come from BuildConfig. No-ops on a blank DSN — see the SENTRY_DSN
     * buildConfigField in app/build.gradle.kts. Errors only, no perf tracing, matching iOS.
     */
    private fun initSentry() {
        if (BuildConfig.SENTRY_DSN.isBlank()) return
        SentryAndroid.init(this) { options ->
            options.dsn = BuildConfig.SENTRY_DSN
            options.environment = if (BuildConfig.DEBUG) "debug" else "production"
            options.release = "apptemplate-android@${BuildConfig.VERSION_NAME}+${BuildConfig.VERSION_CODE}"
            options.isAttachStacktrace = true
            options.tracesSampleRate = 0.0
        }
    }

    // No SDK_INT guard needed — minSdk 30 is already above O (26), where channels became required.
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            AppTemplateFirebaseMessagingService.DEFAULT_CHANNEL_ID,
            getString(R.string.notification_channel_default_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = getString(R.string.notification_channel_default_description)
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
}
