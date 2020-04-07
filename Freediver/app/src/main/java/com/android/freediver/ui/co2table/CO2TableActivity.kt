package com.android.freediver.ui.co2table

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.freediver.R
import com.android.freediver.databinding.ActivityCo2TableBinding
import com.android.freediver.model.CO2Table
import com.android.freediver.model.Row
import kotlinx.android.synthetic.main.activity_best_time.*

class CO2TableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCo2TableBinding
    private lateinit var viewModel: CO2TableViewModel
    private lateinit var adapter: RowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_co2_table)
        viewModel = ViewModelProviders.of(this).get(CO2TableViewModel::class.java)

        adapter = RowAdapter(viewModel, this)
        binding.rowRecyclerView.adapter = adapter

        binding.playButton.setOnClickListener {
            viewModel.chronometerAction()
            viewModel.bestTimeDuration = viewModel.getDeltaTime(chronometer.base)
        }

        binding.contractionsButton.setOnClickListener {
            viewModel.contractionsStartTime = viewModel.getDeltaTime(chronometer.base)
        }

        binding.progressBar.max = viewModel.chronometerMaxValue
        binding.chronometer.setOnChronometerTickListener {
            updateProgressBar(it.base)
        }

        viewModel.startCountEvent.observe(this, Observer {
            startChronometer()
        })

        viewModel.stopCountEvent.observe(this, Observer {
            stopChronometer()
        })
    }

    private fun startChronometer() {
        chronometer.base = SystemClock.elapsedRealtime()
        progressBar.progress = 0
        chronometer.start()
        playButton.background = getDrawable(R.drawable.ic_stop_circle_ripple)
    }

    private fun stopChronometer() {
        chronometer.stop()
        playButton.background = getDrawable(R.drawable.ic_play_circle_ripple)
    }
    private fun clearUi() {
        progressBar.progress = 0
        chronometer.base = SystemClock.elapsedRealtime()
    }

    private fun updateProgressBar(chronometerBaseTime: Long) {
        val deltaTime = viewModel.getDeltaTime(chronometerBaseTime)
        progressBar.progress = viewModel.parseMillisToSeconds(deltaTime)
    }
}
