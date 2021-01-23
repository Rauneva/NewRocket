package dev.mem.rocket.sanya

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.amplitude.api.Amplitude
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.clean.emptyrocket.Scanning_Junk
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dev.mem.rocket.sanya.presentation.prem.ParentPremActivity
import dev.mem.rocket.sanya.presentation.battery.low.Noraml_Mode
import dev.mem.rocket.sanya.presentation.cpu.Cpu_Scanner
import dev.mem.rocket.sanya.presentation.prem.ab.dec.*
import dev.mem.rocket.sanya.presentation.privacy.PrivacyPoliceActivity
import dev.mem.rocket.sanya.utils.ABConfig
import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.PreferencesProvider
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import dev.mem.rocket.sanya.utils.ads.AdMobFullscreenManager
import dev.mem.rocket.sanya.utils.analytic.ErrorInterceptor
import dev.mem.rocket.sanya.utils.notifications.fcm.FCMConfig
import dev.mem.rocket.sanya.utils.notifications.interactive.SystemNotifConfig
import kotlinx.android.synthetic.main.flash_screen.*
import java.util.*
import java.util.concurrent.TimeUnit


class SplashActivity : AppCompatActivity(), AdMobFullscreenManager.AdMobFullscreenDelegate {
    private var fullscreenManager: AdMobFullscreenManager? = null
    private var privatePoliceBtn: Button? = null
    private var privacyPoliceClicked = false

    var MAX = 2
    var counter = 0

    var isProcessOpenNextScreen = false


    private val adManager: AdMobFullscreenManager?
        get() {
            if (fullscreenManager == null) {
                configureManager()
            }
            return fullscreenManager
        }

    private fun postNext(count: Int, tag : String) {
        Log.e("LOL", "postNext -- $tag")
        counter += count
        if (counter >= MAX) {
            goNext()
        }
    }

    private fun bindTest() {
        val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.setDefaults(R.xml.default_config)

        firebaseRemoteConfig.fetch(3600).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseRemoteConfig.activateFetched()

            } else {

            }
            setABTestConfig(firebaseRemoteConfig.getString(ABConfig.PREMIUM_REQUEST_STRING))
        }
    }

    private fun setABTestConfig(premVer: String) {
        var defPremVer = ABConfig.A_VER
        try {
            if (premVer != "") {
                defPremVer = premVer
            }
        } catch (ex: java.lang.Exception) {
            ErrorInterceptor.trackRemoteError(ex.localizedMessage)
        }
        SubscriptionProvider.startGettingPrice(0)
        PreferencesProvider.setPremVersion(defPremVer)

        Analytics.setABVersion(defPremVer)
        Analytics.setVersion()

        postNext(1, "setABTestConfig")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        Log.e("LOL", "hour $currentHour")
        PreferencesProvider.getInstance().edit().putString("junk", "1").commit()
        trackSystemNotifAnalytics()
        trackFCMAnalytic()
        bindTest()
        setContentView(R.layout.flash_screen)
        signInAndInitUser()
        privacyPoliceClicked = false
        privatePoliceBtn = findViewById(R.id.privacyPoliceBtn)
        val htmlTaggedString = "<u>" + getString(R.string.privacy_police) + "</u>"
        val textSpan = android.text.Html.fromHtml(htmlTaggedString)
        privatePoliceBtn!!.text = textSpan
        privatePoliceBtn!!.setOnClickListener {
            privacyPoliceClicked = true
            startActivity(Intent(this@SplashActivity, PrivacyPoliceActivity::class.java))
        }
        if (Config.FOR_TEST) {
            privacyPoliceBtn.visibility = View.GONE
        }

        Thread {
            TimeUnit.SECONDS.sleep(4)
            postNext(1, "sleep")
        }.start()
    }

    private fun trackFCMAnalytic() {
        if (intent?.getStringExtra(FCMConfig.FCM_DATA_TAG) == FCMConfig.FCM_DATA) {
            Analytics.openFCMNotification()
            PreferencesProvider.setFCMNotifWay()
        }else{
            PreferencesProvider.clearFCMNotifWay()
        }
    }

    private fun trackSystemNotifAnalytics() {
        if (intent?.getStringExtra(SystemNotifConfig.SYSTEM_NOTIF_TAG) != null
                && intent?.getStringExtra(SystemNotifConfig.SYSTEM_NOTIF_TAG) != "") {
            when (intent.getStringExtra(SystemNotifConfig.SYSTEM_NOTIF_TAG)) {
                SystemNotifConfig.BAT_TAG -> {
                    PreferencesProvider.setSystemNotifWay(PreferencesProvider.SYSTEM_NOTIF_BAT)
                    Analytics.openSystemNotification(PreferencesProvider.SYSTEM_NOTIF_BAT)
                }

                SystemNotifConfig.DELETE_TAG -> {
                    PreferencesProvider.setSystemNotifWay(PreferencesProvider.SYSTEM_NOTIF_DELETE)
                    Analytics.openSystemNotification(PreferencesProvider.SYSTEM_NOTIF_DELETE)
                }

                SystemNotifConfig.INSTALL_TAG -> {
                    PreferencesProvider.setSystemNotifWay(PreferencesProvider.SYSTEM_NOTIF_INSTALL)
                    Analytics.openSystemNotification(PreferencesProvider.SYSTEM_NOTIF_INSTALL)
                }

                SystemNotifConfig.STORAGE_TAG -> {
                    PreferencesProvider.setSystemNotifWay(PreferencesProvider.SYSTEM_NOTIF_STORAGE)
                    Analytics.openSystemNotification(PreferencesProvider.SYSTEM_NOTIF_STORAGE)
                }
            }
        } else {
            PreferencesProvider.setSystemNotifWay(PreferencesProvider.SYSTEM_NOTIF_EMPTY)
        }
    }

    private fun signInAndInitUser() {
        try {
            trackUser()
        } catch (ex: java.lang.Exception) {
            ErrorInterceptor.trackInstallRefBug(ex.localizedMessage)
        }

    }

    private fun trackUser() {
        var client = InstallReferrerClient.newBuilder(this).build()
        client.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> sendAnal(client.installReferrer.installReferrer)
                    InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR -> sendAnal("DEVELOPER_ERROR")
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> sendAnal("FEATURE_NOT_SUPPORTED")
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED -> sendAnal("SERVICE_DISCONNECTED")
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> sendAnal("SERVICE_UNAVAILABLE")
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                sendAnal("onInstallReferrerServiceDisconnected")
            }
        })
    }

    private fun sendAnal(s: String) {
        val clickId = getClickId(s)
        saveClickId(clickId)
        var mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        var bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CAMPAIGN, clickId)
        bundle.putString(FirebaseAnalytics.Param.MEDIUM, clickId)
        bundle.putString(FirebaseAnalytics.Param.SOURCE, clickId)
        bundle.putString(FirebaseAnalytics.Param.ACLID, clickId)
        bundle.putString(FirebaseAnalytics.Param.CONTENT, clickId)
        bundle.putString(FirebaseAnalytics.Param.CP1, clickId)
        bundle.putString(FirebaseAnalytics.Param.VALUE, clickId)
        mFirebaseAnalytics!!.logEvent("traffic_id", bundle)
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.CAMPAIGN_DETAILS, bundle)
    }

    private fun saveClickId(clickId: String) {
        try {
            PreferencesProvider.setId(clickId)
        } catch (ex: Exception) {

        }
    }

    private fun getClickId(s: String): String {
        var id = s
        if (s.contains("&")) {
            id = s.split("&")[0]
        }
        return id
    }

    private fun configureManager() {
        if (fullscreenManager == null) {
            fullscreenManager = AdMobFullscreenManager(this, this)
        } else {
            fullscreenManager!!.reloadAd()
        }
    }

    override fun ADLoaded() {
        MAX++
        if (adManager!!.tryingShowDone && PreferencesProvider.getWidgetWay() == PreferencesProvider.WAY_NONE) {
            adManager!!.showAdd(Analytics.from_splash)
        } else {
            postNext(2, "adLoadedFailed")
        }
    }

    override fun ADIsClosed() {
        if (adManager!!.tryingShowDone) {
            postNext(2, "ADIsClosed")
        }
    }

    override fun onResume() {
        privacyPoliceClicked = false
        adManager!!.completed()
        super.onResume()
        adManager!!.completed()
    }


    private fun goNext() {
        if (!privacyPoliceClicked && !isProcessOpenNextScreen) {
            isProcessOpenNextScreen = true
            var i: Intent
            i = if (PreferencesProvider.getWidgetWay() != PreferencesProvider.WAY_NONE) {
                getWayIntent()
            } else if (PreferencesProvider.getFirstEnterStatus()!! == PreferencesProvider.FIRST_ENTER_ALREADY) {
                when (PreferencesProvider.getPremVersion()!!) {
                    ABConfig.A_VER -> Intent(applicationContext, ParentPremActivity::class.java)
                    ABConfig.B_VER -> Intent(applicationContext, BottomActivity::class.java)
                    ABConfig.C_VER -> Intent(applicationContext, FlagActivity::class.java)
                    ABConfig.D_VER -> Intent(applicationContext, LockActivity::class.java)
                    ABConfig.E_VER -> Intent(applicationContext, ShieldActivity::class.java)
                    ABConfig.F_VER -> Intent(applicationContext, WhiteActivity::class.java)
                    else -> {
                        ErrorInterceptor.trackDefaultPrem("def")
                        Intent(applicationContext, ParentPremActivity::class.java)
                    }
                }
            } else {
                Intent(applicationContext, MainActivity::class.java)
            }
            startActivity(i)
            finish()
        }
    }

    private fun getWayIntent(): Intent {
        var way = PreferencesProvider.getWidgetWay()
        return when (way) {
            PreferencesProvider.WAY_BOOST -> Intent(this, MainActivity::class.java)
            PreferencesProvider.WAY_BATTERY -> Intent(this, Noraml_Mode::class.java)
            PreferencesProvider.WAY_TEMP -> Intent(this, Cpu_Scanner::class.java)
            PreferencesProvider.WAY_CLEAR -> Intent(this, Scanning_Junk::class.java)
            PreferencesProvider.WAY_OTHER -> Intent(this, MainActivity::class.java)
            else -> Intent(this, MainActivity::class.java)
        }
    }

}