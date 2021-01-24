package dev.clean.rocket.booster


import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.Preference
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.firebase.messaging.FirebaseMessaging
import dev.clean.rocket.booster.AAA.AlarmBroadCastReceiver
import dev.clean.rocket.booster.Config.Companion.TOPIC_NEW
import dev.clean.rocket.booster.Constants.adsShow
import dev.clean.rocket.booster.Volume.MyPagerAdapter
import dev.clean.rocket.booster.presentation.booster.PhoneBoosterFrag
import dev.clean.rocket.booster.utils.PreferencesProvider
import dev.clean.rocket.booster.utils.SubscriptionProvider
import dev.clean.rocket.booster.utils.ads.nat.NativeProvider
import dev.clean.rocket.booster.utils.ads.nat.NativeSpeaker
import dev.clean.rocket.booster.utils.ads.nat.TestNativeProvider
import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.notifications.AlarmReceiver
import dev.clean.rocket.booster.utils.notifications.fcm.FBWork
import dev.clean.rocket.booster.utils.notifications.interactive.ProcessMainClass
import dev.clean.rocket.booster.utils.notifications.interactive.brs.RestartBR
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_backs.*
import kotlinx.android.synthetic.main.native_layout.*
import kotlinx.android.synthetic.main.test_banner_layout.*
import java.util.*

class MainActivity : AppCompatActivity(), Preference.OnPreferenceClickListener {

    //todo Prefs
    internal var consent: Boolean = false
    var isShowedAD = false
    lateinit var bsBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var serviceIntent: Intent


    private fun setNative() {
        ad_view.mediaView = ad_media
        ad_view.headlineView = ad_headline
        ad_view.bodyView = ad_body
        ad_view.callToActionView = ad_call_to_action
        ad_view.iconView = ad_icon
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        data.observe(this, Observer {
            name.setText(it)
        })

        /*      /topics/news          */
        FirebaseMessaging
                .getInstance()
                .subscribeToTopic(Config.TOPIC_OLD)
                .addOnSuccessListener {
                    Analytics.setTopic(Config.TOPIC_OLD)
                }
        FBWork.getFCMToken()



        if (!SubscriptionProvider.hasSubscription()) {
            bindBannner()
            adView_bs.visibility = View.VISIBLE
            val adRequest = AdRequest.Builder().build()
            adView_bs!!.loadAd(adRequest)
        }

        consent = intent.getBooleanExtra(CONSENT, false)

        val randomNum = 6 + (Math.random() * 18).toInt()

        val randomNum2 = 6 + (Math.random() * 18).toInt()

        val myIntent = Intent(this@MainActivity, AlarmBroadCastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, myIntent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val firingCal = Calendar.getInstance()
        val currentCal = Calendar.getInstance()

        firingCal.set(Calendar.HOUR_OF_DAY, randomNum) // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, randomNum2) // Particular minute
        firingCal.set(Calendar.SECOND, 0) // particular second

        var intendedTime = firingCal.timeInMillis
        val currentTime = currentCal.timeInMillis

        if (intendedTime >= currentTime) {
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent)
        } else {
            firingCal.add(Calendar.DAY_OF_MONTH, 1)
            intendedTime = firingCal.timeInMillis

            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent)
        }

        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            //Do your own error handling here

            if (oldHandler != null)
                oldHandler.uncaughtException(
                        paramThread,
                        paramThrowable
                )
            else
                System.exit(2)
        }

        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.phonebooster))
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.battery_saver))
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.cooler))
        tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.cleaner))
        Log.i("adsShow", adsShow.toString())
        if (!SubscriptionProvider.hasSubscription()) {
            tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ads))
        }
        tab_layout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = MyPagerAdapter(supportFragmentManager, tab_layout.tabCount)
        pager.adapter = adapter

        pager.offscreenPageLimit = 4
        //        viewPager.setCurrentItem(4);

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        if (intent.getBooleanExtra("fromService", false)) {
            pager.currentItem = 3
        }
        pager.currentItem = intent.getIntExtra("frag", 0)

        bsBehavior = BottomSheetBehavior.from(llBottomSheet)
        cvBS.setBackgroundResource(R.drawable.shape_bs_card)

        btnExitYes.setOnClickListener {
            finishAffinity()
        }

        btnExitNo.setOnClickListener {
            bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bindWidgetOpen()
        setWidget()
        bindSystemNotifWay()
    }

    private fun bindSystemNotifWay() {
        when (PreferencesProvider.getSystemNotifWay()) {
            PreferencesProvider.SYSTEM_NOTIF_BAT -> pager.currentItem = 1
            PreferencesProvider.SYSTEM_NOTIF_STORAGE -> pager.currentItem = 3
            PreferencesProvider.SYSTEM_NOTIF_DELETE -> pager.currentItem = 3
            PreferencesProvider.SYSTEM_NOTIF_INSTALL -> pager.currentItem = 3
        }
    }


    private fun setWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RestartBR.scheduleJob(applicationContext)
        } else {
            var pmc = ProcessMainClass()
            pmc.launchService(applicationContext)
        }
    }


    private fun bindWidgetOpen() {
        var way = PreferencesProvider.getWidgetWay()
        if (way != PreferencesProvider.WAY_NONE) {
            when (way) {
                PreferencesProvider.WAY_BOOST -> {
                    pager.currentItem = 0
                }
                PreferencesProvider.WAY_BATTERY -> {
                    pager.currentItem = 1
                    PreferencesProvider.clearWidgetWay()
                }
                PreferencesProvider.WAY_TEMP -> {
                    pager.currentItem = 2
                    PreferencesProvider.clearWidgetWay()
                }
                PreferencesProvider.WAY_CLEAR -> {
                    pager.currentItem = 3
                    PreferencesProvider.clearWidgetWay()
                }
                PreferencesProvider.WAY_OTHER -> if (!SubscriptionProvider.hasSubscription()) {
                    pager.currentItem = 4
                    PreferencesProvider.clearWidgetWay()
                }
            }
        }
    }


    private fun bindBannner() {
        i_banner_test.visibility = View.VISIBLE
        val adRequest = AdRequest.Builder().build()
        adView_test!!.loadAd(adRequest)
    }

    private fun bindBSNative() {
        if (!SubscriptionProvider.hasSubscription()) {
            NativeProvider.observeOnNativeList(object : NativeSpeaker {
                override fun loadFin(nativeAd: UnifiedNativeAd) {
                    flNativeBS.visibility = View.VISIBLE
                    setNativeBS()
                    bindAdViewBS(nativeAd)
                }
            })
        } else {
            flNativeBS.visibility = View.GONE
        }
    }

    private fun setNativeBS() {
        ad_view_bs.mediaView = ad_media_bs
        ad_view_bs.headlineView = ad_headline_bs
        ad_view_bs.bodyView = ad_body_bs
        ad_view_bs.callToActionView = ad_call_to_action_bs
        ad_view_bs.iconView = ad_icon_bs
    }

    private fun bindAdViewBS(nativeAd: UnifiedNativeAd) {
        (ad_view_bs.headlineView as TextView).text = nativeAd.headline
        (ad_view_bs.bodyView as TextView).text = nativeAd.body
        (ad_view_bs.callToActionView as Button).text = nativeAd.callToAction
        val icon = nativeAd.icon
        if (icon == null) {
            ad_view_bs.iconView.visibility = View.INVISIBLE
        } else {
            (ad_view_bs.iconView as ImageView).setImageDrawable(icon.drawable)
            ad_view_bs.iconView.visibility = View.VISIBLE
        }
        ad_view_bs.setNativeAd(nativeAd)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        if (bsBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bsBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            bsBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun bindNative() {
        if (!SubscriptionProvider.hasSubscription()) {
            TestNativeProvider.observeOnNativeList(object : NativeSpeaker {
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


    override fun onResume() {
        super.onResume()
        PreferencesProvider.getInstance().edit()
                .putString("booster", "1")
                .apply()
        try {
            PhoneBoosterFrag.setButtonText(R.string.optimize)
        } catch (e: Exception) {
        }
    }

    private fun setNotification() {
        PreferencesProvider.getInstance().edit().putString("state_Head", resources.getString(R.string.notif_head))
                .putString("state_Body", resources.getString(R.string.notif_body))
                .apply()

        val calendar = Calendar.getInstance()
        val now = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 18)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        //if user sets the alarm after their preferred time has already passed that day
        if (now.after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(this, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val alarmManager = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)


        Log.d("TIME", "setNot")
    }


    override fun onDestroy() {
        super.onDestroy()

        PreferencesProvider.getInstance().edit()
                .putString("button1", "0")
                .putString("button2", "0")
                .putString("button3", "0")
                .putString("button4", "0")
                .putString("button5", "0")
                .apply()
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        return false
    }

    inner class MyException : Exception()// special exception code goes here


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    companion object {
        private val CONSENT = "consent"

        fun getIntent(context: Context, consent: Boolean): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(CONSENT, consent)
            return intent
        }

        private val data = MutableLiveData<Int>()

        fun setInfo(int: Int) {
            data.postValue(int)
        }
    }

}
