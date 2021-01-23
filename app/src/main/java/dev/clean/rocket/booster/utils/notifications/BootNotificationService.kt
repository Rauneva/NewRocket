package dev.clean.rocket.booster.utils.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootNotificationService : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        BootReceiver.enqueueWork(context!!, Intent())
    }
}