package com.android.freediver.ui.home

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.android.freediver.R
import com.android.freediver.databinding.ActivityMainBinding
import com.android.freediver.util.WatchState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        binding.playButton.setOnClickListener {
            when (viewModel.watchState) {
                WatchState.Stopped -> {
                    chronometer.base = SystemClock.elapsedRealtime()
                    progressBar.progress = 0
                    chronometer.start()
                    viewModel.watchState = WatchState.Running
                    playButton.background = getDrawable(R.drawable.ic_stop_circle_ripple)
                }
                WatchState.Running -> {
                    chronometer.stop()
                    viewModel.watchState = WatchState.Stopped
                    playButton.background = getDrawable(R.drawable.ic_play_circle_ripple)
                }
            }
        }

        initProgressBar()
        chronometer.setOnChronometerTickListener {
            progressBar.progress =  ((it.base * 1000) % 60).toInt()
        }
    }

    private fun initProgressBar() {
        progressBar.max = 60
    }
    
}
