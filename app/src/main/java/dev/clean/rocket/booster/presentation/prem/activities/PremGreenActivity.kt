package dev.clean.rocket.booster.presentation.prem.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yandex.metrica.YandexMetrica
import dev.clean.rocket.booster.MainActivity
import dev.clean.rocket.booster.R
import dev.clean.rocket.booster.utils.SubscriptionProvider
import dev.clean.rocket.booster.inapp.InAppCallback
import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.PreferencesProvider
import kotlinx.android.synthetic.main.prem_green_activity.*

class PremGreenActivity : AppCompatActivity(R.layout.prem_green_activity) {

    var where = Analytics.make_purchase_start

    override fun onBackPressed() {
        //super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onPostResume() {
        super.onPostResume()
        PreferencesProvider.setFirstEnterStatusOn()
    }

    private fun handlInApp() {
        SubscriptionProvider.setSuccesSubscription()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnPayGreen.setOnClickListener { _ ->
            SubscriptionProvider.startChoiseSub(this, 3, object :
                    InAppCallback {
                override fun trialSucces() {
                    handlInApp()
                    Analytics.makePurchase(where)
                }
            })
        }

        btnClose.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        setPrice()
    }

    private fun setPrice() {
        var price = PreferencesProvider.getPrice()!!
        var unit = price.split(" ")[1]
        var micro = price.split(" ")[0]
        var oldMicro = (micro.toDouble() / 3).toInt() + micro.toInt()

        tvCurrentPrice.text = "$price${getString(R.string.month)}"
        tvOldPrice.text = "$oldMicro $unit${getString(R.string.month)}"
        YandexMetrica.reportEvent(price)
    }
}