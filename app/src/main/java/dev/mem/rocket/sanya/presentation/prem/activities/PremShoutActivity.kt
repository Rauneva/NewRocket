package dev.mem.rocket.sanya.presentation.prem.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.mem.rocket.sanya.MainActivity
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import dev.mem.rocket.sanya.inapp.InAppCallback
import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.PreferencesProvider
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