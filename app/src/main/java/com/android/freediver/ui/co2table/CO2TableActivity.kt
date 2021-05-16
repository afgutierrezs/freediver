package com.android.freediver.ui.co2table

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.android.freediver.R
import com.android.freediver.databinding.ActivityCo2TableBinding
import com.android.freediver.util.chronometer.ChronometerAction
import com.android.freediver.util.notifications.cancelNotifications
import com.android.freediver.util.notifications.sendNotification
import kotlinx.android.synthetic.main.activity_best_time.chronometer
import kotlinx.android.synthetic.main.activity_best_time.progressBar

class CO2TableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCo2TableBinding
    private lateinit var viewModel: CO2TableViewModel
    private lateinit var adapter: RowAdapter
    private lateinit var  notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_co2_table)
        viewModel = ViewModelProvider(this).get(CO2TableViewModel::class.java)

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
            viewModel.cancelTable()
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

        createNotificationChannel(
            getString(R.string.co2_table_notification_channel_id),
            getString(R.string.co2_table_notification_channel_name)
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancelNotifications()
    }

    private fun startChronometer() {
        chronometer.base = SystemClock.elapsedRealtime()
        progressBar.progress = 0
        chronometer.start()
        binding.startButton.visibility = View.GONE
        binding.stopButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.VISIBLE
        notificationManager.sendNotification(getString(R.string.timer_running), this)
    }

    private fun stopChronometer() {
        chronometer.stop()
        binding.stopButton.visibility = View.GONE
        binding.pauseButton.visibility = View.GONE
        binding.resumeButton.visibility = View.GONE
        binding.startButton.visibility = View.VISIBLE
        clearUi()
        notificationManager.cancelNotifications()
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

    private fun createNotificationChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.co2_channel_description)

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
