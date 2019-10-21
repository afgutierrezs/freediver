package com.android.freediver

import android.util.Log
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    enum class WatchState {
        Running, Stopped
    }

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

    fun watchAction(){
        if (watchState == WatchState.Stopped) {
            startCount()
        } else if (watchState == WatchState.Running) {
            stopCount()
        }
    }

    private fun startCount(){
        watchState = WatchState.Running
        Log.d(TAG, "Count Started")
    }

    private fun stopCount(){
        watchState = WatchState.Stopped
        Log.d(TAG, "Count Stopped")
    }

}