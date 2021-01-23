package dev.clean.rocket.booster.presentation.prem.dialogs

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.yandex.metrica.YandexMetrica
import dev.clean.rocket.booster.MainActivity
import dev.clean.rocket.booster.R
import dev.clean.rocket.booster.utils.SubscriptionProvider
import dev.clean.rocket.booster.inapp.InAppCallback
import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.PreferencesProvider
import kotlinx.android.synthetic.main.premium_timer_dialog.*

class PremiumTimerDialog : DialogFragment() {

    var counterHours = 11
    var counterSeconds = 32
    var counterMinute = 59
    var max = 59
    var timer : CountDownTimer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.premium_timer_dialog, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        PreferencesProvider.setFirstEnterStatusOn()
        timer = object : CountDownTimer(100_000, 1_000){
            override fun onFinish() {
                timer?.start()
            }

            override fun onTick(millisUntilFinished: Long) {
                if (counterSeconds == 0){
                    if (counterMinute == 0){
                        counterHours --
                        counterMinute = 59
                    }else{
                        counterMinute --
                    }
                    counterSeconds = 59
                }else{
                    counterSeconds --
                }
                tvFirstHour?.text = "%02d".format(counterHours)[0].toString()
                tvSecondHour?.text = "%02d".format(counterHours)[1].toString()

                tvFirstMinute?.text = "%02d".format(counterMinute)[0].toString()
                tvSecondMinute?.text = "%02d".format(counterMinute)[1].toString()

                tvFirstSecond?.text = "%02d".format(counterSeconds)[0].toString()
                tvSecondSecond?.text = "%02d".format(counterSeconds)[1].toString()
            }
        }
        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        timer?.start()
        ivClose.setOnClickListener {
            dismiss()
        }

        btnPay.setOnClickListener {
            SubscriptionProvider.startChoiseSub((activity as MainActivity), 2, object :
                    InAppCallback {
                override fun trialSucces() {
                    handlInApp()
                    Analytics.makePurchase(Analytics.make_purchase_timer_dialog)
                }
            })
        }
        setPrice()
    }



    private fun handlInApp() {
        SubscriptionProvider.setSuccesSubscription()
        startActivity(Intent((activity as MainActivity), MainActivity::class.java))
        activity?.finish()
    }

    private fun setPrice() {
        var price = PreferencesProvider.getPrice()!!
        var unit = price.split(" ")[1]
        var micro = price.split(" ")[0]
        var oldMicro = (micro.toDouble() / 10 * 9).toInt() + micro.toInt()

        tvNewPrice.text = "$price${getString(R.string.month)}"
        tvOldAlertPrice.text = "$oldMicro $unit${getString(R.string.month)}"
        YandexMetrica.reportEvent(price)
    }
}