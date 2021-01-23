package dev.clean.rocket.booster.AAA

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import dev.clean.rocket.booster.utils.PreferencesProvider

/**
 * Created by intag pc on 3/2/2017.
 */

class Alarm_Junk : BroadcastReceiver() {

    //todo Prefs

    companion object {
        private var needToCheck = MutableLiveData<Boolean>()
        fun getNeedToCheck() = needToCheck
    }

    override fun onReceive(context: Context, intent: Intent) {
        PreferencesProvider.getInstance().edit().putString("junk", "1").apply()

        try {
            needToCheck.postValue(true)
        } catch (e: Exception) {

        }

    }

}

