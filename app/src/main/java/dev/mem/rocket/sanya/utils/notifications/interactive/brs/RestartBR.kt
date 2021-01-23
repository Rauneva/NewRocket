package dev.mem.rocket.sanya.utils.notifications.interactive.brs

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import dev.mem.rocket.sanya.utils.notifications.interactive.SystemNotifConfig
import dev.mem.rocket.sanya.utils.notifications.interactive.ProcessMainClass
import dev.mem.rocket.sanya.utils.notifications.interactive.jservice.JobService
import java.lang.Exception

class RestartBR : BroadcastReceiver() {

    var restartBR: RestartBR? = null

    companion object{
        var jobScheduler: JobScheduler? = null

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        fun scheduleJob(context: Context) {
            if (jobScheduler == null) {
                jobScheduler = context
                        .getSystemService(Context.JOB_SCHEDULER_SERVICE)
                        as JobScheduler
            }

            var componentName = ComponentName(context, JobService::class.java)

            var jobInfo = JobInfo.Builder(1, componentName)
                    .setOverrideDeadline(0)
                    .setPersisted(true)
                    .build()

            jobScheduler!!.schedule(jobInfo)
        }
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scheduleJob(context!!)
        } else {
            registerRestartReceiver(context!!)
            var pcm = ProcessMainClass()
            pcm.launchService(context)
        }
    }

    private fun registerRestartReceiver(context: Context){
        if(restartBR == null){
            restartBR = RestartBR()
        }else try {
            context.unregisterReceiver(restartBR)
        }catch (ex : Exception){

        }

        Handler().postDelayed({
            var filter = IntentFilter()
            filter.addAction(SystemNotifConfig.PATH)
            try {
                context.applicationContext.registerReceiver(restartBR, filter)
            }catch (ex : Exception){

            }
        }, 1000)
    }


}