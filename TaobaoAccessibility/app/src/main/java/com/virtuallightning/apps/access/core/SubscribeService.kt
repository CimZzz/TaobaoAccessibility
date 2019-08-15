package com.virtuallightning.apps.access.core

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import com.virtuallightning.apps.access.accessibility.Constants
import com.virtuallightning.apps.access.utils.DataUtils
import com.virtuallightning.apps.access.utils.SysUtils
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/1/18 12:16:04
 *  Project : taoke_android
 *  Since Version : Alpha
 */
class SubscribeService : AccessibilityService() {

    private lateinit var currentAccessibility: BaseAccessibility

    override fun onCreate() {
        super.onCreate()
        DataUtils.init(this)
        Constants.init(this)
        currentAccessibility = Constants.Accessibility.ENTRY
    }

    override fun onInterrupt() {
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        currentAccessibility.onEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        SysUtils.destroy()
    }

    fun fireOtherAccessibility(accessibility: BaseAccessibility) {
        currentAccessibility.onHidden()
        currentAccessibility = accessibility
        accessibility.onFired()
    }
}