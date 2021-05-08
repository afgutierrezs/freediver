package com.android.freediver.util.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.android.freediver.R
import com.android.freediver.ui.co2table.CO2TableActivity


// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0


/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity

    val contentIntent = Intent(applicationContext, CO2TableActivity::class.java).apply {
        flags =  FLAG_ACTIVITY_REORDER_TO_FRONT
    }

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val freediverNotificationImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.freediver_notification
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(freediverNotificationImage)
        .bigLargeIcon(null)

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.co2_table_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_lungs_shape)
        .setContentTitle(
            applicationContext
                .getString(R.string.co2_notification_title)
        )
        .setContentText(messageBody)

        .setContentIntent(contentPendingIntent)
        .setAutoCancel(false)
        .setOngoing(true)

        .setStyle(bigPicStyle)
        .setLargeIcon(freediverNotificationImage)

        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}