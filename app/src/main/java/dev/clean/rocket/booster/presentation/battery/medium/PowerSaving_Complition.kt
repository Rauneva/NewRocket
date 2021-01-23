package dev.clean.rocket.booster.presentation.battery.medium

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
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd

import dev.clean.rocket.booster.utils.ads.AdMobFullscreenManager
import dev.clean.rocket.booster.R

import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent

import dev.clean.rocket.booster.Constants.adsShow
import dev.clean.rocket.booster.utils.SubscriptionProvider
import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.ads.nat.NativeProvider
import dev.clean.rocket.booster.utils.ads.nat.NativeSpeaker
import kotlinx.android.synthetic.main.banner_layout.adView
import kotlinx.android.synthetic.main.native_layout.*
import kotlinx.android.synthetic.main.powersaving_completion.*
import kotlinx.android.synthetic.main.powersaving_completion.i_native

/**
 * Created by intag pc on 2/22/2017.
 */

class PowerSaving_Complition : Activity(), AdMobFullscreenManager.AdMobFullscreenDelegate {

    internal var check = 0
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
        setContentView(R.layout.powersaving_completion)
        Analytics.openPart(Analytics.OPEN_ES_POWERSAVING_START)


        val adRequest = AdRequest.Builder().build()
        if(!SubscriptionProvider.hasSubscription()) {
            //bindNative()
            adView!!.loadAd(adRequest)
        }

        dynamicArcView2.addSeries(SeriesItem.Builder(Color.parseColor("#27282D"))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(false)
                .setLineWidth(12f)
                .build())

        val seriesItem1 = SeriesItem.Builder(Color.parseColor("#27282D"))
                .setRange(0f, 100f, 0f)
                .setLineWidth(10f)
                .build()

        val seriesItem2 = SeriesItem.Builder(Color.parseColor("#FFFFFF"))
                .setRange(0f, 100f, 0f)
                .setLineWidth(10f)
                .build()

        val series1Index2 = dynamicArcView2.addSeries(seriesItem2)

        seriesItem2.addArcSeriesItemListener(object : SeriesItem.SeriesItemListener {
            override fun onSeriesItemAnimationProgress(v: Float, v1: Float) {


                val i = v1.toInt()
                completion.text = "$i%"

                if (v1 >= 10 && v1 < 50) {
                    ist.setTextColor(Color.parseColor("#FFFFFF"))
                    istpic.setImageResource(R.drawable.circle_white)

                } else if (v1 >= 50 && v1 < 75) {
                    sec.setTextColor(Color.parseColor("#FFFFFF"))
                    secpic.setImageResource(R.drawable.circle_white)
                } else if (v1 >= 75 && v1 < 90) {
                    thi.setTextColor(Color.parseColor("#FFFFFF"))
                    thipic.setImageResource(R.drawable.circle_white)
                } else if (v1 >= 90 && v1 <= 100) {
                    fou.setTextColor(Color.parseColor("#FFFFFF"))
                    foupic.setImageResource(R.drawable.circle_white)
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

                if (adsShow) {
                    adManager!!.completed()
                } else {
                    youDesirePermissionCode(this@PowerSaving_Complition)
                    closesall()
                    check = 1
                }
            }
        }).build())
    }

    fun closesall() {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter.isEnabled) {
            mBluetoothAdapter.disable()
        }
        ContentResolver.setMasterSyncAutomatically(false)
    }


    override fun onBackPressed() {
        //        super.onBackPressed();
    }


    fun youDesirePermissionCode(context: Activity) {
        val permission: Boolean
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context)
        } else {
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED
        }
        if (permission) {
            Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 30)
            setAutoOrientationEnabled(context, false)

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

    //
    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && Settings.System.canWrite(this)) {
            Settings.System.putInt(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 30)
            setAutoOrientationEnabled(this, false)
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(applicationContext, "onRequestPermissionsResult", Toast.LENGTH_LONG).show()
            Settings.System.putInt(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 30)
            setAutoOrientationEnabled(this, false)
            finish()
        }

    }


    override fun onResume() {
        super.onResume()
        if (check == 1) {
            try {
                Settings.System.putInt(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 30)
                setAutoOrientationEnabled(this, false)
            } catch (e: Exception) {
                finish()
            }
            finish()
        }
    }


    private fun configureManager() {
        if (fullscreenManager == null) {
            fullscreenManager = AdMobFullscreenManager(this, this)
        } else {
            fullscreenManager!!.reloadAd()
        }
    }

    override fun ADLoaded() {
        if (adManager!!.tryingShowDone) {
            adManager!!.showAdd(Analytics.from_power_saving)
        }
    }

    override fun ADIsClosed() {
        if (adManager!!.tryingShowDone) {
            youDesirePermissionCode(this@PowerSaving_Complition)
            closesall()
            check = 1
        }
    }

    companion object {
        fun setAutoOrientationEnabled(context: Context, enabled: Boolean) {
            Settings.System.putInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, if (enabled) 1 else 0)
        }
    }
}
