package dev.mem.rocket.sanya.utils.notifications.interactive.brs

import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.PreferencesProvider

class ClearBR : BoostBR() {

    override fun setWay() {
        Analytics.clickClean()
        PreferencesProvider.setWidgetWay(PreferencesProvider.WAY_CLEAR)
    }
}