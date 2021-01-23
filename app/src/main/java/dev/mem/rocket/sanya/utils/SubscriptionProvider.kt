package dev.mem.rocket.sanya.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.billingclient.api.*
import com.yandex.metrica.YandexMetrica
import dev.mem.rocket.sanya.Config.Companion.HAS_SUBSCRIPTION
import dev.mem.rocket.sanya.inapp.InAppCallback
import dev.mem.rocket.sanya.presentation.prem.IDS
import java.util.*
import kotlin.collections.HashMap

object SubscriptionProvider : PurchasesUpdatedListener, BillingClientStateListener {

    lateinit private var playStoreBillingClient: BillingClient
    private lateinit var preferences: SharedPreferences
    private val skuDetails: MutableMap<String, SkuDetails?> =
            HashMap()

    private const val SUBSCRIPTION_ID = "diff_no_ads_i"
    private const val TRACKER_TAG = "TRACKER_TAG"

    private var inAppCallback: InAppCallback? = null

    var idsSubs = listOf("sub_blue_lock", "sub_shout", "sub_alert", "sub_white_green")

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult!!.responseCode == BillingClient.BillingResponseCode.OK) {
            if (purchases != null && purchases.size > 0 && purchases[0].purchaseState == Purchase.PurchaseState.PURCHASED) {
                inAppCallback?.trialSucces()
                if (!purchases[0].isAcknowledged) {
                    val params = AcknowledgePurchaseParams
                            .newBuilder()
                            .setPurchaseToken(purchases[0].purchaseToken)
                            .build()
                    var listener = AcknowledgePurchaseResponseListener {
                        Log.e("LOL", "confirmed")
                    }
                    playStoreBillingClient.acknowledgePurchase(params, listener)
                }
            }
        }
    }


    fun init(context: Context) {
        preferences = context.getSharedPreferences("subscription", Context.MODE_PRIVATE)
        playStoreBillingClient = BillingClient.newBuilder(context.applicationContext)
                .enablePendingPurchases() // required or app will crash
                .setListener(this).build()
        connectToPlayBillingService()
    }

    private fun connectToPlayBillingService(): Boolean {
        if (!playStoreBillingClient.isReady) {
            playStoreBillingClient.startConnection(this)
            return true
        }
        return false
    }


    override fun onBillingServiceDisconnected() {

    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            var hasSubscription = false
            val result = playStoreBillingClient.queryPurchases(BillingClient.SkuType.SUBS)
            if (result != null && result.purchasesList != null && result.purchasesList!!.size > 0) {
                hasSubscription = true
            }
            preferences.edit().putBoolean(HAS_SUBSCRIPTION, hasSubscription).apply()
        }
    }

    fun hasSubscription() = preferences.getBoolean(HAS_SUBSCRIPTION, false)

    fun setSuccesSubscription() = preferences.edit().putBoolean(HAS_SUBSCRIPTION, true).commit()

    fun startSubscription(activity: Activity) {
        val params = SkuDetailsParams.newBuilder().setSkusList(arrayListOf(SUBSCRIPTION_ID))
                .setType(BillingClient.SkuType.SUBS).build()
        playStoreBillingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    if (skuDetailsList.orEmpty().isNotEmpty()) {
                        skuDetailsList!!.forEach {
                            val perchaseParams = BillingFlowParams.newBuilder().setSkuDetails(it)
                                    .build()
                            playStoreBillingClient.launchBillingFlow(activity, perchaseParams)
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    fun startChoiseSub(activity: Activity, number: Int, callback: InAppCallback) {
        Log.e("LOL", "make real purchase ${idsSubs[number]}")
        inAppCallback = callback
        val params = SkuDetailsParams.newBuilder().setSkusList(arrayListOf(idsSubs[number]))
                .setType(BillingClient.SkuType.SUBS).build()
        playStoreBillingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    if (skuDetailsList.orEmpty().isNotEmpty()) {
                        skuDetailsList!!.forEach {
                            skuDetails[TRACKER_TAG] = it
                            val perchaseParams = BillingFlowParams.newBuilder().setSkuDetails(it)
                                    .build()
                            playStoreBillingClient.launchBillingFlow(activity, perchaseParams)
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    fun choiceSubNew(activity: Activity, subId: String, callback: InAppCallback) {
        inAppCallback = callback
        val params = SkuDetailsParams.newBuilder().setSkusList(arrayListOf(subId))
                .setType(BillingClient.SkuType.SUBS).build()
        playStoreBillingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    if (skuDetailsList.orEmpty().isNotEmpty()) {
                        skuDetailsList!!.forEach {
                            skuDetails[TRACKER_TAG] = it
                            val perchaseParams = BillingFlowParams.newBuilder().setSkuDetails(it)
                                    .build()
                            playStoreBillingClient.launchBillingFlow(activity, perchaseParams)
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    fun startGettingPrice(number: Int): String {
        Log.e("LOL", "get price ${idsSubs[number]}")
        val params = SkuDetailsParams.newBuilder().setSkusList(arrayListOf(idsSubs[number], IDS.WHITE_MONTH, IDS.WHITE_YEAR))
                .setType(BillingClient.SkuType.SUBS).build()
        playStoreBillingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            Log.e("LOL", billingResult.responseCode.toString())
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    if (skuDetailsList!!.isNotEmpty()) {
                        try {
                            var priceParse = skuDetailsList!![0].priceAmountMicros / 1_000_000
                            var unitParse = skuDetailsList!![0].priceCurrencyCode
                            PreferencesProvider.setPrice("$priceParse $unitParse")

                            var monthUnitSymbol = Currency.getInstance(skuDetailsList!![1].priceCurrencyCode).symbol
                            PreferencesProvider.setABMonthPriceValue(skuDetailsList!![1].priceAmountMicros.toFloat() / 1_000_000)
                            PreferencesProvider.setABMonthPriceUnit(monthUnitSymbol)

                            var yearUnitSymbol = Currency.getInstance(skuDetailsList!![2].priceCurrencyCode).symbol
                            PreferencesProvider.setABYearPriceValue(skuDetailsList!![2].priceAmountMicros.toFloat() / 1_000_000)
                            PreferencesProvider.setABYearPriceUnit(yearUnitSymbol)

                        } catch (ex: Exception) {
                            YandexMetrica.reportEvent("error price set")
                        }
                    }
                }
            }
        }
        return ""
    }


}
