package dev.mem.rocket.sanya.presentation.privacy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.mem.rocket.sanya.MainActivity
import dev.mem.rocket.sanya.R
import kotlinx.android.synthetic.main.activity_privacy_police.*

class PrivacyPoliceActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_police)

        okBtn.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
    }
}
