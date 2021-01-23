package dev.clean.rocket.booster.presentation.files

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import dev.clean.rocket.booster.AAA.Alarm_Junk
import dev.clean.rocket.booster.MainActivity
import dev.clean.rocket.booster.R
import com.clean.emptyrocket.Scanning_Junk

import java.util.Random

import android.content.Context.ALARM_SERVICE
import androidx.lifecycle.Observer
import dev.clean.rocket.booster.utils.analytic.Analytics
import dev.clean.rocket.booster.utils.PreferencesProvider
import kotlinx.android.synthetic.main.junk_cleaner.*

/**
 * Created by intag pc on 2/12/2017.
 */

class JunkCleanerFrag : Fragment() {

    internal var checkvar = 0

    override fun onResume() {
        checkText()
        super.onResume()
    }
    
    fun getHardText() = getString(R.string.system_junked_hard)
    fun getLiteText() = getString(R.string.system_junked_lite)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Alarm_Junk.getNeedToCheck().observe(this, Observer {
            if (it) {
                checkText()
            }
        })

        try {
            mainbutton.setOnClickListener {
                //TODO: dont call for android.Version > Nougat
                    if (PreferencesProvider.getInstance().getString("junk", "1") == "1") {

                        val intent = Intent(activity, Alarm_Junk::class.java)
                        val pendingIntent = PendingIntent.getBroadcast(activity, 0,
                                intent, PendingIntent.FLAG_ONE_SHOT)
                        val alarmManager = activity!!.getSystemService(ALARM_SERVICE) as AlarmManager
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 600 * 1000, pendingIntent)

                        startClean()

                    } else {
                        @SuppressLint("RestrictedApi") val inflater = getLayoutInflater(arguments)
                        val layout = inflater.inflate(R.layout.my_toast, null)

                        val text = layout.findViewById<View>(R.id.textView1) as TextView
                        text.setText(R.string.no_junk_files_already_cleaned)

                        val toast = Toast(activity)
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70)
                        toast.duration = Toast.LENGTH_LONG
                        toast.view = layout
                        toast.show()
                    }
            }
        } catch (e: Exception) {
        }
        checkText()
        if (PreferencesProvider.getWidgetWay() == PreferencesProvider.WAY_CLEAR){
            PreferencesProvider.clearWidgetWay()
            setCleanedState()
        }
    }

    private fun setCleanedState(){
        val handler = Handler()
        handler.postDelayed({
            mainbrush?.setImageResource(R.drawable.junk_blue)
            mainbutton.setText(R.string.cleaned)
            cache.setText(R.string.cache_memory) //2
            temp.setText(R.string.temporary_files)
            residue.setText(R.string.residual_files)
            system.setText(R.string.system_junk)


            maintext.setText(R.string.crystal_clear)
            maintext.setTextColor(Color.parseColor("#24D149"))

            cachetext.text = getLiteText()
            cachetext.setTextColor(Color.parseColor("#24D149"))

            temptext.text = getLiteText()
            temptext.setTextColor(Color.parseColor("#24D149"))

            residuetext.text = getLiteText()
            residuetext.setTextColor(Color.parseColor("#24D149"))

            systemtext.text = getLiteText()
            systemtext.setTextColor(Color.parseColor("#24D149"))


            PreferencesProvider.getInstance().edit()
                    .putString("junk", "0")
                    .apply()
        }, 2000)
    }

    private fun startClean() {
        val i = Intent(activity, Scanning_Junk::class.java)
        i.putExtra("junk", alljunk.toString() + "")
        startActivity(i)

        setCleanedState()

    }

    private fun checkText() {
        if (PreferencesProvider.getInstance().getString("junk", "1") == "1") {
            mainbrush.setImageResource(R.drawable.junk_red)
            mainbutton.setText(R.string.clean)
            cache.setText(R.string.cache_memory)
            temp.setText(R.string.temporary_files)
            residue.setText(R.string.residual_files)
            system.setText(R.string.system_junk)

            val ran1 = Random()
            val proc1 = ran1.nextInt(20) + 5

            val ran2 = Random()
            val proc2 = ran2.nextInt(15) + 10

            val ran3 = Random()
            val proc3 = ran3.nextInt(30) + 15

            val ran4 = Random()
            val proc4 = ran4.nextInt(25) + 10

            alljunk = proc1 + proc2 + proc3 + proc4

            maintext.text = getHardText()
            maintext.setTextColor(Color.parseColor("#F22938"))

            cachetext.text = getHardText()
            cachetext.setTextColor(Color.parseColor("#F22938"))

            temptext.text = getHardText()
            temptext.setTextColor(Color.parseColor("#F22938"))

            residuetext.text = getHardText()
            residuetext.setTextColor(Color.parseColor("#F22938"))

            systemtext.text = getHardText()
            systemtext.setTextColor(Color.parseColor("#F22938"))

        } else {
            mainbrush.setImageResource(R.drawable.junk_blue)
            mainbutton.setText(R.string.cleaned)
            cache.setText(R.string.cache_memory) //2
            temp.setText(R.string.temporary_files)
            residue.setText(R.string.residual_files)
            system.setText(R.string.system_junk)


            maintext.setText(R.string.crystal_clear)
            maintext.setTextColor(Color.parseColor("#24D149"))

            cachetext.text = getLiteText()
            cachetext.setTextColor(Color.parseColor("#24D149"))

            temptext.text = getLiteText()
            temptext.setTextColor(Color.parseColor("#24D149"))

            residuetext.text = getLiteText()
            residuetext.setTextColor(Color.parseColor("#24D149"))

            systemtext.text = getLiteText()
            systemtext.setTextColor(Color.parseColor("#24D149"))
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.junk_cleaner, container, false);
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity!!.intent.getBooleanExtra("fromService", false)) {
            mainbutton.callOnClick()
        }
    }


    override fun getUserVisibleHint(): Boolean {
        return userVisibleHint
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            MainActivity.setInfo(R.string.junk_cleaner)
            Analytics.openPart(Analytics.OPEN_JUNK_CLEAN)

        }
    }

    companion object {
        var alljunk: Int = 0
    }
}
