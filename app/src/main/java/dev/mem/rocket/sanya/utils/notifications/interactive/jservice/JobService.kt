package dev.mem.rocket.sanya.utils.notifications.interactive.jservice

import android.app.job.JobParameters
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import dev.mem.rocket.sanya.utils.notifications.interactive.SystemNotifConfig.PATH
import dev.mem.rocket.sanya.utils.notifications.interactive.ProcessMainClass
import dev.mem.rocket.sanya.utils.notifications.interactive.brs.RestartBR
import java.lang.Exception

class JobService : android.app.job.JobService() {

    val TAG = JobService::class.java.simpleName
    var restartBR: RestartBR? = null
    var instance: JobService? = null
    var jobParams: JobParameters? = null


    companion object{
        var jobParams: JobParameters? = null
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        var broadcastIntent = Intent(PATH)
        sendBroadcast(broadcastIntent)
        Handler().postDelayed({ unregisterReceiver(restartBR) }, 1000)
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        var processMainClass = ProcessMainClass()
        processMainClass.launchService(this)
        registerRestartReceiver()
        instance = this
        JobService.jobParams = jobParams
        return false
    }

    private fun registerRestartReceiver(){
        if(restartBR == null){
            restartBR = RestartBR()
        }else try {
            unregisterReceiver(restartBR)
        }catch (ex : Exception){

        }

        Handler().postDelayed({
            var filter = IntentFilter()
            filter.addAction(PATH)
            try {
                applicationContext.registerReceiver(restartBR, filter)
            }catch (ex : Exception){

            }
        }, 1000)
    }


}