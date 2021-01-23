package dev.clean.rocket.booster.utils.util

import android.os.Build
import android.os.Environment
import android.os.StatFs

class DiskStat {

    private var mExternalBlockSize: Long = 0
    private var mExternalBlockCount: Long = 0
    private var mExternalAvailableBlocks: Long = 0

    private var mInternalBlockSize: Long = 0
    private var mInternalBlockCount: Long = 0
    private var mInternalAvailableBlocks: Long = 0

    val totalSpace: Long
        get() = mInternalBlockSize * mInternalBlockCount + mExternalBlockSize * mExternalBlockCount

    val usedSpace: Long
        get() = mInternalBlockSize * (mInternalBlockCount - mInternalAvailableBlocks) + mExternalBlockSize * (mExternalBlockCount - mExternalAvailableBlocks)

    val usableSpace: Long
        get() = mInternalBlockSize * mInternalAvailableBlocks + mExternalBlockSize * mExternalAvailableBlocks

    init {
        calculateInternalSpace()
        calculateExternalSpace()
    }

    private fun calculateExternalSpace() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val sdcardDir = Environment.getExternalStorageDirectory()
            val sf = StatFs(sdcardDir.path)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mExternalBlockSize = sf.blockSizeLong
                mExternalBlockCount = sf.blockCountLong
                mExternalAvailableBlocks = sf.availableBlocksLong
            } else {
                mExternalBlockSize = sf.blockSize.toLong()
                mExternalBlockCount = sf.blockCount.toLong()
                mExternalAvailableBlocks = sf.availableBlocks.toLong()
            }
        }
    }

    private fun calculateInternalSpace() {
        val root = Environment.getRootDirectory()
        val sf = StatFs(root.path)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mInternalBlockSize = sf.blockSizeLong
            mInternalBlockCount = sf.blockCountLong
            mInternalAvailableBlocks = sf.availableBlocksLong
        } else {
            mInternalBlockSize = sf.blockSize.toLong()
            mInternalBlockCount = sf.blockCount.toLong()
            mInternalAvailableBlocks = sf.availableBlocks.toLong()
        }
    }
}
