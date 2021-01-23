package dev.clean.rocket.booster.AAA

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import dev.clean.rocket.booster.R

import dev.clean.rocket.booster.MainActivity

import android.content.Context.NOTIFICATION_SERVICE

/**
 * Created by intag pc on 10/5/2017.
 */

class AlarmBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

            //Toast.makeText(context, "HGello", Toast.LENGTH_SHORT).show();

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
            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager


            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP




            notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
            notificationManager.notify(0, notification)


            //            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
            //            Intent notificationIntent = new Intent(
            //                    context.getApplicationContext(), MainActivity.class);
            //            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            //            stackBuilder.addParentStack(MainActivity.class);
            //            stackBuilder.addNextIntent(notificationIntent);
            //            PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            //            Notification notification = new Notification.Builder(context)
            //                    .setContentTitle("Title")
            //                    .setContentText("Detail about it")
            //                    .setDefaults(Notification.DEFAULT_SOUND
            //                            | Notification.DEFAULT_VIBRATE)
            //                    .setContentIntent(pIntent).setAutoCancel(true)
            //                    .setSmallIcon(R.mipmap.ic_launcher).build();
            //            notificationManager.notify(0, notification);
        }
    }
}
