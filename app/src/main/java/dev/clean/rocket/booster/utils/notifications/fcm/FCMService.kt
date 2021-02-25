package dev.clean.rocket.booster.utils.notifications.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.clean.rocket.booster.R
import dev.clean.rocket.booster.SplashActivity
import dev.clean.rocket.booster.utils.SubscriptionProvider
import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.notifications.fcm.FCMConfig.FCM_DATA
import dev.clean.rocket.booster.utils.notifications.fcm.FCMConfig.FCM_DATA_TAG
import java.util.*

class FCMService : FirebaseMessagingService() {

    val TAG_NUMBER_UI = "title"

    val TAG_NECESSITY_CHECK_TIME = "isNeedCheckTime"
    val NEED_CHECK = 1
    val NOT_NEED_CHECK = 0

    val TAG_CHOICE_HOUR = "choiceHour"


    var listLayouts = listOf(R.layout.a_notif_view, //0
            R.layout.b_notif_view, //1
            R.layout.c_notif_view, //2
            R.layout.d_notif_view, //3
            R.layout.e_notif_view, //4
            R.layout.f_notif_view, //5
            R.layout.g_notif_view, //6
            R.layout.h_notif_view, //7
            R.layout.sar_notif_a, //8
            R.layout.sar_notif_b, //9
            R.layout.sar_notif_c, //10
            R.layout.sar_notif_d, //11
            R.layout.sar_notif_e, //12
            R.layout.sar_notif_f //13
    )

    override fun onMessageReceived(p0: RemoteMessage) {
        if (!SubscriptionProvider.hasSubscription()) {
            showNotification(p0)
        }
    }

    private fun isNeedShow(p0: RemoteMessage): Boolean {
        return if (p0.data[TAG_NECESSITY_CHECK_TIME]!!.toInt() == NEED_CHECK){
            var currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            var choiceHour = p0.data[TAG_CHOICE_HOUR]!!.toInt()
            Log.e("LOL", "current -- $currentHour, choice -- $choiceHour")
            currentHour == choiceHour
        }else{
            true
        }
    }

    private fun showNotification(p0: RemoteMessage) {
        var intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(FCM_DATA_TAG, FCM_DATA)

        var index = p0.data[TAG_NUMBER_UI]!!.toInt()
        var layout = listLayouts[index]
        var pendingIntent = PendingIntent
                .getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        var collapsedView = RemoteViews(packageName, layout)
        //collapsedView.setTextViewText(R.id.tvNotificationTitle, p0.data["title"])

        var largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_small_notification)
        var notificationBuilder = NotificationCompat.Builder(this, "com.cleaner.booster.rocket")
                .setSmallIcon(R.drawable.ic_small_notification)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(0, 500))
                .setLights(Color.MAGENTA, 500, 1000)
                .setContentIntent(pendingIntent)
                .setCustomContentView(collapsedView)
        var notification = notificationBuilder.build()
        /*var notificationTarget = NotificationTarget(this,
                R.id.ivAvatarNotification, collapsedView,
                notification, 0)
        Handler(Looper.getMainLooper()).post(Runnable {
            Glide.with(MyApp.getInstance().applicationContext).asBitmap().load(p0.data["url"]).into(notificationTarget)
        })*/

        var notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("com.jundev.diets",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }*/

        Analytics.showFCMNotification()

        notificationManager.notify(0, notification)
    }


}