package dev.clean.rocket.booster.Volume

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

import dev.clean.rocket.booster.MainActivity
import dev.clean.rocket.booster.R

import kotlinx.android.synthetic.main.fragment_buy_consume.*
import dev.clean.rocket.booster.utils.SubscriptionProvider


class NoAdsFrag : AppCompatActivity() {
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_fragment)
        if (savedState == null) {
            val fm = supportFragmentManager
            fm.beginTransaction()
                    .add(R.id.fragment, MyFragment())
                    .commit()
        }
    }

    class MyFragment : Fragment() {
        // @BindView(R.id.item)
        //TextView mItem;
        private var resCode = 0
        private val canClick = MutableLiveData<Boolean>()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_buy_consume, container, false)
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            canClick.observe(this, Observer {
                buy_consume.isEnabled = it
            })

            buy_consume.setOnClickListener {
                SubscriptionProvider.startSubscription(activity!!)
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            resCode = resultCode
            Log.i("onActivityResult", "Request CODE: $requestCode")
            Log.i("onActivityResult", "Result Code: $resultCode")
        }


        override fun setUserVisibleHint(isVisibleToUser: Boolean) {
            super.setUserVisibleHint(isVisibleToUser)


            if (isVisibleToUser) {
                MainActivity.setInfo(R.string.remove_ads)
            } else {

            }
        }

        companion object {

            private val AD_FREE = "noads"
        }
    }

}


