package dev.mem.rocket.sanya.utils.notifications.interactive

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import dev.mem.rocket.sanya.MyApp
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.SplashActivity
import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.PreferencesProvider
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import dev.mem.rocket.sanya.utils.analytic.ErrorInterceptor
import dev.mem.rocket.sanya.utils.notifications.interactive.brs.*
import java.lang.Exception
import java.util.concurrent.TimeUnit


object CleanNotification {

    private val CHANNEL_ID = "com.wsoteam.mydietcoach.channelIdReact.udo"

    private const val MAX_BATTERY_LEVEL = 101
    private const val HIGH_BATTERY_LEVEL = 30
    private const val MEDIUM_BATTERY_LEVEL = 20
    private const val LOW_BATTERY_LEVEL = 10
    private const val EMPTY_BATTERY_LEVEL = 5

    private const val HIGH_BATTERY_TAG = "HIGH_BATTERY_TAG"
    private const val MEDIUM_BATTERY_TAG = "MEDIUM_BATTERY_TAG"
    private const val LOW_BATTERY_TAG = "LOW_BATTERY_TAG"
    private const val EMPTY_BATTERY_TAG = "EMPTY_BATTERY_TAG"

    private const val TYPE_BATTERY = "TYPE_BATTERY"
    private const val TYPE_STORAGE = "TYPE_STORAGE"
    private const val TYPE_INSTALL = "TYPE_INSTALL"
    private const val TYPE_DELETE = "TYPE_DELETE"

    private const val LOOPER_TIMEOUT = 10L

    private const val LOWER_MEMORY_LIMIT = 3072
    private const val HIGH_MEMORY_LIMIT = 3584

    var batteryLayoutList = listOf(R.layout.bat_notif_a, R.layout.bat_notif_b, R.layout.bat_notif_c)
    var storageLayoutList = listOf(R.layout.storage_notif_a, R.layout.storage_notif_b, R.layout.storage_notif_c)
    var installLayoutList = listOf(R.layout.package_notif_a, R.layout.package_notif_b, R.layout.package_notif_c)
    var deleteLayoutList = listOf(R.layout.delete_notif_a, R.layout.delete_notif_b, R.layout.delete_notif_c)
    var iconsList = listOf(R.drawable.ic_notif_battery, R.drawable.ic_notif_storage, R.drawable.ic_notif_install, R.drawable.ic_notif_delete)
    var channels = listOf("com.wsoteam.cleaner.bat", "com.wsoteam.cleaner.storage", "com.wsoteam.cleaner.install", "com.wsoteam.cleaner.delete")


    private fun getPendingIntent(intent: Intent): PendingIntent {
        return PendingIntent
                .getBroadcast(MyApp.getInstance(), 0,
                        intent, 0)
    }

    fun newCreate(): Notification {
        var collapsedView = RemoteViews(MyApp.getInstance().packageName, R.layout.view_widget)

        val boostIntent = Intent(MyApp.getInstance(), BoostBR::class.java)
        val batteryIntent = Intent(MyApp.getInstance(), BatteryBR::class.java)
        val tempIntent = Intent(MyApp.getInstance(), TemperatureBR::class.java)
        val clearIntent = Intent(MyApp.getInstance(), ClearBR::class.java)
        val otherIntent = Intent(MyApp.getInstance(), OtherBR::class.java)

        val builder = NotificationCompat
                .Builder(MyApp.getInstance(), CHANNEL_ID)
                .setCustomBigContentView(collapsedView)
                .setOngoing(true)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = builder
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_notification_widget)
                .build()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID)
        }

        val notificationManager = MyApp.getInstance().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    CHANNEL_ID,
                    MyApp.getInstance().resources.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            channel.enableLights(true)
            channel.setSound(alarmSound, audioAttributes)
            channel.enableVibration(false)
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }

        collapsedView.setOnClickPendingIntent(R.id.tvBoost, getPendingIntent(boostIntent))
        collapsedView.setOnClickPendingIntent(R.id.tvBattery, getPendingIntent(batteryIntent))
        collapsedView.setOnClickPendingIntent(R.id.tvTemp, getPendingIntent(tempIntent))
        collapsedView.setOnClickPendingIntent(R.id.tvDelete, getPendingIntent(clearIntent))
        collapsedView.setOnClickPendingIntent(R.id.tvOther, getPendingIntent(otherIntent))

        if (!SubscriptionProvider.hasSubscription()) {
            setLooper()
        }
        //notificationManager.notify(0, notification)

        //Analytics.showWidget()

        return notification
    }

    private fun setLooper() {
        try {
            Thread {
                checkBattery()
                checkPackages()
                checkFreeSpace()
                TimeUnit.MINUTES.sleep(LOOPER_TIMEOUT)
                setLooper()
            }.start()
        } catch (ex: Exception) {
            ErrorInterceptor.trackLooperError(ex.localizedMessage)
        }
    }

    private fun checkFreeSpace() {
        val stat = StatFs(Environment.getExternalStorageDirectory().path)
        val bytesAvailable = if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.JELLY_BEAN_MR2) {
            stat.blockSizeLong * stat.availableBlocksLong
        } else {
            stat.blockSize.toLong() * stat.availableBlocks.toLong()
        }
        val megAvailable = bytesAvailable / (1024 * 1024)
        if (megAvailable < LOWER_MEMORY_LIMIT) {
            if (!PreferencesProvider.isSpeakLowSpace()!!) {
                showLowFreeSpaceNotification()
                PreferencesProvider.setSpeakLowSpace(true)
            }
        } else if (megAvailable > HIGH_MEMORY_LIMIT) {
            PreferencesProvider.setSpeakLowSpace(false)
        }
    }

    private fun showLowFreeSpaceNotification() {
        createNotif(TYPE_STORAGE)
    }

    private fun checkPackages() {
        try {
            var countApps = MyApp.getInstance().packageManager.getInstalledApplications(PackageManager.GET_META_DATA).size
            if (PreferencesProvider.getAppsCount() != -1) {
                if (countApps == PreferencesProvider.getAppsCount()) {
                    return
                } else if (PreferencesProvider.getAppsCount()!! < countApps) {
                    PreferencesProvider.setAppsCount(countApps)
                    showAddPackageNotification()
                } else {
                    PreferencesProvider.setAppsCount(countApps)
                    showDelPackageNotification()
                }
            } else {
                PreferencesProvider.setAppsCount(countApps)
            }
        } catch (ex: Exception) {
            ErrorInterceptor.trackPackagesError(ex.localizedMessage)
        }
    }

    private fun showDelPackageNotification() {
        createNotif(TYPE_DELETE)
    }

    private fun showAddPackageNotification() {
        createNotif(TYPE_INSTALL)
    }

    private fun checkBattery() {
        var brReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val level = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                when (level) {
                    in 0..EMPTY_BATTERY_LEVEL -> {
                        if (!PreferencesProvider.isSpeakLowBattery(EMPTY_BATTERY_TAG)!!) {
                            showLowBatteryNotification(EMPTY_BATTERY_LEVEL)
                            PreferencesProvider.setSpeakLowBattery(true, EMPTY_BATTERY_TAG)
                            PreferencesProvider.setSpeakLowBattery(true, LOW_BATTERY_TAG)
                            PreferencesProvider.setSpeakLowBattery(true, MEDIUM_BATTERY_TAG)
                            PreferencesProvider.setSpeakLowBattery(true, HIGH_BATTERY_TAG)
                        }
                    }
                    in EMPTY_BATTERY_LEVEL..LOW_BATTERY_LEVEL -> {
                        if (!PreferencesProvider.isSpeakLowBattery(LOW_BATTERY_TAG)!!) {
                            showLowBatteryNotification(LOW_BATTERY_LEVEL)
                            PreferencesProvider.setSpeakLowBattery(true, LOW_BATTERY_TAG)
                            PreferencesProvider.setSpeakLowBattery(true, MEDIUM_BATTERY_TAG)
                            PreferencesProvider.setSpeakLowBattery(true, HIGH_BATTERY_TAG)
                        }
                    }
                    in LOW_BATTERY_LEVEL..MEDIUM_BATTERY_LEVEL -> {
                        if (!PreferencesProvider.isSpeakLowBattery(MEDIUM_BATTERY_TAG)!!) {
                            showLowBatteryNotification(MEDIUM_BATTERY_LEVEL)
                            PreferencesProvider.setSpeakLowBattery(true, MEDIUM_BATTERY_TAG)
                            PreferencesProvider.setSpeakLowBattery(true, HIGH_BATTERY_TAG)
                        }
                    }
                    in MEDIUM_BATTERY_LEVEL..HIGH_BATTERY_LEVEL -> {
                        if (!PreferencesProvider.isSpeakLowBattery(HIGH_BATTERY_TAG)!!) {
                            showLowBatteryNotification(HIGH_BATTERY_LEVEL)
                            PreferencesProvider.setSpeakLowBattery(true, HIGH_BATTERY_TAG)
                        }
                    }
                    in HIGH_BATTERY_LEVEL..MAX_BATTERY_LEVEL -> {
                        PreferencesProvider.setSpeakLowBattery(false, EMPTY_BATTERY_TAG)
                        PreferencesProvider.setSpeakLowBattery(false, LOW_BATTERY_TAG)
                        PreferencesProvider.setSpeakLowBattery(false, MEDIUM_BATTERY_TAG)
                        PreferencesProvider.setSpeakLowBattery(false, HIGH_BATTERY_TAG)
                    }
                }
            }
        }
        MyApp.getInstance().registerReceiver(brReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }


    private fun showLowBatteryNotification(level: Int) {
        createNotif(TYPE_BATTERY)
    }

    fun createNotif(type: String) {
        var version = 2
        var layout = 0
        var icon = 0
        var channel = ""

        val notificationIntent = Intent(MyApp.getInstance().applicationContext, SplashActivity::class.java)

        when (type) {
            TYPE_BATTERY -> {
                layout = batteryLayoutList[version]
                icon = iconsList[0]
                channel = channels[0]
                Analytics.showSystemNotification(PreferencesProvider.SYSTEM_NOTIF_BAT)
                notificationIntent.putExtra(SystemNotifConfig.SYSTEM_NOTIF_TAG, SystemNotifConfig.BAT_TAG)
            }
            TYPE_STORAGE -> {
                layout = storageLayoutList[version]
                icon = iconsList[1]
                channel = channels[1]
                Analytics.showSystemNotification(PreferencesProvider.SYSTEM_NOTIF_STORAGE)
                notificationIntent.putExtra(SystemNotifConfig.SYSTEM_NOTIF_TAG, SystemNotifConfig.STORAGE_TAG)
            }
            TYPE_INSTALL -> {
                layout = installLayoutList[version]
                icon = iconsList[2]
                channel = channels[2]
                Analytics.showSystemNotification(PreferencesProvider.SYSTEM_NOTIF_INSTALL)
                notificationIntent.putExtra(SystemNotifConfig.SYSTEM_NOTIF_TAG, SystemNotifConfig.INSTALL_TAG)
            }
            TYPE_DELETE -> {
                layout = deleteLayoutList[version]
                icon = iconsList[3]
                channel = channels[3]
                Analytics.showSystemNotification(PreferencesProvider.SYSTEM_NOTIF_DELETE)
                notificationIntent.putExtra(SystemNotifConfig.SYSTEM_NOTIF_TAG, SystemNotifConfig.DELETE_TAG)
            }
        }

        var collapsedView = RemoteViews(MyApp.getInstance().packageName, layout)


        val stackBuilder = TaskStackBuilder.create(MyApp.getInstance().applicationContext)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat
                .Builder(MyApp.getInstance().applicationContext, channel)
                .setCustomBigContentView(collapsedView)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = builder
                .setAutoCancel(true)
                .setSmallIcon(icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent).build()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(channel)
        }

        val notificationManager = MyApp
                .getInstance()
                .applicationContext
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    channel,
                    MyApp
                            .getInstance()
                            .applicationContext
                            .resources
                            .getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
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
}