package dev.clean.rocket.booster.presentation.prem.dialogs

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dev.clean.rocket.booster.MainActivity
import dev.clean.rocket.booster.R
import dev.clean.rocket.booster.utils.SubscriptionProvider
import dev.clean.rocket.booster.inapp.InAppCallback
import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.PreferencesProvider
import kotlinx.android.synthetic.main.cat_fragment_dialog.view.*

class CatFragmentDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.cat_fragment_dialog, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
        PreferencesProvider.setFirstEnterStatusOn()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.btnSkip.setOnClickListener {
            dismiss()
        }

        view.btnPay.setOnClickListener {
            SubscriptionProvider.startChoiseSub((activity as MainActivity), 0, object :
                    InAppCallback {
                override fun trialSucces() {
                    handlInApp()
                    Analytics.makePurchase(Analytics.make_purchase_cat_dialog)
                }
            })
        }
    }

    private fun handlInApp() {
        SubscriptionProvider.setSuccesSubscription()
        startActivity(Intent((activity as MainActivity), MainActivity::class.java))
        activity?.finish()
    }
}