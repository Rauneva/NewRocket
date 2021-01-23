package dev.clean.rocket.booster.utils.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build

import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import dev.clean.rocket.booster.R
import dev.clean.rocket.booster.SplashActivity
import dev.clean.rocket.booster.utils.PreferencesProvider

class AlarmReceiver : BroadcastReceiver() {

    var listLayouts = listOf(R.layout.b_notif_view, R.layout.a_notif_view,
            R.layout.g_notif_view, R.layout.h_notif_view, R.layout.c_notif_view,
            R.layout.f_notif_view, R.layout.e_notif_view, R.layout.d_notif_view)

    override fun onReceive(context: Context, intent: Intent) {
        var version = getVersion()
        var collapsedView = RemoteViews(context!!.packageName, listLayouts[version])

        val notificationIntent = Intent(context, SplashActivity::class.java)

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID).setCustomBigContentView(collapsedView)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = builder
                .setContentTitle(PreferencesProvider.getInstance().getString("state_Head", "Error"))
                .setContentText(PreferencesProvider.getInstance().getString("state_Body", "Increase device performance"))
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent).build()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID)
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    CHANNEL_ID,
                    context.resources.getString(R.string.app_name),
                    IMPORTANCE_DEFAULT
            )
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            channel.setSound(alarmSound, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }


        notificationManager.notify(0, notification)

    }


    private fun getVersion(): Int {
        var version = PreferencesProvider.getVerNotif()!!
        var newVersion = version + 1
        if (newVersion >= listLayouts.size){
            newVersion = 0
        }
        PreferencesProvider.setVerNotif(newVersion)
        return version
    }

    companion object {

        private val CHANNEL_ID = "com.singhajit.notificationDemo.channelId"
    }

}
