package dev.clean.rocket.booster.presentation.prem.ab.dec

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import dev.clean.rocket.booster.MainActivity
import dev.clean.rocket.booster.MyApp
import dev.clean.rocket.booster.R
import dev.clean.rocket.booster.utils.PreferencesProvider
import dev.clean.rocket.booster.utils.SubscriptionProvider
import dev.clean.rocket.booster.utils.analytic.Analytics
import java.text.DecimalFormat

open class PriceManagerActivity(layout: Int) : AppCompatActivity(layout) {
    var oldYearPrice = ""
    var yearPrice = ""
    var monthPrice = ""

    var termsYear = ""
    var termsMonth = ""

    val where = Analytics.make_purchase_start

    private var whichTwice = Analytics.twice_month


    init {
        PreferencesProvider.setFirstEnterStatusOn()

        var formatter = DecimalFormat("#.##")

        monthPrice = "${formatter.format(PreferencesProvider.getABMonthPriceValue())} ${PreferencesProvider.getABMonthPriceUnit()}"
        yearPrice = "${formatter.format(PreferencesProvider.getABYearPriceValue())} ${PreferencesProvider.getABYearPriceUnit()}"

        var oldYearPriceValue = PreferencesProvider.getABYearPriceValue()!! + (PreferencesProvider.getABYearPriceValue()!! * 0.2)


        oldYearPrice = "${formatter.format(oldYearPriceValue)} ${PreferencesProvider.getABYearPriceUnit()}"

        termsMonth = MyApp.getInstance().applicationContext.getString(R.string.month_terms, monthPrice)
        termsYear = MyApp.getInstance().applicationContext.getString(R.string.year_terms, yearPrice)
    }

    fun close() {
        startActivity(Intent(MyApp.getInstance().applicationContext, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        close()
    }

    fun handlInApp() {
        Analytics.makePurchaseTwice(where, whichTwice)
        SubscriptionProvider.setSuccesSubscription()
        close()
    }

    fun setMonth(){
        whichTwice = Analytics.twice_month
    }

    fun setYear(){
        whichTwice = Analytics.twice_year
    }
}