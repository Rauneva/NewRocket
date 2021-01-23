package dev.mem.rocket.sanya.utils.ads.nat

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd
import dev.mem.rocket.sanya.Config
import dev.mem.rocket.sanya.MyApp
import dev.mem.rocket.sanya.R
import kotlin.random.Random

object TestNativeProvider {

    var adsList: ArrayList<UnifiedNativeAd> = arrayListOf()
    var bufferAdsList: ArrayList<UnifiedNativeAd> = arrayListOf()
    var nativeSpeaker: NativeSpeaker? = null
    var adLoader: AdLoader? = null
    val NATIVE_ITEMS_MAX = 3
    var counter = 0

    fun loadNative(){
        adLoader = AdLoader
                .Builder(MyApp.getInstance(), MyApp.getInstance().getString(R.string.ab_native_ad))
                .forUnifiedNativeAd { nativeAD ->
                    bufferAdsList.add(nativeAD)
                    if (!adLoader!!.isLoading) {
                        endLoading()
                    }
                }.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(p0: Int) {
                        if (!adLoader!!.isLoading) {
                            endLoading()
                        }
                    }
                }).build()
        adLoader?.loadAds(AdRequest.Builder().build(), NATIVE_ITEMS_MAX)
    }

    private fun endLoading() {
        Log.e("LOL", "endLoading")
        if (bufferAdsList.size > 0 && !Config.FOR_TEST) {
            adsList = bufferAdsList
            bufferAdsList = arrayListOf()
            nativeSpeaker?.loadFin(getItem(adsList))
        }
    }

    private fun getItem(adsList: ArrayList<UnifiedNativeAd>): UnifiedNativeAd {
        return adsList[Random.nextInt(2)]
    }

    fun observeOnNativeList(nativeSpeaker: NativeSpeaker) {
        if (adsList.size > 0) {
            nativeSpeaker.loadFin(getItem(adsList))
        } else {
            NativeProvider.nativeSpeaker = nativeSpeaker
        }
    }

    fun refreshNativeAd(context: Context) {
        nativeSpeaker = null
        //loadNative(context)
    }
}