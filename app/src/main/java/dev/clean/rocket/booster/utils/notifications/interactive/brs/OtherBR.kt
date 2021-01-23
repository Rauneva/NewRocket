package dev.clean.rocket.booster.utils.notifications.interactive.brs

import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.PreferencesProvider

class OtherBR : BoostBR() {
    override fun setWay() {
        Analytics.clickOther()
        PreferencesProvider.setWidgetWay(PreferencesProvider.WAY_OTHER)
    }
}