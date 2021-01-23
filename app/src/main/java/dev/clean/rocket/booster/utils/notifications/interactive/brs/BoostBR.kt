package dev.clean.rocket.booster.utils.notifications.interactive.brs

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.clean.rocket.booster.SplashActivity
import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.PreferencesProvider

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