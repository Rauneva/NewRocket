package dev.mem.rocket.sanya.presentation.prem.ab.dec

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.mem.rocket.sanya.MainActivity
import dev.mem.rocket.sanya.MyApp
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.inapp.InAppCallback
import dev.mem.rocket.sanya.presentation.prem.IDS
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import dev.mem.rocket.sanya.utils.analytic.Analytics
import kotlinx.android.synthetic.main.bottom_activity.*

class BottomActivity : PriceManagerActivity(R.layout.bottom_activity) {


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
            IDS.HAND_MONTH
        }else{
            IDS.HAND_YEAR
        }
    }

    private fun selectYear() {
        ivBackMonth.isSelected = false
        ivDotMonth.isSelected = false

        ivBackYear.isSelected = true
        ivDotYear.isSelected = true

        tvPlayTerms.text = termsYear
        setYear()
    }

    private fun selectMonth() {
        ivBackMonth.isSelected = true
        ivDotMonth.isSelected = true

        ivBackYear.isSelected = false
        ivDotYear.isSelected = false
        tvPlayTerms.text = termsMonth
        setMonth()
    }

}