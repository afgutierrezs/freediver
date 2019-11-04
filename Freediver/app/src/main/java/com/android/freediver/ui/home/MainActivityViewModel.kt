package com.android.freediver.ui.home

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.freediver.model.BestTime
import com.android.freediver.util.ChronometerState
import com.android.freediver.util.Constants.Companion.BEST_TIME_PROGRESSBAR_MAX_VALUE
import com.android.freediver.util.SingleLiveEvent
import java.util.concurrent.TimeUnit

class MainActivityViewModel : ViewModel() {

    companion object {
        private val TAG = MainActivityViewModel::class.java.simpleName
    }

    val startCountEvent = SingleLiveEvent<Boolean>()
    val stopCountEvent = SingleLiveEvent<Boolean>()

    var chronometerState = ChronometerState.Stopped
    var chronometerMaxValue = BEST_TIME_PROGRESSBAR_MAX_VALUE
    var bestTimeDuration = 0L
    var contractionsStartTime = 0L

    init {
        Log.d(TAG, "MainActivityViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "MainActivityViewModel destroyed!")
    }

    fun chronometerAction() {
        when (chronometerState) {
            ChronometerState.Stopped -> {
                startCount()
            }
            ChronometerState.Running -> {
                stopCount()
            }
        }
    }

    private fun startCount() {
        chronometerState = ChronometerState.Running
        startCountEvent.value = true
        Log.d(TAG, "Count Started")
    }

    private fun stopCount() {
        chronometerState = ChronometerState.Stopped
        stopCountEvent.value = true
        Log.d(TAG, "Count Stopped")
    }

    fun saveBestTimeOnDataBase() {
        val bestTime = BestTime()
    }

    fun parseMillisToStringFormat(millis: Long): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
        )
    }

    fun parseMillisToSeconds(millis: Long): Int {
        return  ((millis / 1000) % 60).toInt()
    }

    fun getDeltaTime(chronometerBaseTime: Long): Long {
        val systemCurrentTime = SystemClock.elapsedRealtime()
        return systemCurrentTime - chronometerBaseTime
    }

}