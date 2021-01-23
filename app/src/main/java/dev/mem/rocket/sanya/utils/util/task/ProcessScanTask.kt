package dev.mem.rocket.sanya.utils.util.task

import android.content.pm.PackageManager
import android.os.AsyncTask

import com.jaredrummler.android.processes.AndroidProcesses

import java.io.IOException
import java.util.ArrayList
import java.util.Collections

import dev.mem.rocket.sanya.MyApp
import dev.mem.rocket.sanya.utils.util.callback.IScanCallback
import dev.mem.rocket.sanya.utils.util.model.JunkInfo

class ProcessScanTask(private val mCallback: IScanCallback) : AsyncTask<Void, Void, Void>() {

    override fun doInBackground(vararg params: Void): Void? {
        mCallback.onBegin()

        val processes = AndroidProcesses.getRunningAppProcesses()

        val junks = ArrayList<JunkInfo>()

        for (process in processes) {
            val info = JunkInfo()
            info.mIsChild = false
            info.mIsVisible = true
            info.mPackageName = process.packageName

            try {
                val statm = process.statm()
                info.mSize = statm.residentSetSize
            } catch (e: IOException) {
                e.printStackTrace()
                continue
            }

            try {
                val pm = MyApp.getInstance().packageManager
                val packageInfo = process.getPackageInfo(MyApp.getInstance(), 0)
                info.name = packageInfo.applicationInfo.loadLabel(pm).toString()
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                continue
            }

            mCallback.onProgress(info)

            junks.add(info)
        }

        Collections.sort(junks)
        Collections.reverse(junks)
        mCallback.onFinish(junks)

        return null
    }
}
