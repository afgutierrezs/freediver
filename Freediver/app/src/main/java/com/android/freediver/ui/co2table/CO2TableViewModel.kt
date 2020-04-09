package com.android.freediver.ui.co2table

import android.app.Application
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.android.freediver.model.CO2Table
import com.android.freediver.model.Row
import com.android.freediver.util.ChronometerAction
import com.android.freediver.util.ChronometerState
import com.android.freediver.util.Constants
import com.android.freediver.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class CO2TableViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val TAG = CO2TableViewModel::class.java.simpleName
    }

    val startCountEvent = SingleLiveEvent<Boolean>()
    val stopCountEvent = SingleLiveEvent<Boolean>()
    val pauseCountEvent = SingleLiveEvent<Boolean>()
    val resumeCountEvent = SingleLiveEvent<Boolean>()
    val chronometerMaxValueChanged = SingleLiveEvent<Boolean>()

    var chronometerState = ChronometerState.Stopped
    var chronometerMaxValue = Constants.BEST_TIME_PROGRESSBAR_MAX_VALUE
    var contractionsStartTime = 0L

    var rows = mutableListOf<Row>()

    init {
        Log.d(TAG, "ViewModel created!")
        loadDefaultTable()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel destroyed!")
    }

    fun loadDefaultTable() {
        rows = createTableFromBestTime(120, 65).rows.toMutableList()
    }

    fun chronometerAction(action: ChronometerAction) {
        when (action) {
            ChronometerAction.Stop -> {
                stopCount()
            }
            ChronometerAction.Pause -> {
                pauseCount()
            }
            ChronometerAction.Resume -> {
                resumeCount()
            }
            ChronometerAction.Start -> {

            }
        }
    }

    fun startTable() {
        GlobalScope.launch(context = Dispatchers.Main) {
            for (item in rows) {
                startCount(item.hold)
                delay(item.hold * 1000L)
                stopCount()
                startCount(item.breath)
                delay(item.breath * 1000L)
                stopCount()
            }
        }
    }

    private fun startCount(maxValue: Int) {
        chronometerMaxValue = maxValue
        chronometerMaxValueChanged.value = true
        chronometerState = ChronometerState.Running
        startCountEvent.value = true
        Log.d(TAG, "Count Started")
    }

    private fun stopCount() {
        chronometerState = ChronometerState.Stopped
        stopCountEvent.value = true
        Log.d(TAG, "Count Stopped")
    }

    private fun pauseCount() {
        chronometerState = ChronometerState.Paused
        pauseCountEvent.value = true
        Log.d(TAG, "Count Paused")
    }

    private fun resumeCount() {
        chronometerState = ChronometerState.Running
        resumeCountEvent.value = true
        Log.d(TAG, "Count Resumed")
    }

    private fun createTableFromBestTime(bestTime: Long, percentage: Int): CO2Table {

        val co2Table = CO2Table()
        val rows = arrayListOf<Row>()

        val holdTime = ((bestTime * percentage) / 100).toInt()
        var breathTime = 120

        for (i in 1..10) {
            rows.add(Row(0, holdTime, breathTime))
            breathTime = if (breathTime - 15 <= 30) {
                30
            } else {
                breathTime - 15
            }
        }

        co2Table.name = "CO2 Table Default"
        co2Table.rows = rows

        return co2Table
    }

    fun parseMillisToSeconds(millis: Long): Int {
        return ((millis / 1000)).toInt()
    }

    fun getDeltaTime(chronometerBaseTime: Long): Long {
        val systemCurrentTime = SystemClock.elapsedRealtime()
        return systemCurrentTime - chronometerBaseTime
    }
}