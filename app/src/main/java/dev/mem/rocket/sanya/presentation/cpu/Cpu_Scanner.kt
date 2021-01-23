package dev.mem.rocket.sanya.presentation.cpu

import android.animation.Animator
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd

import dev.mem.rocket.sanya.utils.ads.AdMobFullscreenManager
import dev.mem.rocket.sanya.OOP.ApplicationsClass
import dev.mem.rocket.sanya.R

import java.util.ArrayList

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

import dev.mem.rocket.sanya.Constants.adsShow
import dev.mem.rocket.sanya.MainActivity
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.ads.nat.NativeProvider
import dev.mem.rocket.sanya.utils.ads.nat.NativeSpeaker
import kotlinx.android.synthetic.main.banner_layout.adView
import kotlinx.android.synthetic.main.cpu_scanner.*
import kotlinx.android.synthetic.main.cpu_scanner.i_native
import kotlinx.android.synthetic.main.native_layout.*


/**
 * Created by intag pc on 2/25/2017.
 */

class Cpu_Scanner : Activity(), AdMobFullscreenManager.AdMobFullscreenDelegate {

    lateinit var mAdapter: Scan_Cpu_Apps
    internal var app: List<ApplicationsClass>? = null
    internal var pm: PackageManager? = null
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
        setContentView(R.layout.cpu_scanner)
        Analytics.openPart(Analytics.OPEN_CPU_START)
        val adRequest = AdRequest.Builder().build()
        if (!SubscriptionProvider.hasSubscription()) {
            adView!!.loadAd(adRequest)
            //bindNative()
        }

        app = ArrayList()

        lavCpuCleaner.playAnimation()

        lavComplete.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (adsShow) {
                    adManager!!.completed()
                } else {
                    val handler6 = Handler()
                    handler6.postDelayed({
                        fin()
                    }, 1000)
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        val animation = TranslateAnimation(0.0f, 1000.0f, 0.0f, 0.0f)          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.duration = 5000  // animation duration
        animation.repeatCount = 0
        animation.interpolator = LinearInterpolator()// animation repeat count
        animation.fillAfter = true

        heart.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                heart.setImageResource(0)
                heart.setBackgroundResource(0)

                lavCpuCleaner.cancelAnimation()
                lavCpuCleaner.visibility = View.INVISIBLE

                lavComplete.visibility = View.VISIBLE
                lavComplete.playAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })



        recycler_view.itemAnimator = SlideInLeftAnimator()


        mAdapter = Scan_Cpu_Apps(CPUCoolerFrag.apps)
        val mLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.layoutManager = mLayoutManager
        recycler_view.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
        recycler_view.computeHorizontalScrollExtent()
        recycler_view.adapter = mAdapter
        killall()
        try {
            val handler1 = Handler()
            handler1.postDelayed({ add(getString(R.string.cpu_scanner1), 0) }, 0)

            val handler2 = Handler()
            handler2.postDelayed({
                remove(0)
                add(getString(R.string.cpu_scanner2), 1)
            }, 900)

            val handler3 = Handler()
            handler3.postDelayed({
                remove(0)
                add(getString(R.string.cpu_scanner3), 2)
            }, 1800)

            val handler4 = Handler()
            handler4.postDelayed({
                remove(0)
                add(getString(R.string.cpu_scanner4), 3)
            }, 2700)

            val handler5 = Handler()
            handler5.postDelayed({
                remove(0)
                add(getString(R.string.cpu_scanner4), 4)
            }, 3700)
            //
            val handler6 = Handler()
            handler6.postDelayed({
                remove(0)
                add(getString(R.string.cpu_scanner4), 5)
            }, 4400)

            val handler7 = Handler()
            handler7.postDelayed({
                add(getString(R.string.cpu_scanner4), 6)
                remove(0)

                heart.setImageResource(0)
                heart.setBackgroundResource(0)


                rel.visibility = View.GONE

                cpucooler.setText(R.string.cooled_CPU_to)
            }, 5500)
        } catch (e: Exception) {

        }

    }


    fun add(text: String, position: Int) {
        try {
            mAdapter.notifyItemInserted(position)
        } catch (e: Exception) {

        }

    }


    fun remove(position: Int) {
        mAdapter.notifyItemRemoved(position)
        try {
            CPUCoolerFrag.apps.removeAt(position)
        } catch (e: Exception) {

        }

    }

    fun killall() {
        val packages: List<ApplicationInfo>
        val pm: PackageManager
        pm = applicationContext.packageManager
        packages = pm.getInstalledApplications(0)
        val mActivityManager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myPackage = applicationContext.applicationContext.packageName
        for (packageInfo in packages) {
            if (packageInfo.flags and ApplicationInfo.FLAG_SYSTEM == 1 || packageInfo.packageName == myPackage) continue
            mActivityManager.killBackgroundProcesses(packageInfo.packageName)
        }
    }

    private fun fin() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
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
        if (adManager!!.tryingShowDone) {
            adManager!!.showAdd(Analytics.from_cpu_scanner)
        }
    }

    override fun ADIsClosed() {
        if (adManager!!.tryingShowDone) {
            val handler6 = Handler()
            handler6.postDelayed({
                fin()
            }, 1000)
        }
    }


}
