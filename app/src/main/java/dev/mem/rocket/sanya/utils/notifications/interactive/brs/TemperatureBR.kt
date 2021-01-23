package dev.mem.rocket.sanya.utils.notifications.interactive.brs

import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.PreferencesProvider

class TemperatureBR : BoostBR() {
    override fun setWay() {
        Analytics.clickTemp()
        PreferencesProvider.setWidgetWay(PreferencesProvider.WAY_TEMP)
    }
}