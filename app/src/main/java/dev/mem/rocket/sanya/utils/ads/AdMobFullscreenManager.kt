package dev.mem.rocket.sanya.utils.ads

import android.content.Context
import android.util.Log

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import dev.mem.rocket.sanya.Config
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.NewConfigProvider
import dev.mem.rocket.sanya.utils.PreferencesProvider
import dev.mem.rocket.sanya.utils.filter.Scan
import kotlin.random.Random


class AdMobFullscreenManager(private val context: Context, delegate: AdMobFullscreenDelegate?) {

    private var countRequestAd: Int = 0

    private var delegate: AdMobFullscreenDelegate? = null
    var extrasInt: Long = -1
    var tryingShowDone = false
    private var adMobState = AdMobState.Loading
    private val mInterstitialAd: InterstitialAd
    private var percent = 0

    enum class AdMobState {
        Loaded,
        Loading,
        NoAd
    }


    init {
        mInterstitialAd = InterstitialAd(context)
        configure(delegate)
        NewConfigProvider.runSetup()
    }

    private fun configure(delegate: AdMobFullscreenDelegate?) {
        mInterstitialAd.setAdUnitId(context?.getString(R.string.interstitial))
        //mInterstitialAd.setAdUnitId("ca-app-pub-3149924354118474/9415209496");
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        this.delegate = delegate
        mInterstitialAd.setAdListener(object : AdListener() {
            override fun onAdLoaded() {
                Analytics.loaded()
                countRequestAd = 0
                adMobState = AdMobState.Loaded
                if (this@AdMobFullscreenManager.delegate != null) {
                    this@AdMobFullscreenManager.delegate!!.ADLoaded()
                }
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Log.e("LOL", "onAdFailedToLoad -- $errorCode")
                this@AdMobFullscreenManager.reloadAd()
                Analytics.failed()
            }

            override fun onAdOpened() {
                Analytics.show()
                // Code to be executed when the ad is displayed.
                Log.e("LOL", "onAdOpened")
                Scan.increaseShow()
            }

            override fun onAdClicked() {
                super.onAdClicked()
                Scan.increaseClick()
            }

            override fun onAdLeftApplication() {
            }

            override fun onAdClosed() {
                if (this@AdMobFullscreenManager.delegate != null) {
                    this@AdMobFullscreenManager.delegate!!.ADIsClosed()
                }
            }
        })
    }

    fun showAdd(from : String): Boolean {
        Analytics.request() //46 k
        var b = false
        if(SubscriptionProvider.hasSubscription()){ // 258
            PreferencesProvider.clearWidgetAdLabel()
            Analytics.requestSub()
            this@AdMobFullscreenManager.delegate!!.ADIsClosed()
        }else{ // 46 k
            if (mInterstitialAd.isLoaded){ //40 k
                if (needShow()){
                    bindShowAdAnalytics(from)
                    mInterstitialAd.show()
                }else{
                    PreferencesProvider.clearWidgetAdLabel()
                    this@AdMobFullscreenManager.delegate!!.ADIsClosed() //10k
                }
                b = true
            }else{ // 6k
                //booster and scanning junk
                bindNotLoadedAnalytics(from)
                delegate?.ADIsClosed()
            }
        }
        return b
    }

    private fun bindNotLoadedAnalytics(from: String) {
        var currentFrom = from
        if (PreferencesProvider.getWidgetAdLabel() != PreferencesProvider.WAY_NONE){
            currentFrom += Analytics.from_widget
            PreferencesProvider.clearWidgetAdLabel()
        }
        Analytics.requestNotLoaded(currentFrom)
    }

    private fun bindShowAdAnalytics(from: String) {
        var currentFrom = from
        if (PreferencesProvider.getWidgetAdLabel() != PreferencesProvider.WAY_NONE){
            currentFrom += Analytics.from_widget
            PreferencesProvider.clearWidgetAdLabel()
        }else if (PreferencesProvider.getFCMNotifWay()!!){
            currentFrom += Analytics.from_fcm
        }else if (PreferencesProvider.getSystemNotifWay() != PreferencesProvider.SYSTEM_NOTIF_EMPTY){
            when(PreferencesProvider.getSystemNotifWay()){
                PreferencesProvider.SYSTEM_NOTIF_BAT -> currentFrom += Analytics.from_sys_bat
                PreferencesProvider.SYSTEM_NOTIF_DELETE -> currentFrom += Analytics.from_sys_delete
                PreferencesProvider.SYSTEM_NOTIF_INSTALL -> currentFrom += Analytics.from_sys_install
                PreferencesProvider.SYSTEM_NOTIF_STORAGE -> currentFrom += Analytics.from_sys_storage
            }
        }
        Analytics.needShow(currentFrom) //38k
    }

    private fun needShow(): Boolean{
        if (Config.FOR_TEST){
            return false
        }else {
            return Random.nextInt(100) <= PreferencesProvider.getPercent()!!
        }
    }

    fun completed() {
        tryingShowDone = true
        if (adMobState == AdMobState.Loaded) {
            if (delegate != null) {
                delegate!!.ADLoaded()
            }
        } else if (adMobState == AdMobState.NoAd) {
            if (delegate != null) {
                delegate!!.ADIsClosed()
            }
        }

    }

    fun reloadAd() {
        countRequestAd++
        if (countRequestAd == MAX_REQUEST_AD) {
            adMobState = AdMobState.NoAd
            if (delegate != null) {
                delegate!!.ADIsClosed()
            }
            return
        }

        if (mInterstitialAd.isLoaded()) {
            adMobState = AdMobState.Loaded
            mInterstitialAd.show()
        } else if (!mInterstitialAd.isLoading()) {
            adMobState = AdMobState.Loading
            mInterstitialAd.loadAd(AdRequest.Builder().build())
        }
    }

    interface AdMobFullscreenDelegate {
        fun ADLoaded()
        fun ADIsClosed()
    }

    companion object {

        private val MAX_REQUEST_AD = 3
    }


}


