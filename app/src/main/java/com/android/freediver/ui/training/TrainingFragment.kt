package com.android.freediver.ui.training

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.freediver.R
import com.android.freediver.ui.besttime.BestTimeActivity
import com.android.freediver.ui.co2table.CO2TableActivity
import kotlinx.android.synthetic.main.fragment_training.*

class TrainingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_training, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bestTimeButton.setOnClickListener {
            val bestTimeIntent = Intent(this.context, BestTimeActivity::class.java)
            startActivity(bestTimeIntent)
        }

        co2Button.setOnClickListener {
            val co2TableIntent = Intent(this.context, CO2TableActivity::class.java)
            startActivity(co2TableIntent)
        }

    }
}