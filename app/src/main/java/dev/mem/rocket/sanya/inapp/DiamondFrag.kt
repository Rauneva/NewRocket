package dev.mem.rocket.sanya.inapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.yandex.metrica.YandexMetrica
import dev.mem.rocket.sanya.MainActivity
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.presentation.prem.IDS
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.PreferencesProvider

class DiamondFrag : Fragment() {

    companion object {
        private const val TAG_FROM = "TAG_FROM"

        fun newInstance(from: String): DiamondFrag {
            var bundle = Bundle()
            bundle.putString(TAG_FROM, from)
            var fragment = DiamondFrag()
            fragment.arguments = bundle
            return fragment
        }
    }

    var priceObserver: Observer<String>
    var version = 0
    var where = Analytics.make_purchase_start

    init {
        priceObserver = Observer {
            setPrice(it)
        }
    }

    private fun setPrice(it: String) {
        var price = ""
        price = if (it != "") {
            it
        } else {
            PreferencesProvider.getPrice()!!
        }
        view!!.findViewById<TextView>(tvsPrices[version]).text = "${getString(R.string.prem5)} $price"
        YandexMetrica.reportEvent(price)
    }

    var layouts = listOf(R.layout.prem_lat_white, R.layout.prem_lat_lock, R.layout.prem_lat_shield)
    var tvsPrices = listOf(R.id.tvPriceWhite, R.id.tvPriceLock, R.id.tvPriceShield)
    var buttons = listOf(R.id.btnPayWhite, R.id.btnPayLock, R.id.btnPayShield)

    private fun setPrice() {
        var price = PreferencesProvider.getPrice()!!
        view!!.findViewById<TextView>(tvsPrices[version]).text = "${getString(R.string.start_text_prem)} $price ${getString(R.string.end_text_prem)}"
        YandexMetrica.reportEvent(price)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        version = 1
        return inflater.inflate(layouts[version], container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is MainActivity){
            where = Analytics.make_purchase_inside
        }
        view!!.findViewById<Button>(buttons[version]).setOnClickListener { _ ->
            activity?.let { it1 ->
                SubscriptionProvider.choiceSubNew(it1, getSubId(), object :
                        InAppCallback {
                    override fun trialSucces() {
                        handlInApp()
                        Analytics.makePurchase(where)
                    }
                })
            }
        }
    }

    private fun getSubId(): String {
        return if (activity is MainActivity){
            IDS.OLD_LOCK
        }else{
            IDS.OLD_LOCK_MONTH
        }
    }

    private fun handlInApp() {
        SubscriptionProvider.setSuccesSubscription()
        activity?.let {
            startActivity(Intent(it, MainActivity::class.java))
        }
        activity?.finish()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && activity is MainActivity) {
            MainActivity.setInfo(R.string.remove_ads)
            setPrice()
        }
    }
}