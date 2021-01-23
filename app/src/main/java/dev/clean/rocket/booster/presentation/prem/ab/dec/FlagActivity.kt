package dev.clean.rocket.booster.presentation.prem.ab.dec

import android.os.Bundle
import dev.clean.rocket.booster.R
import dev.clean.rocket.booster.inapp.InAppCallback
import dev.clean.rocket.booster.presentation.prem.IDS
import dev.clean.rocket.booster.utils.SubscriptionProvider
import kotlinx.android.synthetic.main.flag_activity.*
import kotlinx.android.synthetic.main.flag_activity.btnBuy
import kotlinx.android.synthetic.main.flag_activity.flMonth
import kotlinx.android.synthetic.main.flag_activity.flYear
import kotlinx.android.synthetic.main.flag_activity.ivBackMonth
import kotlinx.android.synthetic.main.flag_activity.ivBackYear
import kotlinx.android.synthetic.main.flag_activity.ivClose
import kotlinx.android.synthetic.main.flag_activity.tvMonthPrice
import kotlinx.android.synthetic.main.flag_activity.tvPlayTerms
import kotlinx.android.synthetic.main.flag_activity.tvYearPrice

class FlagActivity : PriceManagerActivity(R.layout.flag_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectMonth()
        flMonth.setOnClickListener {
            selectMonth()
        }

        flYear.setOnClickListener {
            selectYear()
        }
        tvMonthPrice.text = monthPrice
        tvYearPrice.text = yearPrice
        tvYearOldPrice.text =  "$oldYearPrice${getString(R.string.in_year_shield)}"

        ivClose.setOnClickListener {
            close()
        }

        btnBuy.setOnClickListener {
            SubscriptionProvider.choiceSubNew(this, getSubId(), object :
                    InAppCallback {
                override fun trialSucces() {
                    handlInApp()
                }
            })
        }
    }

    private fun getSubId(): String {
        return if (ivBackMonth.isSelected){
            IDS.FLAG_MONTH
        }else{
            IDS.FLAG_YEAR
        }
    }

    private fun selectYear() {
        ivBackMonth.isSelected = false
        ivBackYear.isSelected = true

        tvPlayTerms.text = termsYear
        setYear()
    }

    private fun selectMonth() {
        ivBackMonth.isSelected = true
        ivBackYear.isSelected = false

        tvPlayTerms.text = termsMonth
        setMonth()
    }
}