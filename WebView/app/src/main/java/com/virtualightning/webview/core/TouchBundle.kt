package com.virtualightning.webview.core

import android.os.SystemClock
import android.view.MotionEvent
import android.view.View

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/06 20:29:49
 *  Project : WebView
 *  Since Version : Alpha
 */
class TouchBundle(
    private val view: View
) {
    private var downTime: Long = 0L
    private var curTime: Long = 0L
    private var x: Float = 0f
    private var y: Float = 0f

    fun down(x: Float, y: Float): TouchBundle {
        downTime = SystemClock.elapsedRealtime()
        curTime = downTime
        this.x = x
        this.y = y
        SysUtils.sendTouchEvent(view, MotionEvent.ACTION_DOWN, x, y, downTime, curTime)
        return this
    }

    fun move(xDiff: Float = 0f, yDiff: Float = 0f, distanceTime: Long = 0L): TouchBundle {
        if(distanceTime == -1L)
            curTime += SystemClock.elapsedRealtime() - curTime
        else curTime += distanceTime
        x += xDiff
        y += xDiff
        SysUtils.sendTouchEvent(view, MotionEvent.ACTION_MOVE, x, y, downTime, curTime)
        return this
    }

    fun up(xDiff: Float = 0f, yDiff: Float = 0f, distanceTime: Long = 0L): TouchBundle {
        if(distanceTime == -1L)
            curTime += SystemClock.elapsedRealtime() - curTime
        else curTime += distanceTime
        x += xDiff
        y += xDiff
        SysUtils.sendTouchEvent(view, MotionEvent.ACTION_UP, x, y, downTime, curTime)
        return this
    }
}

fun mockTouch(view: View): TouchBundle = TouchBundle(view)