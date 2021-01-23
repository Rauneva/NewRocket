package dev.mem.rocket.sanya.presentation.prem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import dev.mem.rocket.sanya.MainActivity
import dev.mem.rocket.sanya.R
import dev.mem.rocket.sanya.inapp.DiamondFrag
import dev.mem.rocket.sanya.utils.PreferencesProvider
import kotlinx.android.synthetic.main.parent_prem_activity.*

class ParentPremActivity : AppCompatActivity(R.layout.parent_prem_activity) {


    override fun onPostResume() {
        super.onPostResume()
        PreferencesProvider.setFirstEnterStatusOn()
        supportFragmentManager.beginTransaction().replace(R.id.flParent, DiamondFrag.newInstance("")).commit()

        ivClose.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }
}