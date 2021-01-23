package dev.mem.rocket.sanya.presentation.booster

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent
import dev.mem.rocket.sanya.Constants.adsShow
import dev.mem.rocket.sanya.MainActivity
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.PreferencesProvider
import dev.mem.rocket.sanya.utils.ads.AdMobFullscreenManager
import dev.mem.rocket.sanya.utils.analytic.ErrorInterceptor
import kotlinx.android.synthetic.main.phone_booster.*
import java.io.RandomAccessFile
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by intag pc on 2/12/2017.
 */

class PhoneBoosterFrag : Fragment(), AdMobFullscreenManager.AdMobFullscreenDelegate {


    internal var mb = 1024 * 1024
    internal var timer: TimerTask? = null
    internal var timer2: TimerTask? = null
    internal var x: Int = 0
    internal var y: Int = 0
    internal var counter = 0
    private var fullscreenManager: AdMobFullscreenManager? = null

    companion object {
        private val data = MutableLiveData<Int>()

        fun setButtonText(text: Int) {
            data.postValue(text)
        }
    }

    val totalRAM: String
        get() {

            var reader: RandomAccessFile? = null
            var load: String? = null
            val twoDecimalForm = DecimalFormat("#.##")
            var totRam = 0.0
            var lastValue = ""
            try {
                try {
                    reader = RandomAccessFile("/proc/meminfo", "r")
                    load = reader.readLine()
                } catch (e: Exception) {

                }

                val p = Pattern.compile("(\\d+)")
                val m = p.matcher(load)
                var value = ""
                while (m.find()) {
                    value = m.group(1)
                }
                try {
                    reader!!.close()
                } catch (e: Exception) {

                }

                totRam = java.lang.Double.parseDouble(value)

                val mb = totRam / 1024.0
                val gb = totRam / 1048576.0
                val tb = totRam / 1073741824.0

                if (tb > 1) {
                    lastValue = twoDecimalForm.format(tb) + " TB"
                } else if (gb > 1) {
                    lastValue = twoDecimalForm.format(gb) + " GB"
                } else if (mb > 1) {
                    lastValue = twoDecimalForm.format(mb) + " MB"
                } else {
                    lastValue = twoDecimalForm.format(totRam) + " KB"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
            }

            return lastValue
        }


    val usedMemorySize: Long
        get() {

            try {
                val mi = ActivityManager.MemoryInfo()
                val activityManager = activity!!.getSystemService(ACTIVITY_SERVICE) as ActivityManager
                activityManager.getMemoryInfo(mi)

                return (mi.totalMem - mi.availMem) / 0x100000L
            } catch (e: Exception) {
                return 200
            }

        }


    private val adManager: AdMobFullscreenManager?
        get() {
            if (fullscreenManager == null) {
                configureManager()
            }
            return fullscreenManager
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.phone_booster, container, false)
        return view
    }

    private val centreeText = MutableLiveData<String>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        data.observe(this, Observer {
            optbutton.setText(it)
        })

        centreeText.observe(this, Observer {
            centree.text = it
        })

        try {
            MainActivity.setInfo(R.string.charge_booster)
            try {
                val totalRAM = totalRAM
                ramperct.setText(String.format("%d%%", Math.round(usedMemorySize / java.lang.Float.parseFloat(totalRAM.substring(0, totalRAM.length - 3)) * 100 / 1024)))
            } catch (e: Exception) {
                ramperct.text = "58%"
            }
            optbutton.setText(R.string.optimize)

            if (PreferencesProvider.getInstance().getString("booster", "1") == "0") {
                optbutton.setText(R.string.optimized)
                centreeText.postValue(Math.round((1 * usedMemorySize).toFloat()).toString() + " MB")
            }
            start()
            optbutton.setOnClickListener {
                if (PreferencesProvider.getInstance().getString("booster", "1") == "1") {
                    Analytics.openPart(Analytics.OPEN_BOOST_START)

                    optimize()

                    PreferencesProvider.getInstance().edit()
                            .putString("booster", "0")
                            .apply()
                } else {
                    @SuppressLint("RestrictedApi") val inflater = getLayoutInflater(arguments)
                    val layout = inflater.inflate(R.layout.my_toast, null)

                    val text = layout.findViewById<View>(R.id.textView1) as TextView
                    text.setText(R.string.phone_is_already_optimized)

                    val toast = Toast(activity)
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70)
                    toast.duration = Toast.LENGTH_LONG
                    toast.view = layout
                    toast.show()
                }
            }
        } catch (e: Exception) {

        }

    }

    fun optimize() {

        val rotate = RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 5000
        rotate.interpolator = LinearInterpolator()

        val image = view?.findViewById<View>(R.id.circularlines) as ImageView

        image.startAnimation(rotate)


        dynamicArcView2.addSeries(SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0f, 100f, 0f)
                .setInterpolator(AccelerateInterpolator())
                .build())

        dynamicArcView2.addSeries(SeriesItem.Builder(Color.parseColor("#F22938"))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(false)
                .setLineWidth(32f)
                .build())

        //Create data series track
        val seriesItem1 = SeriesItem.Builder(Color.parseColor("#F22938"))
                .setRange(0f, 100f, 0f)
                .setLineWidth(32f)
                .build()

        val seriesItem2 = SeriesItem.Builder(Color.parseColor("#2499E0"))
                .setRange(0f, 100f, 0f)
                .setLineWidth(32f)
                .build()
        //
        //        int series1Index = dynamicArcView2.addSeries(seriesItem1);
        val series1Index2 = dynamicArcView2.addSeries(seriesItem2)

        dynamicArcView2.addEvent(DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(300)
                .setDuration(300)
                .setListener(object : DecoEvent.ExecuteEventListener {
                    override fun onEventStart(decoEvent: DecoEvent) {
                        bottom.text = ""
                        top.text = ""
                        centreeText.postValue(getString(R.string.optimiing))
                    }

                    override fun onEventEnd(decoEvent: DecoEvent) {

                    }
                })
                .build())

        dynamicArcView2.addEvent(DecoEvent.Builder(25f).setIndex(series1Index2).setDelay(4000).setListener(object : DecoEvent.ExecuteEventListener {
            override fun onEventStart(decoEvent: DecoEvent) {
                bottom.text = ""
                top.text = ""
                centreeText.postValue(getString(R.string.optimiing))
            }

            override fun onEventEnd(decoEvent: DecoEvent) {
                if (adsShow)
                    adManager!!.completed()
                else {
                    bottom.setText(R.string.found)
                    top.setText(R.string.storage)
                    try {
                        val totalRAM = totalRAM
                        ramperct.setText(String.format("%d%%", Math.round(usedMemorySize / java.lang.Float.parseFloat(totalRAM.substring(0, totalRAM.length - 3)) * 100 / 1024)))

                    } catch (e: Exception) {
                        ramperct.text = "58%"
                    }


                }
            }
        }).build())


        val animation = TranslateAnimation(0.0f, 1000.0f, 0.0f, 0.0f)          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.duration = 5000  // animation duration
        animation.repeatCount = 0
        animation.interpolator = LinearInterpolator()// animation repeat count
        animation.fillAfter = true

        waves.startAnimation(animation)

        val counter = 0
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

                scanlay.visibility = View.VISIBLE
                optimizelay.visibility = View.GONE
                scanning.setText(R.string.scanning)

                killall()
            }

            override fun onAnimationEnd(animation: Animation) {
                scanlay.visibility = View.GONE
                optimizelay.visibility = View.VISIBLE
                optbutton.setText(R.string.optimized)

                centreeText.postValue(Math.round((1 * usedMemorySize).toFloat()).toString() + " MB")

                PreferencesProvider.getInstance().edit()
                        .putString("value", "$usedMemorySize MB")
                        .apply()

                Log.e("used mem", "$usedMemorySize MB")
                Log.e("used mem", totalRAM)

                totalram.text = totalRAM
                usedram.text = "$usedMemorySize MB/ "

                appsfreed.text = totalRAM
                appsused.text = Math.round((1 * usedMemorySize).toFloat()).toString() + " MB/ "

                val ran = Random()
                x = ran.nextInt(40) - 20

                processes.text = (Math.round((1 * usedMemorySize).toFloat()) + x).toString() + ""
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }


    fun start() {
        val t = Timer()
        timer = object : TimerTask() {

            override fun run() {

                try {
                    activity!!.runOnUiThread {
                        counter++
                        centreeText.postValue("$counter MB")
                    }
                } catch (e: Exception) {
                }
            }
        }
        t.schedule(timer, 30, 30)

        val ran2 = Random()
        val proc = ran2.nextInt(60) + 30

        dynamicArcView2.addSeries(SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0f, 100f, 0f)
                .setInterpolator(AccelerateInterpolator())
                .build())

        dynamicArcView2.addSeries(SeriesItem.Builder(Color.parseColor("#F22938"))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(false)
                .setLineWidth(32f)
                .build())

        //Create data series track
        val seriesItem1 = SeriesItem.Builder(Color.parseColor("#F22938"))
                .setRange(0f, 100f, 0f)
                .setLineWidth(32f)
                .build()

        val seriesItem2 = SeriesItem.Builder(Color.parseColor("#2499E0"))
                .setRange(0f, 100f, 0f)
                .setLineWidth(32f)
                .build()

        val series1Index2 = dynamicArcView2.addSeries(seriesItem2)

        dynamicArcView2.addEvent(DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(0)
                .setDuration(600)
                .build())


        dynamicArcView2.addEvent(DecoEvent.Builder(proc.toFloat()).setIndex(series1Index2).setDelay(2000).setListener(object : DecoEvent.ExecuteEventListener {
            override fun onEventStart(decoEvent: DecoEvent) {


            }

            override fun onEventEnd(decoEvent: DecoEvent) {
                t.cancel()
                timer!!.cancel()
                t.purge()

                if (this@PhoneBoosterFrag.context != null) {
                    centreeText.postValue(Math.round((1 * usedMemorySize).toFloat()).toString() + " MB")

                    if (PreferencesProvider.getInstance().getString("booster", "1") == "0") {
                        centreeText.postValue(Math.round((1 * usedMemorySize).toFloat()).toString() + " MB")
                    }
                }

                val t = Timer()
                val t2 = Timer()

                try {
                    timer2 = object : TimerTask() {
                        override fun run() {
                            try {
                                activity!!.runOnUiThread {
                                    centreeText.postValue(Math.round((1 * usedMemorySize).toFloat()).toString() + " MB")
                                    if (PreferencesProvider.getInstance().getString("booster", "1") == "0") {
                                        centreeText.postValue(Math.round((1 * usedMemorySize).toFloat()).toString() + " MB")
                                    }

                                    t2.cancel()
                                    timer2!!.cancel()
                                    t2.purge()
                                    startWidgetWay()
                                }
                            } catch (e: Exception) {

                            }
                        }
                    }
                } catch (e: Exception) {
                }
                t2.schedule(timer2, 100, 100)
            }
        }).build())

        totalram.text = totalRAM
        usedram.text = "$usedMemorySize MB/ "
        appsfreed.text = totalRAM
        appsused.text = Math.round((1 * usedMemorySize).toFloat()).toString() + " MB/ "
        val ran = Random()
        x = ran.nextInt(40) - 20

        processes.text = (Math.round((1 * usedMemorySize).toFloat()) + x).toString() + ""

    }

    private fun startWidgetWay() {
        if (PreferencesProvider.getWidgetWay() == PreferencesProvider.WAY_BOOST) {
            optimize()
            PreferencesProvider.clearWidgetWay()
        }
    }

    fun killall() {
        try {
            val packages: List<ApplicationInfo>
            val pm: PackageManager
            pm = activity!!.packageManager
            packages = pm.getInstalledApplications(0)
            val mActivityManager = activity!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val myPackage = activity!!.applicationContext.packageName
            for (packageInfo in packages) {
                if (packageInfo.flags and ApplicationInfo.FLAG_SYSTEM == 1 || packageInfo.packageName == myPackage) continue
                mActivityManager.killBackgroundProcesses(packageInfo.packageName)
            }
        }catch (ex : java.lang.Exception){
            ErrorInterceptor.trackKillError(ex.localizedMessage)
        }
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            MainActivity.setInfo(R.string.charge_booster)
            Analytics.openPart(Analytics.OPEN_BOOST)

        }
    }

    private fun configureManager() {
        if (fullscreenManager == null && context != null) {
            fullscreenManager = AdMobFullscreenManager(context!!, this)
        } else {
            fullscreenManager!!.reloadAd()
        }
    }

    override fun ADLoaded() {
        if (adManager!!.tryingShowDone) {
            adManager!!.showAdd(Analytics.from_booster)
        }
    }

    override fun ADIsClosed() {
        if (adManager!!.tryingShowDone) {
            bottom.setText(R.string.found)
            top.setText(R.string.storage)
            try {
                val totalRAM = totalRAM
                ramperct.setText(String.format("%d%%", Math.round(usedMemorySize / java.lang.Float.parseFloat(totalRAM.substring(0, totalRAM.length - 3)) * 100 / 1024)))
            } catch (e: Exception) {
                ramperct.text = "58%"
            }

        }
    }

}
