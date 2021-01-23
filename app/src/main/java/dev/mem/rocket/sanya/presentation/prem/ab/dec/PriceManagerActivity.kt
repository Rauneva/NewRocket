package dev.mem.rocket.sanya.presentation.prem.ab.dec

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dev.mem.rocket.sanya.MainActivity
import dev.mem.rocket.sanya.MyApp
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.utils.PreferencesProvider
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import dev.mem.rocket.sanya.utils.analytic.Analytics
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