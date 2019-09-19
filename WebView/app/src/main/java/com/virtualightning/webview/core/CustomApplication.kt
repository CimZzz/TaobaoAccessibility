package com.virtualightning.webview.core

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/08 13:08:28
 *  Project : WebView
 *  Since Version : Alpha
 */
class CustomApplication: Application() {
    companion object {
        lateinit var instance: CustomApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        CrashReport.initCrashReport(this, "6c8fa281f6", false)
    }
}