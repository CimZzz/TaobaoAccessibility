package com.virtuallightning.apps.accessibility.core

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import com.virtuallightning.apps.accessibility.accessibility.Constants
import com.virtuallightning.apps.accessibility.utils.DataUtils
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/1/18 12:16:04
 *  Project : taoke_android
 *  Since Version : Alpha
 */
class SubscribeService : AccessibilityService() {
    init {
        DataUtils.init(this)
        Constants.init(this)
    }

    private var currentAccessibility: BaseAccessibility = TestAccessibility(this)
//    private var currentAccessibility: BaseAccessibility = Constants.Accessibility.ENTRY

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private var timerBundleMap: ConcurrentHashMap<String, TimerBundle>? = ConcurrentHashMap()

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun onInterrupt() {
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        currentAccessibility.onEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        closeTimer()
        val tempMap = timerBundleMap
        timerBundleMap = null
        tempMap?.values?.forEach {
            it.isDestroy = true
        }
    }

    fun fireOtherAccessibility(accessibility: BaseAccessibility) {
        currentAccessibility = accessibility
        accessibility.onFired()
    }

    fun registerTimerCallback(key: String, intervalTime: Long, callback: TimerCallback) {
        unregisterTimerCallback(key)
        timerBundleMap?.put(key, TimerBundle(intervalTime, callback))
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
                        handler.post {
                            if(!it.isDestroy)
                                it.callback()
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
}