package dev.mem.rocket.sanya.presentation.prem.ab.dec

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.inapp.InAppCallback
import dev.mem.rocket.sanya.presentation.prem.IDS
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import kotlinx.android.synthetic.main.lock_activity.*
import kotlinx.android.synthetic.main.shield_activity.*
import kotlinx.android.synthetic.main.shield_activity.btnBuy
import kotlinx.android.synthetic.main.shield_activity.flMonth
import kotlinx.android.synthetic.main.shield_activity.flYear
import kotlinx.android.synthetic.main.shield_activity.ivClose
import kotlinx.android.synthetic.main.shield_activity.ivMonthBack
import kotlinx.android.synthetic.main.shield_activity.ivRBMonth
import kotlinx.android.synthetic.main.shield_activity.ivRBYear
import kotlinx.android.synthetic.main.shield_activity.ivYearBack
import kotlinx.android.synthetic.main.shield_activity.tvMonthPrice
import kotlinx.android.synthetic.main.shield_activity.tvMonthTime
import kotlinx.android.synthetic.main.shield_activity.tvOldYearPrice
import kotlinx.android.synthetic.main.shield_activity.tvPlayTerms
import kotlinx.android.synthetic.main.shield_activity.tvTimeMonthSmall
import kotlinx.android.synthetic.main.shield_activity.tvYearPrice
import kotlinx.android.synthetic.main.shield_activity.tvYearTime

class ShieldActivity : PriceManagerActivity(R.layout.shield_activity) {

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
        return if (ivMonthBack.isSelected){
            IDS.SHIELD_MONTH
        }else{
            IDS.SHIELD_YEAR
        }
    }

    private fun selectYear() {
        ivMonthBack.isSelected = false
        ivRBMonth.isSelected = false

        ivYearBack.isSelected = true
        ivRBYear.isSelected = true

        tvMonthTime.setTextColor(resources.getColor(R.color.text_color_shield_unselected))
        tvMonthPrice.setTextColor(resources.getColor(R.color.text_color_shield_unselected))
        tvTimeMonthSmall.setTextColor(resources.getColor(R.color.text_color_shield_unselected))

        tvYearTime.setTextColor(resources.getColor(R.color.text_color_shield_selected))

        tvPlayTerms.text = termsYear
        setYear()
    }

    private fun selectMonth() {
        ivMonthBack.isSelected = true
        ivRBMonth.isSelected = true

        ivYearBack.isSelected = false
        ivRBYear.isSelected = false

        tvMonthTime.setTextColor(resources.getColor(R.color.text_color_shield_selected))
        tvMonthPrice.setTextColor(resources.getColor(R.color.text_color_shield_selected))
        tvTimeMonthSmall.setTextColor(resources.getColor(R.color.text_color_shield_selected))

        tvYearTime.setTextColor(resources.getColor(R.color.text_color_shield_unselected))

        tvPlayTerms.text = termsMonth
        setMonth()
    }
}