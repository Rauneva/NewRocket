package dev.mem.rocket.sanya

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Build
import android.os.Handler
import android.os.Process
import android.webkit.WebView
import com.amplitude.api.Amplitude
import com.google.android.gms.ads.MobileAds
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import dev.mem.rocket.sanya.utils.SubscriptionProvider

class MyApp : Application() {

    @Volatile
    var applicationHandler: Handler? = null



    override fun onCreate() {
        super.onCreate()
        sInstance = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val process = getProcessName(this)
            if (packageName != process) {
                WebView.setDataDirectorySuffix(process)
            }
        }
        SubscriptionProvider.init(this)

        //MobileAds.initialize(this, getString(R.string.interstitial))
        MobileAds.initialize(this, getString(R.string.admob_id))
        val config = YandexMetricaConfig.newConfigBuilder("04c86bac-0b6a-4a17-ab35-809884595f8f").build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)
        Amplitude.getInstance()
                //.initialize(this, "a599776e0f5f48011f9842ca7bffad60")  // old
                .initialize(this, "414b963913e653b6594dcc19d9b2555e") // new 21.01.2021
                .enableForegroundTracking(this)

        applicationHandler =  Handler(applicationContext.getMainLooper());
        /*if (BuildConfig.DEBUG) {
            Bugsee.launch(this, "1187e351-e756-4bad-80af-5efa69a3ff56")
        }*/
        createNotificationChannel()
    }

    @SuppressLint("NewApi")
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val att = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            val channelId = "com.clean.emptyrocket"
            val channelName = "com.clean.emptyrocket"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.lightColor = Color.parseColor("#4B8A08")
            channel.vibrationPattern = longArrayOf(0, 500)
            channel.enableVibration(true)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun getProcessName(context: Context?): String? {
        if (context == null) return null
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName
            }
        }
        return null
    }

    companion object {

        private lateinit var sInstance: MyApp

        fun getInstance(): MyApp {
            return sInstance
        }

        /**
         * Returns an instance of [MyApp] attached to the passed activity.
         */
        fun getInstance(activity: Activity): MyApp {
            return activity.application as MyApp
        }
    }
}