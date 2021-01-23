package dev.clean.rocket.booster.utils.util.callback


import java.util.ArrayList

import dev.clean.rocket.booster.utils.util.model.JunkInfo

interface IScanCallback {
    fun onBegin()

    fun onProgress(info: JunkInfo)

    fun onFinish(children: ArrayList<JunkInfo>)
}
