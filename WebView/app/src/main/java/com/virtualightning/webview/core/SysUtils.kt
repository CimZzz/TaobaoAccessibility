package com.virtualightning.webview.core

import android.Manifest
import android.app.Instrumentation
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/06 08:23:57
 *  Project : WebView
 *  Since Version : Alpha
 */
object SysUtils {
    private val beginTime = SystemClock.elapsedRealtime()

    fun getElapsedSecond(): Long = (SystemClock.elapsedRealtime() - beginTime) / 1000

    fun destroy() {
        shutdown()
        closeTimer()
        val tempMap = timerBundleMap
        timerBundleMap = null
        tempMap?.values?.forEach {
            it.isDestroy = true
        }
    }

    private val contactPermission = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)

    fun checkContactPermission(context: Context): Boolean {
        return checkPermission(
            context,
            contactPermission
        )
    }

    fun checkPermission(context: Context, permissions: Array<String>): Boolean {
        var isGrant = true
        for(permission in permissions) {
            isGrant = ContextCompat.checkSelfPermission(context , permission) == PackageManager.PERMISSION_GRANTED
            if(!isGrant)
                break
        }

        return isGrant
    }

    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    fun runOnMain(runnable: () -> Unit) {
        handler.post(runnable)
    }
    fun runOnMainDelay(delay: Long, runnable: () -> Unit) {
        handler.postDelayed(runnable, delay)
    }


    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private var timerBundleMap: ConcurrentHashMap<String, TimerBundle>? = ConcurrentHashMap()

    fun registerTimerCallback(key: String, intervalTime: Long, goalTime: Long = -1L, isOnce: Boolean = false, callback: TimerCallback) {
        unregisterTimerCallback(key)
        timerBundleMap?.put(key,
            TimerBundle(key, intervalTime, goalTime, isOnce, callback)
        )
        ensureStartTimer()
    }

    fun unregisterTimerCallback(key: String) {
        timerBundleMap?.remove(key)?.isDestroy = true
    }

    private fun ensureStartTimer() {
        if(timer != null)
            return
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                val currentTime = SystemClock.elapsedRealtime()
                timerBundleMap?.values?.forEach {
                    if(it.checkTime(currentTime)) {
                        runOnMain {
                            if (!it.isDestroy) {
                                if (it.isOnce || it.checkOver()) {
                                    unregisterTimerCallback(it.key)
                                    it.callback(true)
                                } else it.callback(false)
                            }
                        }
                    }
                }
            }
        }
        timer?.schedule(timerTask, 0L, 200L)
    }

    private fun closeTimer() {
        timerTask?.cancel()
        timerTask = null
        timer?.cancel()
        timer = null
    }


    /**
     * 线程池
     */

    private val threadPool = Executors.newCachedThreadPool()

    fun run(runnable: () -> Unit) {
        threadPool.execute(runnable)
    }

    private fun shutdown() {
        threadPool.shutdownNow()
    }


    fun sendTouchEvent(view: View, actionId: Int, x: Float, y: Float, downTime: Long, eventTime: Long) {
        val event = MotionEvent.obtain(downTime, eventTime, actionId, x, y, 0)
        view.dispatchTouchEvent(event)
        event.recycle()
    }

    fun sendKeyCode(str: String) {
        run {
            val instrumentation = Instrumentation()
            str.forEach {
                instrumentation.sendStringSync(it.toString())
            }
        }
    }

}