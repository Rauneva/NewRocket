package dev.clean.rocket.booster.utils.analytic

import android.util.Log
import com.amplitude.api.Amplitude
import com.amplitude.api.Identify
import com.yandex.metrica.YandexMetrica
import dev.clean.rocket.booster.utils.PreferencesProvider
import org.json.JSONException
import org.json.JSONObject

object Analytics {
    val make_purchase = "make_purchase"
    val make_purchase_where = "where"
    val make_purchase_cat_dialog = "cat_dialog"
    val make_purchase_timer_dialog = "timer"
    val make_purchase_start = "start"
    val make_purchase_inside = "inside"
    val set_ver = "set_ver"

    val AB = "AB"
    val AB_NOTIF = "AB_NOTIF"
    val AB_PREM = "AB_PREM"
    val AB_SYSTEM_NOTIF = "AB_SYSTEM_NOTIF"


    //Firebase
    val AD_LOADED = "AD_LOADED"
    val AD_SHOW = "AD_SHOW"
    val AD_REQUEST = "AD_REQUEST"
    val AD_REQUEST_NOT_LOADED = "AD_REQUEST_NOT_LOADED"
    val AD_REQUEST_SUB = "AD_REQUEST_SUB"
    val AD_FAILED = "AD_FAILED"
    val NEED_SHOW = "NEED_SHOW"

    val from_open = "from"
    val from_scanning_junk_1 = "from_scanning_junk_1"
    val from_scanning_junk_2 = "from_scanning_junk_2"
    val from_cpu_scanner = "from_cpu_scanner"
    val from_main_activity = "from_main_activity"
    val from_noraml_mode = "from_noraml_mode"
    val from_power_saving = "from_power_saving"
    val from_splash = "from_splash"
    val from_booster = "from_booster"

    //FROM WIDGET
    val from_widget = "_after_widget"

    //FROM SYSTEM NOTIF
    val from_sys_bat = "_after_sys_bat"
    val from_sys_delete = "_after_sys_delete"
    val from_sys_install = "_after_sys_install"
    val from_sys_storage = "_after_sys_storage"

    //FROM FCM
    val from_fcm = "_after_fcm"

    val errorString = "errorString"
    val which = "which"
    val first = "first"
    val second = "second"

    val FREQUENCY = "FREQUENCY"
    val CAMPAIGN_ID = "CAMPAIGN_ID"
    val frod_set = "frod_set"

    private val click_boost = "click_boost"
    private val click_bat = "click_bat"
    private val click_temp = "click_temp"
    private val click_clear = "click_clear"
    private val click_other = "click_other"
    private val show_widget = "show_widget"


    ////OPEN PART
    val OPEN_BOOST = "OPEN_BOOST"
    val OPEN_BOOST_START = "OPEN_BOOST_START"

    val OPEN_ES = "OPEN_ES"

    val OPEN_ES_NORMAL_START = "OPEN_ES_NORMAL_START"
    val OPEN_ES_POWERSAVING = "OPEN_ES_POWERSAVING"
    val OPEN_ES_POWERSAVING_START = "OPEN_ES_POWERSAVING_START"

    val OPEN_ES_EXTREME = "OPEN_ES_EXTREME"
    val OPEN_ES_EXTREME_START = "OPEN_ES_EXTREME_START"

    val OPEN_CPU = "OPEN_CPU"
    val OPEN_CPU_START = "OPEN_CPU_START"

    val OPEN_JUNK_CLEAN = "OPEN_JUNK_CLEAN"
    val OPEN_JUNK_CLEAN_START = "OPEN_JUNK_CLEAN_START"

    val SHOW_SYSTEM_NOTIF = "SHOW_SYSTEM_NOTIF"
    val OPEN_SYSTEM_NOTIF = "OPEN_SYSTEM_NOTIF"
    val type_system_notif = "type"
    val FREQUENCY_TAG = "FREQUENCY"
    val TOPIC = "TOPIC"

    val SHOW_FCM_NOTIF = "SHOW_FCM_NOTIF"
    val OPEN_FCM_NOTIF = "OPEN_FCM_NOTIF"

    val twice_month = "month"
    val twice_year = "year"
    val which_twice = "which_twice"

    val BAN_STATE = "BAN_STATE"
    val CLICK_COUNT = "click_count"
    val SHOW_COUNT = "show_count"


    fun setBanVersion(state : String){
        var identify = Identify().setOnce(BAN_STATE, state)
        Amplitude.getInstance().identify(identify)
    }

    fun setClickCount(count : String){
        var identify = Identify().setOnce(CLICK_COUNT, count)
        Amplitude.getInstance().identify(identify)
    }

    fun setShowCount(count : String){
        var identify = Identify().setOnce(SHOW_COUNT, count)
        Amplitude.getInstance().identify(identify)
    }


    fun openPart(part: String) {
        Log.e("LOL", part)
        //Amplitude.getInstance().logEvent(part)
        YandexMetrica.reportEvent(part)
    }


    //YM
    val in_app_ad_impr = "in_app_ad_impr"

    fun setFrodStatus(frequency: String, id: String) {
        var identify = Identify().setOnce(FREQUENCY, frequency).setOnce(CAMPAIGN_ID, id)
        Amplitude.getInstance().identify(identify)
    }

    fun setVersion() {
        Amplitude.getInstance().logEvent(set_ver)
    }

    fun clickBoost() {
        //Amplitude.getInstance().logEvent(click_boost)
    }

    fun clickBat() {
        //Amplitude.getInstance().logEvent(click_bat)
    }

    fun clickTemp() {
        //Amplitude.getInstance().logEvent(click_temp)
    }

    fun clickClean() {
        //Amplitude.getInstance().logEvent(click_clear)
    }

    fun clickOther() {
        //Amplitude.getInstance().logEvent(click_other)
    }

    fun showWidget() {
        Amplitude.getInstance().logEvent(show_widget)
    }

    fun showSystemNotification(type : String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put(type_system_notif, type)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        //Amplitude.getInstance().logEvent(SHOW_SYSTEM_NOTIF, eventProperties)
        Log.e("LOL", "show $type")
    }

    fun openSystemNotification(type : String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put(type_system_notif, type)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        //Amplitude.getInstance().logEvent(OPEN_SYSTEM_NOTIF, eventProperties)
    }


    fun showFCMNotification() {
        Amplitude.getInstance().logEvent(SHOW_FCM_NOTIF)
    }

    fun openFCMNotification() {
        Amplitude.getInstance().logEvent(OPEN_FCM_NOTIF)
    }

    fun makePurchase(where : String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put(make_purchase_where, where)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        Log.e("LOL", "make purchase where $where")
        Log.e("LOL", "$where make purchase ${PreferencesProvider.getPremVersion()}")
        Amplitude.getInstance().logEvent(make_purchase, eventProperties)
    }

    fun makePurchaseTwice(where : String, which : String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put(make_purchase_where, where)
            eventProperties.put(which_twice, which)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        Amplitude.getInstance().logEvent(make_purchase, eventProperties)
    }

    fun setABVersion(premVer : String) {
        Log.e("LOL", "ver $premVer")
        var identify = Identify()
                .set(AB_PREM, premVer)
        Amplitude
                .getInstance()
                .identify(identify)
    }

    fun request() {
        //FirebaseAnalytics.getInstance(MyApp.getInstance()).logEvent(AD_REQUEST, null)
        YandexMetrica.reportEvent(AD_REQUEST)
    }

    fun requestSub() {
        //FirebaseAnalytics.getInstance(MyApp.getInstance()).logEvent(AD_REQUEST_SUB, null)
        YandexMetrica.reportEvent(AD_REQUEST_SUB)
    }

    fun requestNotLoaded(from: String) {
        /*var params = Bundle()
        params.putString(from_open, from)
        FirebaseAnalytics.getInstance(MyApp.getInstance()).logEvent(AD_REQUEST_NOT_LOADED, params)*/

        var params = hashMapOf<String, Any>()
        params.put(from_open, from)
        YandexMetrica.reportEvent(AD_REQUEST_NOT_LOADED, params)
    }

    fun needShow(from: String) {
        /*var params = Bundle()
        params.putString(from_open, from)
        FirebaseAnalytics.getInstance(MyApp.getInstance()).logEvent(NEED_SHOW, params)*/

        var params = hashMapOf<String, Any>()
        params.put(from_open, from)
        YandexMetrica.reportEvent(NEED_SHOW, params)
        Log.e("LOL", "show add with tag -- $from")
    }

    fun show() {
        //FirebaseAnalytics.getInstance(MyApp.getInstance()).logEvent(AD_SHOW, null)
        YandexMetrica.reportEvent(in_app_ad_impr)
        YandexMetrica.reportEvent(AD_SHOW)
    }

    fun loaded() {
        //FirebaseAnalytics.getInstance(MyApp.getInstance()).logEvent(AD_LOADED, null)
        YandexMetrica.reportEvent(AD_LOADED)
    }

    fun failed() {
        //FirebaseAnalytics.getInstance(MyApp.getInstance()).logEvent(AD_FAILED, null)
        YandexMetrica.reportEvent(AD_FAILED)
    }

    fun error(whichString: String) {
        /*var params = Bundle()
        params.putString(which, whichString)
        FirebaseAnalytics.getInstance(MyApp.getInstance()).logEvent(errorString, params)*/

        var params = hashMapOf<String, Any>()
        params.put(which, whichString)
        YandexMetrica.reportEvent(errorString, params)
    }

    fun setFrequency(per: Int) {
        var identify = Identify()
                .set(FREQUENCY_TAG, per.toString())
        Amplitude
                .getInstance()
                .identify(identify)
    }

    fun setTopic(topic: String) {

        var identify = Identify()
                .set(TOPIC, topic)
        Amplitude
                .getInstance()
                .identify(identify)
    }


}