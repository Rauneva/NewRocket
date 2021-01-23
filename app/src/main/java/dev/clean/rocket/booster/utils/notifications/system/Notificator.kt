package dev.clean.rocket.booster.utils.notifications.system

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dev.clean.rocket.booster.MainActivity
import dev.clean.rocket.booster.R

object Notificator {

    fun show(context: Context, intent: Intent) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        val intentt = PendingIntent.getActivity(context, 0,
                notificationIntent, 0)
        val notification = Notification.Builder(context)
                .setContentTitle(context.resources.getString(R.string.title_notefication))
                .setContentText(context.resources.getString(R.string.detail_notification))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(intentt).setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
                .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP




        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        notificationManager.notify(0, notification)
    }
}