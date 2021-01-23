package dev.clean.rocket.booster.utils.notifications.interactive.brs

import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.PreferencesProvider

class BatteryBR : BoostBR() {

    override fun setWay() {
        Analytics.clickBat()
        PreferencesProvider.setWidgetWay(PreferencesProvider.WAY_BATTERY)
    }
}