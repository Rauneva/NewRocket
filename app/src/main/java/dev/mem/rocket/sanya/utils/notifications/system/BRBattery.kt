package dev.mem.rocket.sanya.utils.notifications.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BRBattery : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("LOL", "low")
    }
}