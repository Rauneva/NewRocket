package dev.mem.rocket.sanya.utils.notifications.fcm

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

object FBWork {

    fun getFCMToken() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("LOL", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }
                    Log.e("LOL", "FCM token -- ${task.result?.token}")
                })
    }
}