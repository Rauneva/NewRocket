package dev.clean.rocket.booster.presentation.prem.ab.dec

import android.os.Bundle
import dev.clean.rocket.booster.R
import dev.clean.rocket.booster.inapp.InAppCallback
import dev.clean.rocket.booster.presentation.prem.IDS
import dev.clean.rocket.booster.utils.SubscriptionProvider
import kotlinx.android.synthetic.main.lock_activity.*
import kotlinx.android.synthetic.main.lock_activity.btnBuy
import kotlinx.android.synthetic.main.lock_activity.flMonth
import kotlinx.android.synthetic.main.lock_activity.flYear
import kotlinx.android.synthetic.main.lock_activity.ivClose
import kotlinx.android.synthetic.main.lock_activity.tvMonthPrice
import kotlinx.android.synthetic.main.lock_activity.tvPlayTerms
import kotlinx.android.synthetic.main.lock_activity.tvYearPrice

class LockActivity : PriceManagerActivity(R.layout.lock_activity) {

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
        return if (ivRBMonth.isSelected){
            IDS.NEW_LOCK_MONTH
        }else{
            IDS.NEW_LOCK_YEAR
        }
    }

    private fun selectYear() {
        dvYear.isSelected = true

        tvOldYearPrice.setTextColor(resources.getColor(R.color.text_color_lock_selected))
        tvTimeYearSmall.setTextColor(resources.getColor(R.color.text_color_lock_selected))
        tvYearPrice.setTextColor(resources.getColor(R.color.text_color_lock_selected))
        tvYearTime.setTextColor(resources.getColor(R.color.text_color_lock_selected))

        ivYearBack.isSelected = true
        ivRBYear.isSelected = true

        ivRBMonth.isSelected = false
        ivMonthBack.isSelected = false

        tvMonthTime.setTextColor(resources.getColor(R.color.text_color_lock_unselected))
        tvMonthPrice.setTextColor(resources.getColor(R.color.text_color_lock_unselected))
        tvTimeMonthSmall.setTextColor(resources.getColor(R.color.text_color_lock_unselected))

        tvPlayTerms.text = termsYear
        setYear()
    }

    private fun selectMonth() {
        dvYear.isSelected = false

        tvOldYearPrice.setTextColor(resources.getColor(R.color.text_color_lock_unselected))
        tvTimeYearSmall.setTextColor(resources.getColor(R.color.text_color_lock_unselected))
        tvYearPrice.setTextColor(resources.getColor(R.color.text_color_lock_unselected))
        tvYearTime.setTextColor(resources.getColor(R.color.text_color_lock_unselected))

        ivYearBack.isSelected = false
        ivRBYear.isSelected = false

        ivRBMonth.isSelected = true
        ivMonthBack.isSelected = true

        tvMonthTime.setTextColor(resources.getColor(R.color.text_color_lock_selected))
        tvMonthPrice.setTextColor(resources.getColor(R.color.text_color_lock_selected))
        tvTimeMonthSmall.setTextColor(resources.getColor(R.color.text_color_lock_selected))

        tvPlayTerms.text = termsMonth
        setMonth()
    }
}