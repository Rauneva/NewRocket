package dev.clean.rocket.booster.presentation.prem.ab.dec

import android.os.Bundle
import dev.clean.rocket.booster.R
import dev.clean.rocket.booster.inapp.InAppCallback
import dev.clean.rocket.booster.presentation.prem.IDS
import dev.clean.rocket.booster.utils.SubscriptionProvider
import kotlinx.android.synthetic.main.white_activity.*
import kotlinx.android.synthetic.main.white_activity.btnBuy
import kotlinx.android.synthetic.main.white_activity.flMonth
import kotlinx.android.synthetic.main.white_activity.flYear
import kotlinx.android.synthetic.main.white_activity.ivClose
import kotlinx.android.synthetic.main.white_activity.tvMonthPrice
import kotlinx.android.synthetic.main.white_activity.tvOldYearPrice
import kotlinx.android.synthetic.main.white_activity.tvPlayTerms
import kotlinx.android.synthetic.main.white_activity.tvYearPrice

class WhiteActivity : PriceManagerActivity(R.layout.white_activity) {

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
        tvOldYearPrice.text = oldYearPrice

        tvMonthTerms.text = getString(R.string.description_month, monthPrice)
        tvYearTerms.text = getString(R.string.description_year, yearPrice)

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
            IDS.WHITE_MONTH
        }else{
            IDS.WHITE_YEAR
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