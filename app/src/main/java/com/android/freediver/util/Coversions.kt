package com.android.freediver.util

class Coversions{
    companion object {
        fun secsToTime(secs : Int): String {
            return String.format("%02d:%02d", (secs % 3600) / 60, secs % 60)
        }
    }
}