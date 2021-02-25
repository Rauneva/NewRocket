package dev.clean.rocket.booster.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import dev.clean.rocket.booster.MyApp

object PreferencesProvider {

    private const val PRICE_TAG = "PRICE_TAG"
    private const val DEF_PRICE = "4 USD"
    private const val FIRST_ENTER_TAG_NEW = "FIRST_ENTER_TAG_NEW"
    const val FIRST_ENTER_ALREADY = 0
    const val FIRST_ENTER_READY = 1

    private const val CONFIG_RAN = "CONFIG_RAN"
    private const val VER_NOTIF = "VER_NOTIF"
    private const val SHOW_COUNT = "SHOW_COUNT"
    private const val CLICK_COUNT = "CLICK_COUNT"
    private const val CAMPAIGN_ID = "CAMPAIGN_ID"

    const val WAY_TAG = "WAY_TAG"
    const val WAY_BOOST = 0
    const val WAY_BATTERY = 1
    const val WAY_TEMP = 2
    const val WAY_CLEAR = 3
    const val WAY_OTHER = 4
    const val WAY_NONE = -1

    const val WAY_AD_LABEL = "WAY_AD_LABEL"

    const val ID_TAG = "ID_TAG"
    const val ID_EMPTY = "ID_EMPTY"

    const val PREM_VER_TAG = "PREM_VER_TAG"
    const val BATTERY_LOW_TAG = "BATTERY_LOW_TAG"

    const val FREE_SPACE_LOW = "FREE_SPACE_LOW"

    const val COUNT_APPS_TAG = "COUNT_APPS_TAG"

    const val SYSTEM_NOTIF_TAG = "SYSTEM_NOTIF_TAG"


    const val SYSTEM_NOTIF_WAY_TAG = "SYSTEM_NOTIF_WAY_TAG"

    const val SYSTEM_NOTIF_BAT = "SYSTEM_NOTIF_BAT"
    const val SYSTEM_NOTIF_STORAGE = "SYSTEM_NOTIF_STORAGE"
    const val SYSTEM_NOTIF_INSTALL = "SYSTEM_NOTIF_INSTALL"
    const val SYSTEM_NOTIF_DELETE = "SYSTEM_NOTIF_DELETE"
    const val SYSTEM_NOTIF_EMPTY = "SYSTEM_NOTIF_EMPTY"

    const val FCM_NOTIF_WAY_TAG = "FCM_NOTIF_WAY_TAG"

    private const val AB_MONTH_PRICE_VALUE_TAG = "AB_MONTH_PRICE_VALUE_TAG"
    private const val AB_MONTH_PRICE_UNIT_TAG = "AB_MONTH_PRICE_UNIT_TAG"

    private const val AB_YEAR_PRICE_VALUE_TAG = "AB_YEAR_PRICE_VALUE_TAG"
    private const val AB_YEAR_PRICE_UNIT_TAG = "AB_YEAR_PRICE_UNIT_TAG"

    private const val EMPTY_UNIT_MONTH = "USD"
    private const val EMPTY_VALUE_MONTH = 4F

    private const val EMPTY_UNIT_YEAR = "USD"
    private const val EMPTY_VALUE_YEAR = 38F

    const val TRIGGER_TAG = "TRIGGER_TAG"
    const val COUNT_TAG = "COUNT_TAG"
    const val AD_BAN_AD = "AD_BAN_AD"

    private val preferences = MyApp.getInstance().getSharedPreferences("waseem", Context.MODE_PRIVATE)

    fun getInstance() = preferences

    private fun editor(put: (SharedPreferences.Editor?) -> SharedPreferences.Editor?) = put(getInstance()?.edit())?.apply()

    fun setPrice(price: String) = editor { it?.putString(PRICE_TAG, price) }
    fun getPrice() = getInstance()?.getString(PRICE_TAG, DEF_PRICE)

    /*MONTH*/
    fun setABMonthPriceValue(price: Float) = editor { it?.putFloat(AB_MONTH_PRICE_VALUE_TAG, price) }
    fun getABMonthPriceValue() = getInstance()?.getFloat(AB_MONTH_PRICE_VALUE_TAG, EMPTY_VALUE_MONTH)

    fun setABMonthPriceUnit(unit: String) = editor { it?.putString(AB_MONTH_PRICE_UNIT_TAG, unit) }
    fun getABMonthPriceUnit() = getInstance()?.getString(AB_MONTH_PRICE_UNIT_TAG, EMPTY_UNIT_MONTH)

    /*YEAR*/
    fun setABYearPriceValue(price: Float) = editor { it?.putFloat(AB_YEAR_PRICE_VALUE_TAG, price) }
    fun getABYearPriceValue() = getInstance()?.getFloat(AB_YEAR_PRICE_VALUE_TAG, EMPTY_VALUE_YEAR)

    fun setABYearPriceUnit(price: String) = editor { it?.putString(AB_YEAR_PRICE_UNIT_TAG, price) }
    fun getABYearPriceUnit() = getInstance()?.getString(AB_YEAR_PRICE_UNIT_TAG, EMPTY_UNIT_YEAR)


    fun setFirstEnterStatusOn() = editor { it?.putInt(FIRST_ENTER_TAG_NEW, FIRST_ENTER_READY) }
    fun getFirstEnterStatus() = getInstance()?.getInt(FIRST_ENTER_TAG_NEW, FIRST_ENTER_ALREADY)


    fun setPercent(per: Int) = editor {
        it?.putInt(CONFIG_RAN, per)
    }
    fun getPercent() = getInstance()?.getInt(CONFIG_RAN, 100)

    fun setVerNotif(ver: Int) = editor { it?.putInt(VER_NOTIF, ver) }
    fun getVerNotif() = getInstance()?.getInt(VER_NOTIF, 0)



    fun setId(id: String) = editor { it?.putString(CAMPAIGN_ID, id) }
    fun getId() = getInstance()?.getString(CAMPAIGN_ID, "")

    fun setUserId(id: String) = editor { it?.putString(ID_TAG, id) }
    fun getUserId() = getInstance()?.getString(ID_TAG, ID_EMPTY)

    fun setWidgetWay(way: Int) {
        setWidgetAdLabel(way)
        editor { it?.putInt(WAY_TAG, way) }
    }
    fun clearWidgetWay() = editor { it?.putInt(WAY_TAG, WAY_NONE) }
    fun getWidgetWay() = getInstance()?.getInt(WAY_TAG, WAY_NONE)

    fun setWidgetAdLabel(id: Int) = editor { it?.putInt(WAY_AD_LABEL, id) }
    fun getWidgetAdLabel() = getInstance()?.getInt(WAY_AD_LABEL, WAY_NONE)
    fun clearWidgetAdLabel() {
        Log.e("LOL", "clearWidgetAdLabel")
        editor { it?.putInt(WAY_AD_LABEL, WAY_NONE) }
    }

    fun setPremVersion(ver: String) = editor { it?.putString(PREM_VER_TAG, ver) }
    fun getPremVersion() = getInstance()?.getString(PREM_VER_TAG, ABConfig.A_VER)

    fun setSpeakLowBattery(isSpeak: Boolean, prefix : String) = editor { it?.putBoolean("$prefix/$BATTERY_LOW_TAG", isSpeak) }
    fun isSpeakLowBattery(prefix : String) = getInstance()?.getBoolean("$prefix/$BATTERY_LOW_TAG", false)

    fun setAppsCount(count: Int) = editor { it?.putInt(COUNT_APPS_TAG, count) }
    fun getAppsCount() = getInstance()?.getInt(COUNT_APPS_TAG, -1)

    fun setSpeakLowSpace(isSpeak: Boolean) = editor { it?.putBoolean(FREE_SPACE_LOW, isSpeak) }
    fun isSpeakLowSpace() = getInstance()?.getBoolean(FREE_SPACE_LOW, false)

    fun setABSystemNotif(number: Int) = editor { it?.putInt(SYSTEM_NOTIF_TAG, number) }
    fun getABSystemNotif() = getInstance()?.getInt(SYSTEM_NOTIF_TAG, 0)

    fun setSystemNotifWay(type: String) = editor { it?.putString(SYSTEM_NOTIF_WAY_TAG, type) }
    fun getSystemNotifWay() = getInstance()?.getString(SYSTEM_NOTIF_WAY_TAG, SYSTEM_NOTIF_EMPTY)

    fun setFCMNotifWay() = editor { it?.putBoolean(FCM_NOTIF_WAY_TAG, true) }
    fun getFCMNotifWay() = getInstance()?.getBoolean(FCM_NOTIF_WAY_TAG, false)
    fun clearFCMNotifWay() = editor { it?.putBoolean(FCM_NOTIF_WAY_TAG, false) }

    fun setTrigger(count : Int) = editor { it?.putInt(TRIGGER_TAG, count)}
    fun getTrigger() = getInstance()?.getInt(TRIGGER_TAG, 1)

    fun setAdBanStatus(isBanned : Boolean) = editor { it?.putBoolean(AD_BAN_AD, isBanned)}
    fun getAdBanStatus() = getInstance()?.getBoolean(AD_BAN_AD, false)

    fun setClickCount(count: Int) = editor { it?.putInt(CLICK_COUNT, count) }
    fun getClickCount() = getInstance()?.getInt(CLICK_COUNT, 0)

    fun setShowCount(count: Int) = editor { it?.putInt(SHOW_COUNT, count) }
    fun getShowCount() = getInstance()?.getInt(SHOW_COUNT, 0)

}