package com.android.freediver.ui.home

import android.content.DialogInterface
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.freediver.R
import com.android.freediver.databinding.ActivityMainBinding
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
            viewModel.chronometerAction()
        }

        progressBar.max = viewModel.chronometerMaxValue
        chronometer.setOnChronometerTickListener {
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
        displayAlertDialogSaveData()
    }
    private fun clearUi() {
        progressBar.progress = 0
        chronometer.base = SystemClock.elapsedRealtime()
    }

    private fun updateProgressBar(chronometerBaseTime: Long) {
        val deltaTime = viewModel.getDeltaTime(chronometerBaseTime)
        progressBar.progress = viewModel.parseMillisToSeconds(deltaTime)
    }

    private fun displayAlertDialogSaveData() {
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("Save",
                    DialogInterface.OnClickListener { dialog, id ->
                        clearUi()
                        viewModel.saveBestTimeOnDataBase()
                        Toast.makeText(context, "Record saved on the database", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Save record on database")
                    })
                setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        clearUi()
                        Log.d(TAG, "Reset timer and discard data")
                    })
            }
            builder.setTitle("Save record")
                .setMessage("Your result is ${viewModel.parseMillisToStringFormat(viewModel.getDeltaTime(chronometer.base))}. Do you want to save the result?")
            // Create the AlertDialog
            builder.create()
        }
        alertDialog!!.show()
    }

}