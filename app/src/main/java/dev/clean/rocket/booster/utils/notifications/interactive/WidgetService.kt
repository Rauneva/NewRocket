package dev.clean.rocket.booster.utils.notifications.interactive

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import java.lang.Exception

class WidgetService : Service() {

    var isServiceRunning = false
    val NOTIFICATION_ID = 1212434534
    var currentService : Service? = null

    override fun onCreate() {
        super.onCreate()
        Log.e("LOL", "onCreate")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            restartForeground()
        }
        currentService = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.e("LOL", "onStartCommand")

        if (intent == null){
            var pmc = ProcessMainClass()
            pmc.launchService(this)
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            restartForeground()
        }
        return START_STICKY
    }

    fun restartForeground(){
        Log.e("LOL", "restartForeground")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            try {
                startForeground(NOTIFICATION_ID, CleanNotification.newCreate())
            }catch (ex : Exception){

            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }



    override fun onDestroy() {
        super.onDestroy()
        var intent = Intent(SystemNotifConfig.PATH)
        sendBroadcast(intent)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        var broadcastInent = Intent(SystemNotifConfig.PATH)
        sendBroadcast(broadcastInent)
    }
}