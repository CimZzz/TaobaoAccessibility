package com.virtualightning.webview.core

import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/06 20:29:49
 *  Project : WebView
 *  Since Version : Alpha
 */
class ViewTouchBundle(
    val bundle: TouchBundle,
    private val startX: Float,
    endX: Float,
    private val y: Float,
    val callback: TouchOverCallback
) {
    var isOver = false
    var isWaitOver = false
    var isEnd = false
    private var isFirstOver = true
    private val totalDistanceX: Float = endX - startX
    private var totalMoveX: Float = 0f
    private var totalMoveY: Float = 0f
    private var currentMoreCount: Int = 0

    fun close() {
        bundle.up()
        isEnd = true
    }

    fun down() {
        bundle.down(startX, y)
    }

    fun move(
        x: Float,
        y: Float,
        time: Long
     ) {
        totalMoveX += x
        val moveY = if(totalMoveY > 0)
            totalMoveY - y
        else totalMoveY + y

        totalMoveY += moveY


        if(totalMoveX >= totalDistanceX) {
            isWaitOver = true
            bundle.move(totalMoveX - totalDistanceX, moveY, time)
        }
        else bundle.move(x, moveY, time)
    }

    fun waitMove(x1: Float, x2: Float, x3: Float, time1: Long, time2: Long, time3: Long) {
        bundle.move(x1, 0f, time1)
            .move(x2, 0f, time2)
            .move(x3, 0f, time3)

        currentMoreCount ++
        if(currentMoreCount > 3)
            isOver = true

    }


    fun overMove() {
        if(isFirstOver) {
            isFirstOver = false
            callback()
        }
        bundle.up(0f, 0f, 20L)
        isEnd = true
    }
}