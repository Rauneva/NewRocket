package dev.mem.rocket.sanya.utils

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.mem.rocket.sanya.BuildConfig
import dev.mem.rocket.sanya.Config
import dev.mem.rocket.sanya.utils.analytic.Analytics

object NewConfigProvider {

    private var hasRequest = false

    fun runSetup() {
        if (!hasRequest && Config.NEED_LOAD) {
            hasRequest = true
            requestPercent()
        }
    }

    private fun requestPercent() {
        var path = "percent_${BuildConfig.VERSION_CODE}"
        FirebaseDatabase.getInstance()
                .reference
                .child(path)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.e("LOL", "cancelled ${p0.message}")
                        Analytics.setFrequency(100)
                        PreferencesProvider.setPercent(100)
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Log.e("LOL", "not cancelled")
                        if (p0.getValue(Int::class.java) == null) {
                            createNewDirectory(path)
                        } else {
                            val newPercent = p0.getValue(Int::class.java) ?: 100
                            Log.e("LOL", "$newPercent")
                            Analytics.setFrequency(newPercent)
                            PreferencesProvider.setPercent(newPercent)
                        }
                    }
                })
    }

    private fun createNewDirectory(path: String) {
        FirebaseDatabase
                .getInstance()
                .reference
                .child(path)
                .setValue(100)
                .addOnSuccessListener {
            requestPercent()
        }
    }
}