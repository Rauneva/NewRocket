package dev.clean.rocket.booster.utils.notifications.interactive.brs

import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.PreferencesProvider

class ClearBR : BoostBR() {

    override fun setWay() {
        Analytics.clickClean()
        PreferencesProvider.setWidgetWay(PreferencesProvider.WAY_CLEAR)
    }
}