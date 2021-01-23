package dev.mem.rocket.sanya.utils.notifications.interactive.brs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.mem.rocket.sanya.SplashActivity
import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.PreferencesProvider

open class BoostBR : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        closeStatusBarPanel(context)
        setWay()
        context.startActivity(Intent(context, SplashActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    private fun closeStatusBarPanel(context: Context) {
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }

    open fun setWay() {
        Analytics.clickBoost()
        PreferencesProvider.setWidgetWay(PreferencesProvider.WAY_BOOST)
    }
}