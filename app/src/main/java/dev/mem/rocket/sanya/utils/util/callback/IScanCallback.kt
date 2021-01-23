package dev.mem.rocket.sanya.utils.util.callback


import java.util.ArrayList

import dev.mem.rocket.sanya.utils.util.model.JunkInfo

interface IScanCallback {
    fun onBegin()

    fun onProgress(info: JunkInfo)

    fun onFinish(children: ArrayList<JunkInfo>)
}
