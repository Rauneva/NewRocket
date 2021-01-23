package dev.mem.rocket.sanya.utils.notifications.interactive.brs

import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.PreferencesProvider

class OtherBR : BoostBR() {
    override fun setWay() {
        Analytics.clickOther()
        PreferencesProvider.setWidgetWay(PreferencesProvider.WAY_OTHER)
    }
}