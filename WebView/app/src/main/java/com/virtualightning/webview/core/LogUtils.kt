package com.virtualightning.webview.core

import android.util.Log

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/08 12:24:07
 *  Project : WebView
 *  Since Version : Alpha
 */
object LogUtils {
    fun log(any: Any?) {
        Log.v("CimZzz", any.toString())
    }
    fun url(any: Any?) {
        Log.v("CimZzz-URL", any.toString())
    }
}