package com.android.freediver.ui.co2table

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.freediver.R
import com.android.freediver.databinding.ActivityCo2TableBinding
import com.android.freediver.util.ChronometerAction
import kotlinx.android.synthetic.main.activity_best_time.chronometer
import kotlinx.android.synthetic.main.activity_best_time.progressBar

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

        binding.contractionsButton.setOnClickListener {
            viewModel.contractionsStartTime = viewModel.getDeltaTime(chronometer.base)
        }

        binding.startButton.setOnClickListener {
            viewModel.startTable()
        }

        binding.pauseButton.setOnClickListener {
            viewModel.chronometerAction(ChronometerAction.Pause)
        }

        binding.stopButton.setOnClickListener {
            viewModel.chronometerAction(ChronometerAction.Stop)
        }

        binding.resumeButton.setOnClickListener {
            viewModel.chronometerAction(ChronometerAction.Resume)
        }

        binding.chronometer.setOnChronometerTickListener {
            updateProgressBar(it.base)
        }

        viewModel.startCountEvent.observe(this, Observer {
            startChronometer()
        })

        viewModel.stopCountEvent.observe(this, Observer {
            stopChronometer()
        })

        viewModel.pauseCountEvent.observe(this, Observer {
            pauseChronometer()
        })

        viewModel.resumeCountEvent.observe(this, Observer {
            resumeChronometer()
        })

        viewModel.chronometerMaxValueChanged.observe(this, Observer {
            binding.progressBar.max = viewModel.chronometerMaxValue
        })

    }

    private fun startChronometer() {
        chronometer.base = SystemClock.elapsedRealtime()
        progressBar.progress = 0
        chronometer.start()
        binding.startButton.visibility = View.GONE
        binding.stopButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.VISIBLE
    }

    private fun stopChronometer() {
        chronometer.stop()
        binding.stopButton.visibility = View.GONE
        binding.pauseButton.visibility = View.GONE
        binding.resumeButton.visibility = View.GONE
        binding.startButton.visibility = View.VISIBLE
        clearUi()
    }

    private fun pauseChronometer() {
        chronometer.stop()
        binding.resumeButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.GONE
    }

    private fun resumeChronometer() {
        chronometer.start()
        binding.resumeButton.visibility = View.GONE
        binding.pauseButton.visibility = View.VISIBLE
    }

    private fun clearUi() {
        chronometer.base = SystemClock.elapsedRealtime()
        progressBar.progress = 0
    }

    private fun updateProgressBar(chronometerBaseTime: Long) {
        val deltaTime = viewModel.getDeltaTime(chronometerBaseTime)
        progressBar.progress = viewModel.parseMillisToSeconds(deltaTime)
        Log.d("PRG", "Progress ${viewModel.parseMillisToSeconds(deltaTime)}")
    }
}
