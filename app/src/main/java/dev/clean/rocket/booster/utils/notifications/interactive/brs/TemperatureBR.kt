package dev.clean.rocket.booster.utils.notifications.interactive.brs

import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.PreferencesProvider

class TemperatureBR : BoostBR() {
    override fun setWay() {
        Analytics.clickTemp()
        PreferencesProvider.setWidgetWay(PreferencesProvider.WAY_TEMP)
    }
}