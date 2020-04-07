package com.android.freediver.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.freediver.R
import com.android.freediver.ui.besttime.BestTimeActivity
import com.android.freediver.ui.co2table.CO2TableActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        bestTimeButton.setOnClickListener {
            val bestTimeIntent = Intent(this, BestTimeActivity::class.java)
            startActivity(bestTimeIntent)
        }

        co2Button.setOnClickListener {
            val co2TableIntent = Intent(this, CO2TableActivity::class.java)
            startActivity(co2TableIntent)
        }
    }
}
