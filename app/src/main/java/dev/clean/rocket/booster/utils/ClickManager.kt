package dev.clean.rocket.booster.utils

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.clean.rocket.booster.Config
import dev.clean.rocket.booster.utils.analytic.Analytics

object ClickManager {
    private var hasRequest = false
    const val DEFUALT_COUNT_CLICK = 1

    fun runSetup() {
        if (!hasRequest && Config.NEED_LOAD_CLICK_TRIGGER) {
            hasRequest = true
            requestPercent {
                Log.e("LOL", it.toString() + "trigger ")
                PreferencesProvider.setTrigger(it)
                refreshBanStatus()
            }
        }
    }

    private fun refreshBanStatus() {
        if (PreferencesProvider.getTrigger()!! <= PreferencesProvider.getClickCount()!!) {
            PreferencesProvider.setAdBanStatus(true)
            Analytics.setBanVersion("true")
        } else {
            PreferencesProvider.setAdBanStatus(false)
            Analytics.setBanVersion("false")
        }
    }

    private fun requestPercent(onResult: (Int) -> Unit) {
        FirebaseDatabase.getInstance()
                .reference
                .child("trigger")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        onResult(DEFUALT_COUNT_CLICK)
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val newPercent = p0.getValue(Int::class.java) ?: DEFUALT_COUNT_CLICK
                        onResult(newPercent)
                    }
                })
    }

    fun increaseClickCount() {
        var clickCount = PreferencesProvider.getClickCount()!!
        clickCount++
        PreferencesProvider.setClickCount(clickCount)
        Analytics.setClickCount(clickCount.toString())

        refreshBanStatus()
    }

    fun increaseShowCount() {
        var showCount = PreferencesProvider.getShowCount()!!
        showCount++
        PreferencesProvider.setShowCount(showCount)
        Analytics.setShowCount(showCount.toString())
    }
}