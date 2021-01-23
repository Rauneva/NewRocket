package dev.clean.rocket.booster.presentation.prem.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.clean.rocket.booster.MainActivity
import dev.clean.rocket.booster.R
import dev.clean.rocket.booster.utils.SubscriptionProvider
import dev.clean.rocket.booster.inapp.InAppCallback
import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.PreferencesProvider
import kotlinx.android.synthetic.main.prem_shout_activity.*

class PremShoutActivity : AppCompatActivity(R.layout.prem_shout_activity) {

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
        btnShoutPay.setOnClickListener { _ ->
            SubscriptionProvider.startChoiseSub(this, 1, object :
                    InAppCallback {
                override fun trialSucces() {
                    handlInApp()
                    Analytics.makePurchase(where)
                }
            })
        }

        ivClose.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}