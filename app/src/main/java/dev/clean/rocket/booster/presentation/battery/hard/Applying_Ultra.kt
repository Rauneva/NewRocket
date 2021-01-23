package dev.clean.rocket.booster.presentation.battery.hard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd

import dev.clean.rocket.booster.presentation.battery.medium.PowerSaving_Complition
import dev.clean.rocket.booster.R

import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent
import dev.clean.rocket.booster.utils.SubscriptionProvider
import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.ads.nat.NativeProvider
import dev.clean.rocket.booster.utils.ads.nat.NativeSpeaker
import kotlinx.android.synthetic.main.applying_ultra.*
import kotlinx.android.synthetic.main.applying_ultra.i_native
import kotlinx.android.synthetic.main.banner_layout.adView
import kotlinx.android.synthetic.main.native_layout.*

/**
 * Created by intag pc on 2/21/2017.
 */

class Applying_Ultra : Activity() {

    internal var check = 0

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
        setContentView(R.layout.applying_ultra)
        Analytics.openPart(Analytics.OPEN_ES_EXTREME_START)

        val adRequest = AdRequest.Builder().build()
        if(!SubscriptionProvider.hasSubscription()) {
            //bindNative()
            adView.loadAd(adRequest)
        }
        dynamicArcView2.addSeries(SeriesItem.Builder(Color.parseColor("#27282D"))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(false)
                .setLineWidth(12f)
                .build())

        //Create data series track
        val seriesItem1 = SeriesItem.Builder(Color.parseColor("#27282D"))
                .setRange(0f, 100f, 0f)
                .setLineWidth(10f)
                .build()

        val seriesItem2 = SeriesItem.Builder(Color.parseColor("#FFFFFF"))
                .setRange(0f, 100f, 0f)
                .setLineWidth(10f)
                .build()
        //
        //        int series1Index = dynamicArcView2.addSeries(seriesItem1);
        val series1Index2 = dynamicArcView2.addSeries(seriesItem2)

        seriesItem2.addArcSeriesItemListener(object : SeriesItem.SeriesItemListener {
            override fun onSeriesItemAnimationProgress(v: Float, v1: Float) {


                val i = v1.toInt()
                completion.text = "$i%"

                if (v1 >= 10 && v1 < 40) {
                    ist.setTextColor(Color.parseColor("#FFFFFF"))
                    istpic.setImageResource(R.drawable.circle_white)

                } else if (v1 >= 40 && v1 < 65) {
                    sec.setTextColor(Color.parseColor("#FFFFFF"))
                    secpic.setImageResource(R.drawable.circle_white)
                } else if (v1 >= 65 && v1 < 80) {
                    thi.setTextColor(Color.parseColor("#FFFFFF"))
                    thipic.setImageResource(R.drawable.circle_white)
                } else if (v1 >= 80 && v1 < 90) {
                    fou.setTextColor(Color.parseColor("#FFFFFF"))
                    foupic.setImageResource(R.drawable.circle_white)
                } else if (v1 >= 90 && v1 < 100) {
                    fif.setTextColor(Color.parseColor("#FFFFFF"))
                    fifthpic.setImageResource(R.drawable.circle_white)
                }


            }

            override fun onSeriesItemDisplayProgress(v: Float) {

            }
        })


        dynamicArcView2.addEvent(DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(0)
                .setDuration(0)
                .setListener(object : DecoEvent.ExecuteEventListener {
                    override fun onEventStart(decoEvent: DecoEvent) {

                    }

                    override fun onEventEnd(decoEvent: DecoEvent) {

                    }

                })
                .build())

        dynamicArcView2.addEvent(DecoEvent.Builder(100f).setIndex(series1Index2).setDelay(1000).setListener(object : DecoEvent.ExecuteEventListener {
            override fun onEventStart(decoEvent: DecoEvent) {

            }

            override fun onEventEnd(decoEvent: DecoEvent) {
                check = 1
                youDesirePermissionCode(this@Applying_Ultra)
                enablesall()

            }
        }).build())
    }

    fun enablesall() {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter.isEnabled) {
            mBluetoothAdapter.disable()
        }
        val wifiManager = application.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiEnabled = wifiManager.isWifiEnabled
        if (wifiEnabled) {
            wifiManager.isWifiEnabled = false
        }
    }


    override fun onBackPressed() {
        //        super.onBackPressed();
    }


    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && Settings.System.canWrite(this)) {
            PowerSaving_Complition.setAutoOrientationEnabled(applicationContext, false)
            Settings.System.putInt(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 20)
            ContentResolver.setMasterSyncAutomatically(false)
            val i = Intent(this@Applying_Ultra, BatterySaver_Black::class.java)
            startActivity(i)
            finish()
        }
    }


    fun youDesirePermissionCode(context: Activity) {
        val permission: Boolean
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context)
        } else {
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED
        }
        if (permission) {
            //do your code
            PowerSaving_Complition.setAutoOrientationEnabled(applicationContext, false)
            Settings.System.putInt(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 20)
            ContentResolver.setMasterSyncAutomatically(false)
            val i = Intent(this@Applying_Ultra, BatterySaver_Black::class.java)
            startActivity(i)
            finish()

        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:" + context.packageName)
                context.startActivityForResult(intent, 1)
            } else {
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_SETTINGS), 1)
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            PowerSaving_Complition.setAutoOrientationEnabled(applicationContext, false)
            Settings.System.putInt(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 20)
            ContentResolver.setMasterSyncAutomatically(false)
            val i = Intent(this@Applying_Ultra, BatterySaver_Black::class.java)
            startActivity(i)
            finish()
        }

    }


    override fun onResume() {
        super.onResume()
        if (check == 1) {
            try {
                PowerSaving_Complition.setAutoOrientationEnabled(applicationContext, false)
                Settings.System.putInt(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 20)
                ContentResolver.setMasterSyncAutomatically(false)
            } catch (e: Exception) {
                val i = Intent(this@Applying_Ultra, BatterySaver_Black::class.java)
                startActivity(i)
                finish()
            }
            val i = Intent(this@Applying_Ultra, BatterySaver_Black::class.java)
            startActivity(i)
            finish()
        }
    }
}
