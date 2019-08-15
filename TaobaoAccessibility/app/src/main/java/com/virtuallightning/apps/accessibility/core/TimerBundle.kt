package com.virtuallightning.apps.accessibility.core

import android.os.SystemClock

class TimerBundle(val intervalTime: Long, val callback: TimerCallback) {
    val startTime = SystemClock.elapsedRealtime()
    var lastTime: Long = startTime
    var isDestroy: Boolean = false

    fun checkTime(currentTime: Long): Boolean {
        if(isDestroy)
            return false
        if(currentTime - lastTime >= intervalTime) {
            lastTime = currentTime
            return true
        }

        return false
    }
}