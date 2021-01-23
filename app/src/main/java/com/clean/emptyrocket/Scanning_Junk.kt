package com.clean.emptyrocket

import android.animation.Animator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd
import dev.mem.rocket.sanya.MainActivity
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.ads.AdMobFullscreenManager
import dev.mem.rocket.sanya.utils.ads.nat.NativeProvider
import dev.mem.rocket.sanya.utils.ads.nat.NativeSpeaker
import kotlinx.android.synthetic.main.banner_layout.*
import kotlinx.android.synthetic.main.native_layout.*
import kotlinx.android.synthetic.main.scanning_junk.*

class Scanning_Junk : Activity(), AdMobFullscreenManager.AdMobFullscreenDelegate {


    private var counter = 0
    private var fullscreenManager: AdMobFullscreenManager? = null

    private val adManager: AdMobFullscreenManager?
        get() {
            if (fullscreenManager == null) {
                configureManager()
            }
            return fullscreenManager
        }

    private fun bindNative() {
        if (!SubscriptionProvider.hasSubscription()) {
            NativeProvider.observeOnNativeList(object : NativeSpeaker {
                override fun loadFin(nativeAd: UnifiedNativeAd) {
                    i_native.visibility = View.VISIBLE
                    setNative()
                    bindAdView(nativeAd)
                }
            })
        } else {
            i_native.visibility = View.GONE
        }
    }

    private fun bindAdView(nativeAd: UnifiedNativeAd) {
        (ad_view.headlineView as TextView).text = nativeAd.headline
        (ad_view.bodyView as TextView).text = nativeAd.body
        (ad_view.callToActionView as Button).text = nativeAd.callToAction
        val icon = nativeAd.icon
        if (icon == null) {
            ad_view.iconView.visibility = View.INVISIBLE
        } else {
            (ad_view.iconView as ImageView).setImageDrawable(icon.drawable)
            ad_view.iconView.visibility = View.VISIBLE
        }
        ad_view.setNativeAd(nativeAd)
    }

    private fun setNative() {
        ad_view.mediaView = ad_media
        ad_view.headlineView = ad_headline
        ad_view.bodyView = ad_body
        ad_view.callToActionView = ad_call_to_action
        ad_view.iconView = ad_icon
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanning_junk)
        Analytics.openPart(Analytics.OPEN_JUNK_CLEAN_START)

        val adRequest = AdRequest.Builder().build()
        if (!SubscriptionProvider.hasSubscription()) {
            adView!!.loadAd(adRequest)
            //bindNative()
        }

        lavLoad.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                Log.e("LOL", "onAnimationRepeat")
                if (counter > 1) {
                    lavLoad.pauseAnimation()
                    lavLoad.visibility = View.INVISIBLE
                    lavComplete.visibility = View.VISIBLE
                    lavComplete.playAnimation()
                    lavLoad.removeAllAnimatorListeners()
                } else {
                    counter++
                }
            }

            override fun onAnimationEnd(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        lavComplete.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                lavComplete.pauseAnimation()
                adManager!!.completed()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

    }


    override fun onBackPressed() {
        //        super.onBackPressed();
    }

    private fun configureManager() {
        if (fullscreenManager == null) {
            fullscreenManager = AdMobFullscreenManager(this, this)
        } else {
            fullscreenManager!!.reloadAd()
        }
    }


    override fun ADLoaded() {
        adManager!!.showAdd(Analytics.from_scanning_junk_2)
    }

    override fun ADIsClosed() {
        val intent = Intent(this@Scanning_Junk, MainActivity::class.java)
        intent.putExtra("frag", 3)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
