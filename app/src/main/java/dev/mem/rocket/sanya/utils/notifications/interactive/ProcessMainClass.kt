package dev.mem.rocket.sanya.utils.notifications.interactive

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class ProcessMainClass {
    val TAG = ProcessMainClass::class.java.simpleName

    companion object{
        private var serviceIntent: Intent? = null

    }

    private fun setServiceIntent(context: Context) {
        if (serviceIntent == null) {
            serviceIntent = Intent(context, WidgetService::class.java)
        }
    }

    fun launchService(context: Context) {
        Log.e("LOL", "launchService")
        if (context == null) {
            return
        }

        setServiceIntent(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}