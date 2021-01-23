package dev.mem.rocket.sanya.utils.util

import android.annotation.TargetApi
import android.app.ActivityManager
import android.content.Context
import android.os.Build

class MemStat @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
constructor(context: Context) {
    val totalMemory: Long
    val usedMemory: Long

    init {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        am.getMemoryInfo(memInfo)

        totalMemory = memInfo.totalMem
        usedMemory = memInfo.totalMem - memInfo.availMem
    }
}
