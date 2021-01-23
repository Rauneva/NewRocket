package dev.mem.rocket.sanya.presentation.prem.dialogs

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dev.mem.rocket.sanya.MainActivity
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.utils.SubscriptionProvider
import dev.mem.rocket.sanya.inapp.InAppCallback
import dev.mem.rocket.sanya.utils.analytic.Analytics
import dev.mem.rocket.sanya.utils.PreferencesProvider
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