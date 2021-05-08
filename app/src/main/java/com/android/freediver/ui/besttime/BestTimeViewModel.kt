package com.android.freediver.ui.besttime

import android.app.Application
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.android.freediver.database.FreediverDatabase
import com.android.freediver.model.BestTime
import com.android.freediver.util.ChronometerState
import com.android.freediver.util.Constants.Companion.BEST_TIME_PROGRESSBAR_MAX_VALUE
import com.android.freediver.util.SingleLiveEvent
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.*


class BestTimeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val TAG = BestTimeViewModel::class.java.simpleName
    }

    val startCountEvent = SingleLiveEvent<Boolean>()
    val stopCountEvent = SingleLiveEvent<Boolean>()
    val currentBestTimeUpdated = SingleLiveEvent<Boolean>()

    var chronometerState = ChronometerState.Stopped
    var chronometerMaxValue = BEST_TIME_PROGRESSBAR_MAX_VALUE
    var bestTimeDuration = 0L
    var contractionsStartTime = 0L

    val bestTimeDao = FreediverDatabase.getInstance(application).bestTimeDao
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var currentBestTime = BestTime()


    init {
        queryCurrentBestTime()
        Log.d(TAG, "BestTimeViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.d(TAG, "BestTimeViewModel destroyed!")
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
        uiScope.launch {
            val bestTime = BestTime()
            bestTime.duration = bestTimeDuration
            bestTime.contractionsStart = contractionsStartTime
            bestTime.date = System.currentTimeMillis()
            bestTimeDao.insert(bestTime)
            queryCurrentBestTime()
        }
    }

    private fun queryCurrentBestTime() {
        uiScope.launch {
            val time = bestTimeDao.getCurrentBestTime()
            if (time != null) {
                currentBestTime = time
                currentBestTimeUpdated.value = true
            }
        }
    }

    fun getCurrentBestTimeDuration() : String {
        return parseMillisToStringFormat(currentBestTime.duration)
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