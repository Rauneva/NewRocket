package dev.mem.rocket.sanya.utils.util.model


import java.util.ArrayList

import dev.mem.rocket.sanya.MyApp
import dev.mem.rocket.sanya.R

class JunkInfo : Comparable<JunkInfo> {
    var name: String? = null
    var mSize: Long = 0
    var mPackageName: String? = null
    var mPath: String? = null
    var mChildren = ArrayList<JunkInfo>()
    var mIsVisible = false
    var mIsChild = true

    override fun compareTo(another: JunkInfo): Int {
        val top = MyApp.getInstance().getString(R.string.system_cache)

        if (this.name != null && this.name == top) {
            return 1
        }

        if (another.name != null && another.name == top) {
            return -1
        }

        return if (this.mSize > another.mSize) {
            1
        } else if (this.mSize < another.mSize) {
            -1
        } else {
            0
        }
    }
}
