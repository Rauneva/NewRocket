package dev.mem.rocket.sanya.utils.notifications

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.utils.PreferencesProvider
import java.util.*

class BootReceiver : JobIntentService() {

    companion object{
        private const val JOB_ID = 1

        fun  enqueueWork(context: Context, work: Intent){
            enqueueWork(context, BootReceiver::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        PreferencesProvider.getInstance().edit().putString("state_Head", resources.getString(R.string.notif_head))
                .putString("state_Body", resources.getString(R.string.notif_body))
                .apply()

        val calendar = Calendar.getInstance()
        val now = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 18)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        //if user sets the alarm after their preferred time has already passed that day
        if (now.after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(this, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val alarmManager = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

    }
}