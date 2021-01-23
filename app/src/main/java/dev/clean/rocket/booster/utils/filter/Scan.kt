package dev.clean.rocket.booster.utils.filter

import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.PreferencesProvider
import java.text.DecimalFormat

object Scan {

    const val TRIGGER_SHOW_COUNT = 10

    fun increaseShow(){
        var countShow = PreferencesProvider.getShowCount()!!
        var countClicks = PreferencesProvider.getClickCount()!!

        countShow++
        PreferencesProvider.setShowCount(countShow)
        detectFrod(countShow, countClicks)
    }

    private fun detectFrod(countShow: Int, countClicks: Int) {
        if (countShow > TRIGGER_SHOW_COUNT){
            var frequency : Double = countClicks.toDouble() / countShow.toDouble()
            var id = PreferencesProvider.getId()!!
            var bd = DecimalFormat("##.#")
            var newFreq = bd.format(frequency)
            Analytics.setFrodStatus(newFreq, id)
        }
    }

    fun increaseClick(){
        var countShow = PreferencesProvider.getShowCount()!!
        var countClicks = PreferencesProvider.getClickCount()!!

        countClicks++
        PreferencesProvider.setClickCount(countClicks)
        detectFrod(countShow, countClicks)
    }
}