package dev.clean.rocket.booster.PPP

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast

import dev.clean.rocket.booster.R
import dev.clean.rocket.booster.utils.PreferencesProvider
import kotlinx.android.synthetic.main.pick_apps.*

/**
 * Created by intag pc on 2/26/2017.
 */

class Pick_Apps : Activity() {

    /// Choose App to Add it in Usable APP LIST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pick_apps)

        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        val editor = PreferencesProvider.getInstance().edit()

        addcontacts.setOnClickListener {
            if (!(PreferencesProvider.getInstance().getString("button1", "l") == "4" || PreferencesProvider.getInstance().getString("button2", "l") == "4" || PreferencesProvider.getInstance().getString("button3", "l") == "4" || PreferencesProvider.getInstance().getString("button4", "l") == "4")) {
                if (PreferencesProvider.getInstance().getString("button", "1") == "1") {
                    editor.putString("button1", "4")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "2") {
                    editor.putString("button2", "4")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "3") {
                    editor.putString("button3", "4")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "4") {
                    editor.putString("button4", "4")
                    editor.apply()
                }

                finish()
            } else {
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.my_toast, null)

                val text = layout.findViewById<View>(R.id.textView1) as TextView
                text.setText(R.string.this_app_is_already_added)

                val toast = Toast(this@Pick_Apps)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70)
                toast.duration = Toast.LENGTH_LONG
                toast.view = layout
                toast.show()

                //                   Toast.makeText(Pick_Apps.this, "Choose Another App This App Is Already Added", Toast.LENGTH_SHORT).show();
            }
        }

        addplaystore.setOnClickListener {
            if (!(PreferencesProvider.getInstance().getString("button1", "l") == "1" || PreferencesProvider.getInstance().getString("button2", "l") == "1" || PreferencesProvider.getInstance().getString("button3", "l") == "1" || PreferencesProvider.getInstance().getString("button4", "l") == "1")) {

                if (PreferencesProvider.getInstance().getString("button", "1") == "1") {
                    editor.putString("button1", "1")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "2") {
                    editor.putString("button2", "1")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "3") {
                    editor.putString("button3", "1")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "4") {
                    editor.putString("button4", "1")
                    editor.apply()
                }

                finish()
            } else {
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.my_toast, null)

                val text = layout.findViewById<View>(R.id.textView1) as TextView
                text.setText(R.string.this_app_is_already_added)

                val toast = Toast(this@Pick_Apps)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70)
                toast.duration = Toast.LENGTH_LONG
                toast.view = layout
                toast.show()

                //                    Toast.makeText(Pick_Apps.this, "Choose Another App This App Is Already Added", Toast.LENGTH_SHORT).show();
            }
        }

        addcalculator.setOnClickListener {
            if (!(PreferencesProvider.getInstance().getString("button1", "l") == "2" || PreferencesProvider.getInstance().getString("button2", "l") == "2" || PreferencesProvider.getInstance().getString("button3", "l") == "2" || PreferencesProvider.getInstance().getString("button4", "l") == "2")) {

                if (PreferencesProvider.getInstance().getString("button", "1") == "1") {
                    editor.putString("button1", "2")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "2") {
                    editor.putString("button2", "2")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "3") {
                    editor.putString("button3", "2")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "4") {
                    editor.putString("button4", "2")
                    editor.apply()
                }

                finish()
            } else {

                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.my_toast, null)

                val text = layout.findViewById<View>(R.id.textView1) as TextView
                text.setText(R.string.this_app_is_already_added)


                val toast = Toast(this@Pick_Apps)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70)
                toast.duration = Toast.LENGTH_LONG
                toast.view = layout
                toast.show()

                //                    Toast.makeText(Pick_Apps.this, "Choose Another App This App Is Already Added", Toast.LENGTH_SHORT).show();
            }
        }

        addclock.setOnClickListener {
            if (!(PreferencesProvider.getInstance().getString("button1", "l") == "3" || PreferencesProvider.getInstance().getString("button2", "l") == "3" || PreferencesProvider.getInstance().getString("button3", "l") == "3" || PreferencesProvider.getInstance().getString("button4", "l") == "3")) {

                if (PreferencesProvider.getInstance().getString("button", "1") == "1") {
                    editor.putString("button1", "3")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "2") {
                    editor.putString("button2", "3")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "3") {
                    editor.putString("button3", "3")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "4") {
                    editor.putString("button4", "3")
                    editor.apply()
                }

                finish()
            } else {
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.my_toast, null)

                val text = layout.findViewById<View>(R.id.textView1) as TextView
                text.text = "This App Is Already Added"

                val toast = Toast(this@Pick_Apps)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70)
                toast.duration = Toast.LENGTH_LONG
                toast.view = layout
                toast.show()

                //                    Toast.makeText(Pick_Apps.this, "Choose Another App This App Is Already Added", Toast.LENGTH_SHORT).show();
            }
        }

        addmap.setOnClickListener {
            if (!(PreferencesProvider.getInstance().getString("button1", "l") == "5" || PreferencesProvider.getInstance().getString("button2", "l") == "5" || PreferencesProvider.getInstance().getString("button3", "l") == "5" || PreferencesProvider.getInstance().getString("button4", "l") == "5")) {

                if (PreferencesProvider.getInstance().getString("button", "1") == "1") {
                    editor.putString("button1", "5")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "2") {
                    editor.putString("button2", "5")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "3") {
                    editor.putString("button3", "5")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "4") {
                    editor.putString("button4", "5")
                    editor.apply()
                }

                finish()
            } else {

                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.my_toast, null)

                val text = layout.findViewById<View>(R.id.textView1) as TextView
                text.setText(R.string.this_app_is_already_added)


                val toast = Toast(this@Pick_Apps)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70)
                toast.duration = Toast.LENGTH_LONG
                toast.view = layout
                toast.show()

                //                    Toast.makeText(Pick_Apps.this, "Choose Another App This App Is Already Added", Toast.LENGTH_SHORT).show();
            }
        }

        addcamera.setOnClickListener {
            if (!(PreferencesProvider.getInstance().getString("button1", "l") == "6" || PreferencesProvider.getInstance().getString("button2", "l") == "6" || PreferencesProvider.getInstance().getString("button3", "l") == "6" || PreferencesProvider.getInstance().getString("button4", "l") == "6")) {

                if (PreferencesProvider.getInstance().getString("button", "1") == "1") {
                    editor.putString("button1", "6")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "2") {
                    editor.putString("button2", "6")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "3") {
                    editor.putString("button3", "6")
                    editor.apply()
                } else if (PreferencesProvider.getInstance().getString("button", "1") == "4") {
                    editor.putString("button4", "6")
                    editor.apply()
                }

                finish()
            } else {
                //                    Toast.makeText(Pick_Apps.this, "Choose Another App This App Is Already Added", Toast.LENGTH_SHORT).show();

                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.my_toast, null)

                val text = layout.findViewById<View>(R.id.textView1) as TextView
                text.text = "This App Is Already Added"

                val toast = Toast(this@Pick_Apps)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70)
                toast.duration = Toast.LENGTH_LONG
                toast.view = layout
                toast.show()
            }
        }

        editor.apply()


    }
}
