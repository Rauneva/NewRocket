package dev.mem.rocket.sanya.presentation.cpu

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


import dev.mem.rocket.sanya.OOP.ApplicationsClass
import dev.mem.rocket.sanya.MainActivity
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.utils.analytic.Analytics

import java.io.File
import java.util.ArrayList

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.cpu_cooler.*

/**
 * Created by intag pc on 2/12/2017.
 */

class CPUCoolerFrag : Fragment() {

    internal var temp: Float = 0.toFloat()
    lateinit var mAdapter: RecyclerAdapter
    internal var apps2: List<ApplicationsClass> = listOf()
    internal var check = 0
    private val imageResource = MutableLiveData<Int>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageResource.observe(this, Observer {
            tempimg.setImageResource(it)
        })
    }

    internal var batteryReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            try {
                val level = intent.getIntExtra("level", 0)
                temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0).toFloat() / 10

                batterytemp.text = "$temp°C"

                if (temp >= 20.0) {

                    apps = ArrayList()
                    apps2 = ArrayList()
                    imageResource.postValue(R.drawable.red_cooler)
                    showmain.setText(R.string.overheated)
                    showmain.setTextColor(Color.parseColor("#F22938"))
                    showsec.setText(R.string.application_class_problem)
                    nooverheating.text = ""

                    coolbutton.setText(R.string.cool_down)
                    coolbutton.setOnClickListener {
                        val i = Intent(activity, Cpu_Scanner::class.java)
                        startActivity(i)

                        val handler = Handler()
                        handler.postDelayed({
                            nooverheating.setText(R.string.currently_no_app_causing_overheating)
                            showmain.setText(R.string.normal)
                            showmain.setTextColor(Color.parseColor("#24D149"))
                            showsec.setText(R.string.cpu_temperature_is_good)
                            showsec.setTextColor(Color.parseColor("#24D149"))
                            coolbutton.setText(R.string.cooled)
                            imageResource.postValue(R.drawable.blue_cooler)
                            batterytemp.text = "25.3" + "°C"
                            recycler_view.adapter = null
                        }, 2000)


                        coolbutton.setOnClickListener {
                            val inflater = layoutInflater
                            val layout = inflater.inflate(R.layout.my_toast, null)

                            val text = layout.findViewById<View>(R.id.textView1) as TextView
                            text.setText(R.string.cpu_temperature_is_already_normal)

                            val toast = Toast(activity)
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70)
                            toast.duration = Toast.LENGTH_LONG
                            toast.view = layout
                            toast.show()
                        }
                    }

                    if (Build.VERSION.SDK_INT < 23) {
                        showsec.setTextAppearance(context, android.R.style.TextAppearance_Medium)
                        showsec.setTextColor(Color.parseColor("#F22938"))

                    } else {
                        showsec.setTextAppearance(android.R.style.TextAppearance_Small)
                        showsec.setTextColor(Color.parseColor("#F22938"))
                    }
                    recycler_view.itemAnimator = SlideInLeftAnimator()
                    recycler_view.itemAnimator!!.addDuration = 10000

                    mAdapter = RecyclerAdapter(apps)
                    val mLayoutManager = LinearLayoutManager(activity!!.applicationContext, LinearLayoutManager.HORIZONTAL, false)
                    recycler_view.layoutManager = mLayoutManager
                    recycler_view.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
                    recycler_view.computeHorizontalScrollExtent()
                    recycler_view.adapter = mAdapter
                    getAllICONS()
                }
            } catch (e: Exception) {

            }


        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.cpu_cooler, container, false)
        try {
            activity!!.registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            nooverheating.setText(R.string.currently_no_app_causing_overheating)
            showmain.setText(R.string.normal)
            showsec.setText(R.string.cpu_temperature_is_good)

            coolbutton.setText(R.string.cooled)
            coolbutton.setOnClickListener {
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.my_toast, null)


                val text = layout.findViewById<View>(R.id.textView1) as TextView
                text.setText(R.string.cpu_temperature_is_already_normal)

                val toast = Toast(activity)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70)
                toast.duration = Toast.LENGTH_LONG
                toast.view = layout
                toast.show()
            }

            imageResource.postValue(R.drawable.blue_cooler)
        } catch (e: Exception) {

        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            activity!!.unregisterReceiver(batteryReceiver)
        } catch (e: Exception) {

        }
    }


    fun getAllICONS() {
        val pm = activity!!.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        if (packages != null) {
            for (k in packages.indices) {
                // String pkgName = app.getPackageName();
                val packageName = packages[k].packageName
                Log.e("packageName-->", "" + packageName)

                if (packageName != "app.magic.cleaner.booost") {
                    var ico: Drawable? = null
                    try {
                        val pName = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)) as String
                        val file = File(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA).publicSourceDir)
                        val size = file.length()
                        val a = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                        ico = activity!!.packageManager.getApplicationIcon(packages[k].packageName)
                        val app = ApplicationsClass((size / 1000000 + 20).toString() + "MB", ico)
                        activity!!.packageManager
                        Log.e("ico-->", "" + ico!!)

                        if (a.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                            if (check <= 5) {
                                check++
                                apps.add(app)
                            } else {
                                activity!!.unregisterReceiver(batteryReceiver)
                                break
                            }

                        }
                        mAdapter.notifyDataSetChanged()

                    } catch (e: PackageManager.NameNotFoundException) {

                    }
                }
            }

        }

        if (apps.size > 1) {
            mAdapter = RecyclerAdapter(apps)
            mAdapter.notifyDataSetChanged()
        }
    }


    override fun getUserVisibleHint(): Boolean {
        MainActivity.setInfo(R.string.cpu_cooler)
        return userVisibleHint
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            MainActivity.setInfo(R.string.cpu_cooler)
            Analytics.openPart(Analytics.OPEN_CPU)
        }
    }

    companion object {
        var apps: MutableList<ApplicationsClass> = mutableListOf()
    }
}
