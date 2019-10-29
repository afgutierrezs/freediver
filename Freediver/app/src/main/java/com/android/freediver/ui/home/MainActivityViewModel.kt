package com.android.freediver.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.freediver.util.WatchState

class MainActivityViewModel : ViewModel() {

    companion object {
        private val TAG = MainActivityViewModel::class.java.simpleName
    }

    var watchState = WatchState.Stopped


    init {
        Log.d(TAG, "MainActivityViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "MainActivityViewModel destroyed!")
    }

    fun watchAction() {
        when (watchState) {
            WatchState.Stopped -> {
                startCount()
            }
            WatchState.Running -> {
                stopCount()
            }
        }
    }

    private fun startCount() {
        watchState = WatchState.Running
        Log.d(TAG, "Count Started")
    }

    private fun stopCount() {
        watchState = WatchState.Stopped
        Log.d(TAG, "Count Stopped")
    }

}