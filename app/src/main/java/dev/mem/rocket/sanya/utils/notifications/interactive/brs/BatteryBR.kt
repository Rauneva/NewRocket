package dev.mem.rocket.sanya.utils.notifications.interactive.brs

import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.PreferencesProvider

class BatteryBR : BoostBR() {

    override fun setWay() {
        Analytics.clickBat()
        PreferencesProvider.setWidgetWay(PreferencesProvider.WAY_BATTERY)
    }
}