package com.virtualightning.webview.core

import android.os.SystemClock

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/06 08:23:33
 *  Project : WebView
 *  Since Version : Alpha
 */
class TimerBundle(val key: String, val intervalTime: Long, val goalTime: Long, val isOnce: Boolean, val callback: TimerCallback) {
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

    fun checkOver(): Boolean =
        if(goalTime == -1L)
            false
        else lastTime - startTime >= goalTime
}