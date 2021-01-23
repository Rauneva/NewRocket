package dev.mem.rocket.sanya.presentation.battery.medium

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd

import dev.mem.rocket.sanya.OOP.PowersClass
import dev.mem.rocket.sanya.PPP.PowerAdapter


import java.util.ArrayList

import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.PreferencesProvider
import dev.mem.rocket.sanya.utils.ads.nat.NativeProvider
import dev.mem.rocket.sanya.utils.ads.nat.NativeSpeaker
import kotlinx.android.synthetic.main.banner_layout.adView
import kotlinx.android.synthetic.main.native_layout.*
import kotlinx.android.synthetic.main.powersaving_popup.*
import kotlinx.android.synthetic.main.powersaving_popup.i_native

/**
 * Created by intag pc on 2/19/2017.
 */

class PowerSaving_popup : Activity() {
    internal var items: MutableList<PowersClass> = mutableListOf()

    lateinit var mAdapter: PowerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        setContentView(R.layout.powersaving_popup)
        Analytics.openPart(Analytics.OPEN_ES_POWERSAVING)

        val adRequest = AdRequest.Builder().build()
        if(!SubscriptionProvider.hasSubscription()) {
            adView!!.loadAd(adRequest)
            //bindNative()
        }

        items = ArrayList()
        applied.setOnClickListener {

            startClear()
        }

        mAdapter = PowerAdapter(items)
        mAdapter.notifyDataSetChanged()

        if (PreferencesProvider.getWidgetWay() == PreferencesProvider.WAY_BATTERY){
            startClear()
        }
    }

    private fun startClear() {
        PreferencesProvider.getInstance().edit()
                .putString("mode", "1")
                .apply()

        val i = Intent(applicationContext, PowerSaving_Complition::class.java)
        startActivity(i)

        finish()
    }

    fun add(text: String, position: Int) {
        val item = PowersClass(text)
        items.add(item)
        mAdapter.notifyItemInserted(position)
    }

    override fun onBackPressed() {
        //   super.onBackPressed();
    }

    private fun bindNative() {
        if (!SubscriptionProvider.hasSubscription()) {
            NativeProvider.observeOnNativeList(object : NativeSpeaker {
                override fun loadFin(nativeAd: UnifiedNativeAd) {
                    //i_native.visibility = View.VISIBLE
                    //setNative()
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

}
