package dev.mem.rocket.sanya.presentation.battery.hard


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
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.ads.nat.NativeProvider
import dev.mem.rocket.sanya.utils.ads.nat.NativeSpeaker
import kotlinx.android.synthetic.main.banner_layout.*
import kotlinx.android.synthetic.main.native_layout.*
import kotlinx.android.synthetic.main.ultra_popup.*
import java.util.*

/**
 * Created by intag pc on 2/19/2017.
 */

class Ultra_PopUp : Activity() {

    lateinit var mAdapter: PowerAdapter
    internal var items: MutableList<PowersClass> = mutableListOf()
    internal var hour: Int = 0
    internal var min: Int = 0

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
        val b = intent.extras
        setContentView(R.layout.ultra_popup)
        Analytics.openPart(Analytics.OPEN_ES_EXTREME)


        val adRequest = AdRequest.Builder().build()
        if(!SubscriptionProvider.hasSubscription()) {
            adView!!.loadAd(adRequest)
            //bindNative()
        }
        try {
            hour = Integer.parseInt(b!!.getString("hour")!!.replace("[^0-9]".toRegex(), "")) - Integer.parseInt(b!!.getString("hournormal")!!.replace("[^0-9]".toRegex(), ""))
            min = Integer.parseInt(b!!.getString("minutes")!!.replace("[^0-9]".toRegex(), "")) - Integer.parseInt(b!!.getString("minutesnormal")!!.replace("[^0-9]".toRegex(), ""))
        } catch (e: Exception) {
            hour = 4
            min = 7
        }

        if (hour == 0 && min == 0) {
            hour = 4
            min = 7
        }

        addedtime.text = "(+" + hour + " h " + Math.abs(min) + " m)"
        addedtimedetail.text = getString(R.string.extended_battery_up_to) + Math.abs(hour) + "h " + Math.abs(min) + "m"

        applied.setOnClickListener {
            val i = Intent(this@Ultra_PopUp, Applying_Ultra::class.java)
            startActivity(i)
            finish()
        }

        items = ArrayList()
    }

    fun add(text: String, position: Int) {
        val item = PowersClass(text)
        items.add(item)
        mAdapter.notifyItemInserted(position)

    }

    override fun onBackPressed() {
        // super.onBackPressed();
    }

}
