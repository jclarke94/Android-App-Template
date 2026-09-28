package com.sostudio.apptemplate.service

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sostudio.apptemplate.R
import com.sostudio.apptemplate.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject

/**
 * Matches iOS's `NotificationService` foreground/background split: FCM auto-displays
 * notification-payload messages when the app is backgrounded/killed (system default channel
 * from the manifest meta-data), but only calls [onMessageReceived] while the process is alive,
 * at which point Android — unlike iOS — won't auto-show anything, so it's built manually here.
 * No tap-to-navigate — tapping just foregrounds the app.
 */
class AppTemplateFirebaseMessagingService : FirebaseMessagingService() {

    private val tokenManager: NotificationTokenManager by inject()

    override fun onNewToken(token: String) {
        tokenManager.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val notification = message.notification ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(this).notify(message.messageId?.hashCode() ?: System.currentTimeMillis().toInt(), builder.build())
    }

    companion object {
        const val DEFAULT_CHANNEL_ID = "apptemplate_default"
    }
}
